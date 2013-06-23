package eu.pinnoo.garbagecalendar.util.scrapers;

import eu.pinnoo.garbagecalendar.util.scrapers.CalendarScraper;
import eu.pinnoo.garbagecalendar.data.models.UserModel;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
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
public class ApartmentsScraper extends Scraper {

    @Override
    protected URL getURL() throws MalformedURLException {
        return new URL(LocalConstants.APARTMENTS_URL);
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Appartementen";
    }

    @Override
    protected String getCacheName() {
        return LocalConstants.CacheName.APARTMENTS.toString();
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

        UserModel.getInstance().markAsApartment(false);

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);

                String street = obj.getString("adres");

                int beginNr, endNr;

                try {
                    beginNr = Integer.parseInt(obj.getString("huisnr van"));
                } catch (NumberFormatException e) {
                    beginNr = Integer.MIN_VALUE;
                }
                try {
                    endNr = Integer.parseInt(obj.getString("huisnr tot"));
                } catch (NumberFormatException e) {
                    endNr = Integer.MAX_VALUE;
                }

                int zipcode = Integer.parseInt(obj.getString("pc"));

                if (UserModel.getInstance().getZipcode() == zipcode
                        && UserModel.getInstance().getStreetname().equals(street)
                        && UserModel.getInstance().getNr() >= beginNr
                        && UserModel.getInstance().getNr() <= endNr) {
                    UserModel.getInstance().markAsApartment(true);
                    break;
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
        return LocalConstants.CacheName.UPDATE_APARTMENTS.toString();
    }
}
