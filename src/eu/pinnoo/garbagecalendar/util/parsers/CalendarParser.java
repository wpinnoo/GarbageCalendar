package eu.pinnoo.garbagecalendar.util.parsers;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.util.Network;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CalendarParser<PrimitiveCollection> extends Parser<PrimitiveCollection> {

    @Override
    protected String getURL() {
        return LocalConstants.CALENDAR_URL;
    }

    /**
     *
     * @param data
     * @return 0 when fetching was successful, otherwise 1
     */
    @Override
    protected int fetchData(List<PrimitiveCollection> data) {
        if (data == null) {
            return 1;
        }
        CollectionsData.getInstance().resetCollections();
        String previousDate = "";
        for (int i = 0; i < data.size(); i++) {
            eu.pinnoo.garbagecalendar.data.PrimitiveCollection prCol = (eu.pinnoo.garbagecalendar.data.PrimitiveCollection) data.get(i);
            Collection col = new Collection(prCol);
            if (UserData.getInstance().getAddress().getSector().equals(col.getSector())) {
                if (prCol.datum.equals(previousDate)) {
                    CollectionsData.getInstance().addToLastCollection(Collection.parseGarbageType(prCol.fractie));
                } else {
                    CollectionsData.getInstance().addCollection(col);
                }
                previousDate = prCol.datum;
            }
        }
        return 0;
    }

    @Override
    protected List<PrimitiveCollection> downloadData() {
        List<PrimitiveCollection> list = new ArrayList<PrimitiveCollection>();
        try {
            InputStream inp = Network.getStream(getURL());
            JsonReader reader = new JsonReader(new InputStreamReader(inp, LocalConstants.ENCODING));
            PrimitiveCollectionList results = new GsonBuilder().create().fromJson(reader, PrimitiveCollectionList.class);
            list.addAll((java.util.Collection<PrimitiveCollection>) results.list);
            reader.close();
        } catch (Exception e) {
            Log.d(LocalConstants.LOG, e.toString());
        } finally {
            return list;
        }
    }

    public class PrimitiveCollectionList {

        @SerializedName("IVAGO-Inzamelkalender")
        public List<PrimitiveCollection> list;
    }
}
