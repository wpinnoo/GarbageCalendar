package eu.pinnoo.garbagecalendar.util.parsers;

import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Sector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class StreetsParser<PrimitiveAddress> extends Parser {

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
    protected int fetchData(List data) {
        if (data == null) {
            return 1;
        }

        for (int i = 0; i < data.size(); i++) {
            eu.pinnoo.garbagecalendar.data.PrimitiveAddress prAddr = (eu.pinnoo.garbagecalendar.data.PrimitiveAddress) data.get(i);
            Address address = new Address(prAddr);
            AddressData.getInstance().addAddress(address);
        }
        return 0;
    }
}
