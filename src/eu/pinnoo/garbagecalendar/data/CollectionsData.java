package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CollectionsData implements DataContainer {

    private static final CollectionsData instance = new CollectionsData();
    private ArrayList<Collection> collections;

    private CollectionsData() {
        if (collections == null) {
            collections = new ArrayList<Collection>();
        }
    }

    public static CollectionsData getInstance() {
        return instance;
    }

    @Override
    public int initialize() {
        if (collections == null || collections.isEmpty()) {
            collections = CollectionCache.getInstance().get(LocalConstants.CacheName.COLLECTIONS_DATA.toString());
            return 0;
        } else {
            return 1;
        }
    }

    public void resetCollections() {
        if (isSet()) {
            collections.clear();
        }
        CollectionCache.getInstance().clear();
    }

    public void setCollections(ArrayList<Collection> list) {
        resetCollections();
        collections = list;
        CollectionCache.getInstance().put(LocalConstants.CacheName.COLLECTIONS_DATA.toString(), collections);
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public boolean isSet() {
        return collections != null && !collections.isEmpty();
    }
}
