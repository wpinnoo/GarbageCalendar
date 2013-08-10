package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressData implements DataContainer {

    private static final AddressData instance = new AddressData();
    private ArrayList<Address> addresses;

    private AddressData() {
        if (addresses == null) {
            addresses = new ArrayList<Address>();
        }
    }

    public static AddressData getInstance() {
        return instance;
    }

    @Override
    public int initialize() {
        if (addresses == null || addresses.isEmpty()) {
            addresses = AddressCache.getInstance().get(LocalConstants.CacheName.ADDRESS_DATA.toString());
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isSet() {
        return addresses != null && !addresses.isEmpty();
    }

    public void resetAddresses() {
        if (isSet()) {
            addresses.clear();
        }
        AddressCache.getInstance().clear();
    }

    public void setAddresses(ArrayList<Address> list) {
        resetAddresses();
        addresses = list;
        AddressCache.getInstance().put(LocalConstants.CacheName.ADDRESS_DATA.toString(), addresses);
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
