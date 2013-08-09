package eu.pinnoo.garbagecalendar.util.parsers;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.PrimitiveCollection;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.util.Network;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CalendarParser extends Parser {

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
    protected int fetchData(ArrayList data) {
        if (data == null) {
            return 1;
        }
        ArrayList<Collection> list = new ArrayList<Collection>();
        String previousDate = "";
        for (int i = 0; i < data.size(); i++) {
            PrimitiveCollection prCol = (PrimitiveCollection) data.get(i);
            Collection col = new Collection(prCol);
            if (UserData.getInstance().getAddress().getSector().equals(col.getSector())) {
                if (prCol.datum.equals(previousDate)) {
                    list.get(list.size() - 1).addTypes(Collection.parseGarbageType(prCol.fractie));
                } else {
                    list.add(col);
                }
                previousDate = prCol.datum;
            }
        }
        CollectionsData.getInstance().setCollections(list);
        return 0;
    }

    @Override
    protected ArrayList downloadData() {
        ArrayList<PrimitiveCollection> list = new ArrayList<PrimitiveCollection>();
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
        public ArrayList<PrimitiveCollection> list;
    }
}
