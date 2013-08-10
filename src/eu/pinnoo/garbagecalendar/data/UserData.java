package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserData implements DataContainer {

    private static final UserData instance = new UserData();
    private Address address;
    private boolean isChanged = false;

    private UserData() {
    }

    public static UserData getInstance() {
        return instance;
    }

    @Override
    public int initialize() {
        if (address == null) {
            address = UserAddressCache.getInstance().get(LocalConstants.CacheName.USER_DATA.toString());
            return 0;
        } else {
            return 1;
        }
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
            UserAddressCache.getInstance().put(LocalConstants.CacheName.USER_DATA.toString(), address);
        }
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void changeCommitted() {
        isChanged = false;
    }

    public boolean needsUpdate(Context c) {
        return false;
    }
}
