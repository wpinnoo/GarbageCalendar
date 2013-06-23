package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import java.io.File;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CollectionCache extends Cache<CollectionsData> {

    private static CollectionCache instance;

    private CollectionCache(File dir) {
        super(dir);
    }

    public static CollectionCache getInstance(Context context) {
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.COLLECTIONS_DATA.toString());
            instance = new CollectionCache(dir);
        }
        return instance;
    }
}
