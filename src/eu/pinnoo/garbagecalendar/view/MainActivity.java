package eu.pinnoo.garbagecalendar.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.models.DataModel;
import eu.pinnoo.garbagecalendar.models.UserModel;
import eu.pinnoo.garbagecalendar.util.AreaType;
import eu.pinnoo.garbagecalendar.util.GarbageCollection;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Sector;
import eu.pinnoo.garbagecalendar.util.scrapers.ApartmentsScraper;
import eu.pinnoo.garbagecalendar.util.scrapers.CalendarScraper;
import eu.pinnoo.garbagecalendar.util.scrapers.StreetsScraper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeModels();
    }

    private void initializeModels() {
        UserModel.getInstance().setContainer(this);
        DataModel.getInstance().setContainer(this);

        Sector s = new Sector(UserModel.getInstance().getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getString("user_sector", LocalConstants.DEFAULT_SECTOR));
        if (s.getType().equals(AreaType.NONE)) {
            promptUserLocation();
        } else {
            UserModel.getInstance().restoreFromCache();
            scrapeData(true);
            createGUI();
        }
    }

    private void locationIsApartment() {
        // TODO: notify user
    }

    private void scrapeData(boolean force) {
        new ApartmentsScraper().loadData(force);
        if (UserModel.getInstance().isApartment()) {
            locationIsApartment();
            return;
        }

        if (force || UserModel.getInstance().getSector().toString().equals(LocalConstants.DEFAULT_SECTOR)) {
            new StreetsScraper().loadData(force);
        }

        new CalendarScraper().loadData(force);
    }

    private InputStream getStream(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(1000);
        return urlConnection.getInputStream();
    }

    public void promptUserLocation() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Your location")
                .setMessage("Please enter your address.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String address = input.getText().toString();
                        new AddressParser(address).execute();
                    }
                }).show();
    }

    private class AddressParser extends AsyncTask<Void, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String address;

        public AddressParser(String address) {
            this.address = address;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Looking up your address...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                parseAddress(address);
            } catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
            new DataScraper().execute();
        }
    }

    private class DataScraper extends AsyncTask<Void, Void, Integer> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading calendar...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            scrapeData(true);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
            createGUI();
        }
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
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException e) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // TODO: show this list of addresses so that the user can chose one
    }

    private void createGUI() {
        List<GarbageCollection> collections = DataModel.getInstance().getCollections();
        Iterator<GarbageCollection> it = collections.iterator();
        int i = 0;
        while (it.hasNext()) {
            GarbageCollection col = it.next();
            if (col.getDate().before(Calendar.getInstance().getTime())) {
                continue;
            }

            String types = "";
            for (int k = 0; k < col.getTypes().length; k++) {
                types += k == 0 ? col.getTypes()[k].toString() : ", " + col.getTypes()[k].toString();
            }

            addTableRow(LocalConstants.DATE_FORMATTER_MAIN_TABLE.format(col.getDate()), types, i++);
        }
    }

    private void addTableRow(String date, String types, int rowNumber) {
        LayoutInflater inflater = getLayoutInflater();
        TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        TableRow tr = (TableRow) inflater.inflate(R.layout.main_table_row, tl, false);
        TextView labelTypes = (TextView) tr.findViewById(R.id.main_row_types);
        labelTypes.setText(types);
        labelTypes.setPadding(1, 5, 5, 5);
        TextView labelDate = (TextView) tr.findViewById(R.id.main_row_date);
        labelDate.setText(date);
        labelDate.setPadding(5, 5, 5, 5);

        if (rowNumber % 2 == 0) {
            tr.setBackgroundColor(Color.GRAY);
            labelTypes.setTextColor(Color.BLACK);
            labelDate.setTextColor(Color.BLACK);

        } else {
            tr.setBackgroundColor(Color.LTGRAY);
            labelTypes.setTextColor(Color.BLACK);
            labelDate.setTextColor(Color.BLACK);
        }

        tl.addView(tr);
    }
}
