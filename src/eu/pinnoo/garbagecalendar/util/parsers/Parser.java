package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.Type;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
public abstract class Parser {

    public static final int NO_INTERNET_CONNECTION = 1;
    
    protected abstract String getURL();

    protected abstract String getJSONArrayName();

    protected abstract int fetchData(JSONArray data);

    /**
     *
     * @param data
     * @return 0 when fetching was successful, otherwise 1
     */
    public int loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return NO_INTERNET_CONNECTION;
        }
        JSONArray arr = downloadData();
        return fetchData(arr);
    }

    private JSONArray downloadData() {
        String result;
        try {
            InputStream inp = Network.getStream(getURL());
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
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException e) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

        JSONArray arr = null;
        try {
            if (!result.isEmpty() && !result.equals("null\n")) {
                JSONObject obj = new JSONObject(result);
                arr = obj.getJSONArray(getJSONArrayName());
            }
        } catch (JSONException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return arr;
        }
    }

    protected Type[] parseGarbageType(String str) {
        HashMap<String, Type> map = new HashMap<String, Type>();
        map.put("Rest", Type.REST);
        map.put("GFT", Type.GFT);
        map.put("PMD", Type.PMD);
        map.put("Papier & karton", Type.PK);
        map.put("Glas", Type.GLAS);

        String[] types = str.split("/");
        Type[] results = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            if (map.containsKey(types[i].trim())) {
                results[i] = map.get(types[i].trim());
            }
        }
        return results;
    }
}
