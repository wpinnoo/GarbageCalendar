package eu.pinnoo.garbagecalendar.data.models;

import android.app.Activity;
import eu.pinnoo.garbagecalendar.data.Collection;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class DataModel {

    private static final DataModel instance = new DataModel();
    private Activity container;
    private ArrayList<Collection> collections;

    private DataModel() {
        collections = new ArrayList<Collection>();
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

    public void addCollection(Collection col) {
        collections.add(col);
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public Collection getLastCollection() {
        return collections.get(collections.size() - 1);
    }
}
