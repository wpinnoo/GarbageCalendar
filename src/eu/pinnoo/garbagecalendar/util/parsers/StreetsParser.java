package eu.pinnoo.garbagecalendar.util.parsers;

import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Sector;
import eu.pinnoo.garbagecalendar.data.UserData;
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

        Address address = UserData.getInstance().getAddress();

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);

                int zipcode = Integer.parseInt(obj.getString("postcode"));
                String street = obj.getString("straatnaam");

                if (address.getZipcode() == zipcode && address.getStreetname().equals(street)) {
                    if (!obj.has("even van") || obj.getString("even van").isEmpty()) {
                        address.setSector(new Sector(obj.getString("sector")));
                        break;
                    }

                    int beginEven = Integer.parseInt(obj.getString("even van"));
                    int endEven = Integer.parseInt(obj.getString("even tot"));
                    int beginOdd = Integer.parseInt(obj.getString("oneven van"));
                    int endOdd = Integer.parseInt(obj.getString("oneven tot"));

                    if (address.getNr() % 2 == 0
                            && beginEven != -1
                            && beginEven <= address.getNr()
                            && endEven >= address.getNr()) {
                        address.setSector(new Sector(obj.getString("sector")));
                        break;
                    }

                    if (address.getNr() % 2 != 0
                            && beginOdd != -1
                            && beginOdd <= address.getNr()
                            && endOdd >= address.getNr()) {
                        address.setSector(new Sector(obj.getString("sector")));
                        break;
                    }
                }
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
