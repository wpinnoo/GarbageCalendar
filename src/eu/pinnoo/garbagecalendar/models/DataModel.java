package eu.pinnoo.garbagecalendar.models;

import android.app.Activity;
import eu.pinnoo.garbagecalendar.util.GarbageCollection;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class DataModel {

    private static final DataModel instance = new DataModel();
    private Activity container;
    private ArrayList<GarbageCollection> collections;

    private DataModel() {
        collections = new ArrayList<GarbageCollection>();
    }

    public static DataModel getInstance() {
        return instance;
    }

    public void setContainer(Activity act) {
        container = act;
    }

    public Activity getContainer() {
        return container;
    }

    public void resetCollections() {
        collections.clear();
    }

    public void addCollection(GarbageCollection col) {
        collections.add(col);
    }
    
    public ArrayList<GarbageCollection> getCollections(){
        return collections;
    }
}
