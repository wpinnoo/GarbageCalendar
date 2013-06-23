package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import java.io.File;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserCache extends Cache<UserData>{
    
    private static UserCache instance;
    
    private UserCache(File dir){
        super(dir);
    }
    
    public static UserCache getInstance(Context context){
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.COLLECTIONS_DATA.toString());
            instance = new UserCache(dir);
        }
        return instance;
    }
}
