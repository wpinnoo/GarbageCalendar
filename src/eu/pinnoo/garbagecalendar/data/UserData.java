package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserData {

    private static final UserData instance = new UserData();
    private Address address;
    private boolean isChanged = false;

    private UserData() {
        address = AddressCache.getInstance().get(LocalConstants.CacheName.USER_DATA.toString());
    }

    public static UserData getInstance() {
        return instance;
    }

    public boolean isSet() {
        return address != null;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (this.address != address) {
            this.address = address;
            this.isChanged = true;
            AddressCache.getInstance().put(LocalConstants.CacheName.USER_DATA.toString(), address);
        }
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void changeCommitted() {
        isChanged = false;
    }
}
