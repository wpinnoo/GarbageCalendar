package eu.pinnoo.garbagecalendar.util.scrapers;

import eu.pinnoo.garbagecalendar.models.UserModel;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Sector;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class StreetsScraper extends Scraper {

    @Override
    protected URL getURL() throws MalformedURLException {
        return new URL(LocalConstants.STREETS_URL);
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Stratenlijst";
    }

    @Override
    protected String getCacheName() {
        return "cache_streets";
    }

    /**
     * 
     * @param data
     * @return 0 when fetching was successful, otherwise 1 
     */
    @Override
    protected int fetchData(JSONArray data) {
        if (data == null) {
            return 1;
        }

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);

                int zipcode = Integer.parseInt(obj.getString("postcode"));
                String street = obj.getString("straatnaam");

                if (UserModel.getInstance().getZipcode() == zipcode && UserModel.getInstance().getStreetname().equals(street)) {
                    if (!obj.has("even van") || obj.getString("even van").isEmpty()) {
                        UserModel.getInstance().setSector(new Sector(obj.getString("sector")));
                        break;
                    }

                    int beginEven = Integer.parseInt(obj.getString("even van"));
                    int endEven = Integer.parseInt(obj.getString("even tot"));
                    int beginOdd = Integer.parseInt(obj.getString("oneven van"));
                    int endOdd = Integer.parseInt(obj.getString("oneven tot"));

                    if (UserModel.getInstance().getNr() % 2 == 0
                            && beginEven != -1
                            && beginEven <= UserModel.getInstance().getNr()
                            && endEven >= UserModel.getInstance().getNr()) {
                        UserModel.getInstance().setSector(new Sector(obj.getString("sector")));
                        break;
                    }

                    if (UserModel.getInstance().getNr() % 2 != 0
                            && beginOdd != -1
                            && beginOdd <= UserModel.getInstance().getNr()
                            && endOdd >= UserModel.getInstance().getNr()) {
                        UserModel.getInstance().setSector(new Sector(obj.getString("sector")));
                        break;
                    }
                }
            } catch (JSONException ex) {
                Logger.getLogger(CalendarScraper.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            } catch (NullPointerException e) {
                Logger.getLogger(CalendarScraper.class.getName()).log(Level.SEVERE, null, e);
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected String getSharedPrefName() {
        return "data_streets_update";
    }
}
