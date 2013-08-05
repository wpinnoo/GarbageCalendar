package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Address;
import java.io.File;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressCache extends Cache<Address> {

    private static AddressCache instance;

    private AddressCache(File dir) {
        super(dir);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.ADDRESS_DATA.toString());
            instance = new AddressCache(dir);
        }
    }

    public static AddressCache getInstance() {
        return instance;
    }
}
