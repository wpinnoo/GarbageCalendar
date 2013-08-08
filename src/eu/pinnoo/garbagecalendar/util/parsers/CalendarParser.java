package eu.pinnoo.garbagecalendar.util.parsers;

import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CalendarParser<PrimitiveCollection> extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.CALENDAR_URL;
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Inzamelkalender";
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
}
