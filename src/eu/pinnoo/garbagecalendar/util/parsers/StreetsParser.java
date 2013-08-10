package eu.pinnoo.garbagecalendar.util.parsers;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Network;
import eu.pinnoo.garbagecalendar.data.PrimitiveAddress;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class StreetsParser extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.STREETS_URL;
    }

    /**
     *
     * @param data
     * @return 0 when fetching was successful, otherwise 1
     */
    @Override
    protected int fetchData(ArrayList data) {
        if (data == null) {
            return 1;
        }

        ArrayList<Address> list = new ArrayList<Address>();
        for (int i = 0; i < data.size(); i++) {
            PrimitiveAddress prAddr = (PrimitiveAddress) data.get(i);
            list.add(new Address(prAddr));
        }
        AddressData.getInstance().setAddresses(list);
        return 0;
    }

    @Override
    protected ArrayList downloadData() {
        ArrayList<PrimitiveAddress> list = new ArrayList<PrimitiveAddress>();
        try {
            InputStream inp = Network.getStream(getURL());
            JsonReader reader = new JsonReader(new InputStreamReader(inp, LocalConstants.ENCODING));
            PrimitiveAddressList results = new GsonBuilder().create().fromJson(reader, PrimitiveAddressList.class);
            list.addAll((Collection<PrimitiveAddress>) results.list);
            reader.close();
        } catch (Exception e) {
            Log.d(LocalConstants.LOG, e.toString());
        } finally {
            return list;
        }
    }

    public class PrimitiveAddressList {

        @SerializedName("IVAGO-Stratenlijst")
        public ArrayList<PrimitiveAddress> list;
    }
}
