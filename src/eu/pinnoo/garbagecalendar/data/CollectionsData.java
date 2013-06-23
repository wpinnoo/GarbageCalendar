package eu.pinnoo.garbagecalendar.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CollectionsData implements Serializable{

    private static final CollectionsData instance = new CollectionsData();
    private ArrayList<Collection> collections;

    private CollectionsData() {
        collections = new ArrayList<Collection>();
    }

    public static CollectionsData getInstance() {
        return instance;
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
