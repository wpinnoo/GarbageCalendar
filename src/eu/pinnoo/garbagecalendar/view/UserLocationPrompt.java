package eu.pinnoo.garbagecalendar.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import eu.pinnoo.garbagecalendar.models.DataModel;
import eu.pinnoo.garbagecalendar.models.UserModel;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserLocationPrompt {

    private final Activity parent;

    public UserLocationPrompt(Activity parent) {
        this.parent = parent;
    }

    private InputStream getStream(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(1000);
        return urlConnection.getInputStream();
    }

    public void prompt() {
        final EditText input = new EditText(parent);
        new AlertDialog.Builder(parent)
                .setTitle("Your location")
                .setMessage("Please enter your address.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        try {
                            parseAddress(value);
                        } catch (IOException ex) {
                            Logger.getLogger(UserLocationPrompt.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ((MainActivity) parent).scrapeDataAndCreateGUI();
                    }
                }).show();
    }

    public void parseAddress(String address) throws IOException {
        String url = LocalConstants.GOOGLE_MAPS_API + "?";
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("address", address));
        params.add(new BasicNameValuePair("sensor", "false"));
        url += URLEncodedUtils.format(params, "utf-8");

        InputStream inp = getStream(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp, LocalConstants.ENCODING), 8);
        StringBuilder builder = new StringBuilder();
        builder.append(reader.readLine()).append("\n");

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        inp.close();
        String result = builder.toString();

        JSONArray arr = null;
        try {
            if (!result.isEmpty() && !result.equals("null\n")) {
                JSONObject obj = new JSONObject(result);
                if (obj.has("status") && obj.getString("status").equals("OK")) {
                    arr = obj.getJSONArray("results");
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(UserLocationPrompt.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (arr == null || arr.length() == 0) {
                addressNotFound();
            } else if (arr.length() > 1) {
                multiplePossibilities(arr);
            } else {
                try {
                    JSONArray addressArr = arr.getJSONObject(0).getJSONArray("address_components");
                    for (int i = 0; i < addressArr.length(); i++) {
                        JSONObject obj = addressArr.getJSONObject(i);
                        String type = obj.getJSONArray("types").getString(0);
                        if (type.equals("street_number")) {
                            UserModel.getInstance().setNr(Integer.parseInt(obj.getString("long_name")));
                        } else if (type.equals("route")) {
                            UserModel.getInstance().setStreetname(obj.getString("long_name"));
                        } else if (type.equals("sublocality")) {
                            UserModel.getInstance().setCity(obj.getString("long_name"));
                        } else if (type.equals("postal_code")) {
                            UserModel.getInstance().setZipcode(Integer.parseInt(obj.getString("long_name")));
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(UserLocationPrompt.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException e) {
                    Logger.getLogger(UserLocationPrompt.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private void addressNotFound() {
        // TODO: show popup with error message
    }

    private void multiplePossibilities(JSONArray arr) {
        List<String> possibilities = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                possibilities.add(obj.getString("formatted_address"));
            } catch (JSONException ex) {
                Logger.getLogger(UserLocationPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // TODO: show this list of addresses so that the user can chose one
    }
}
