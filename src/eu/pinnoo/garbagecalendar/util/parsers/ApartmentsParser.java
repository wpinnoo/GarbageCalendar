package eu.pinnoo.garbagecalendar.util.parsers;

import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.util.parsers.CalendarParser;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
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
public class ApartmentsParser extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.APARTMENTS_URL;
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Appartementen";
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

                Address address = UserData.getInstance().getAddress();
//                if (address.getZipcode() == zipcode
//                        && address.getStreetname().equals(street)
//                        && address.getNr() >= beginNr
//                        && address.getNr() <= endNr) {
//                    address.markAsApartment(true);
//                    break;
//                }
            } catch (JSONException ex) {
                Logger.getLogger(CalendarParser.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            } catch (NullPointerException e) {
                Logger.getLogger(CalendarParser.class.getName()).log(Level.SEVERE, null, e);
                return 1;
            }
        }
        return 0;
    }
}
