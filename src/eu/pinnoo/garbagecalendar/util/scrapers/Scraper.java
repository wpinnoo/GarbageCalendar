package eu.pinnoo.garbagecalendar.util.scrapers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import eu.pinnoo.garbagecalendar.models.DataModel;
import eu.pinnoo.garbagecalendar.util.GarbageType;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Scraper {

    protected abstract URL getURL() throws MalformedURLException;

    protected abstract String getJSONArrayName();

    protected abstract String getCacheName();
    
    protected abstract String getSharedPrefName();

    /**
     * 
     * @param data
     * @return 0 when fetching was successful, otherwise 1 
     */
    protected abstract int fetchData(JSONArray data);

    private InputStream getStream() throws IOException {
        URLConnection urlConnection = getURL().openConnection();
        urlConnection.setConnectTimeout(1000);
        return urlConnection.getInputStream();
    }

    private boolean needsUpdate() {
        int lastUpdate = DataModel.getInstance().getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getInt(getSharedPrefName(), 0);
        return lastUpdate != Calendar.getInstance().get(Calendar.YEAR);
    }

    private boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) DataModel.getInstance().getContainer().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * 
     * @param data
     * @return 0 when fetching was successful, otherwise 1 
     */
    public int loadData(boolean force) {
        JSONArray arr;
        if (networkAvailable() && (force || needsUpdate())) {
            arr = downloadData();
        } else {
            arr = readCache();
        }
        return fetchData(arr);
    }

    private JSONArray readCache() {
        JSONArray arr = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(new File(DataModel.getInstance().getContainer().getCacheDir(), "") + getCacheName() + ".srl")));
            String line, content = "";
            while ((line = br.readLine()) != null) {
                content += line;
            }
            arr = new JSONArray(content);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StreamCorruptedException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return arr;
        }
    }

    private JSONArray downloadData() {
        String result;
        try {
            InputStream inp = getStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inp, LocalConstants.ENCODING), 8);
            StringBuilder builder = new StringBuilder();
            builder.append(reader.readLine()).append("\n");

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            inp.close();
            result = builder.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException e) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

        JSONArray arr = null;
        try {
            if (!result.isEmpty() && !result.equals("null\n")) {
                JSONObject obj = new JSONObject(result);
                arr = obj.getJSONArray(getJSONArrayName());
            }
        } catch (JSONException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(DataModel.getInstance().getContainer().getCacheDir(), "") + getCacheName() + ".srl"));
                out.write(arr.toString());
                out.close();
                DataModel.getInstance().getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                        .edit()
                        .putInt(getSharedPrefName(), Calendar.getInstance().get(Calendar.YEAR))
                        .commit();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                return arr;
            }
        }
    }

    protected GarbageType[] parseGarbageType(String str) {
        HashMap<String, GarbageType> map = new HashMap<String, GarbageType>();
        map.put("Rest", GarbageType.REST);
        map.put("GFT", GarbageType.GFT);
        map.put("PMD", GarbageType.PMD);
        map.put("Papier & karton", GarbageType.PK);
        map.put("Glas", GarbageType.GLAS);

        String[] types = str.split("/");
        GarbageType[] results = new GarbageType[types.length];
        for (int i = 0; i < types.length; i++) {
            if (map.containsKey(types[i].trim())) {
                results[i] = map.get(types[i].trim());
            }
        }
        return results;
    }
}
