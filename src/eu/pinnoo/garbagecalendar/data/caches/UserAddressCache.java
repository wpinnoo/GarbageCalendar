package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Address;
import java.io.File;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserAddressCache extends Cache<Address> {

    private static UserAddressCache instance;

    private UserAddressCache(File dir) {
        super(dir);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.USER_ADDRESS_DATA.toString());
            instance = new UserAddressCache(dir);
        }
    }

    public static UserAddressCache getInstance() {
        return instance;
    }
}
