package eu.pinnoo.garbagecalendar.util.parsers;

import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Sector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class StreetsParser extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.STREETS_URL;
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Stratenlijst";
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

                Address address = new Address();

                address.setZipcode(Integer.parseInt(obj.optString("postcode")));
                address.setStreetname(obj.optString("straatnaam"));
                address.setCode(obj.optString("straatcode"));
                address.setSector(new Sector(obj.optString("sector")));
                address.setNrEvenBegin(obj.optString("even van "));
                address.setNrEvenEnd(obj.optString("even tot"));
                address.setNrOddBegin(obj.optString("oneven van "));
                address.setNrOddEnd(obj.optString("oneven tot"));
                address.setCity(obj.optString("gemeente"));

                AddressData.getInstance().addAddress(address);
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
