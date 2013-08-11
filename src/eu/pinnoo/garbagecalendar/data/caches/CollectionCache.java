package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CollectionCache extends Cache<ArrayList<Collection>> {

    private static CollectionCache instance;

    private CollectionCache(File dir) {
        super(dir);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.COLLECTIONS_DATA.toString());
            instance = new CollectionCache(dir);
        }
    }

    public static CollectionCache getInstance() {
        return instance;
    }
    
    public boolean isSet(){
        return instance != null;
    }
}
