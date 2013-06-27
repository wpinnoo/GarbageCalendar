package eu.pinnoo.garbagecalendar.data;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.util.Network;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressData implements DataContainer {

    private static final AddressData instance = new AddressData();
    private List<Address> addresses;

    private AddressData() {
        if(addresses == null){
            addresses = new ArrayList<Address>();
        }
    }

    public static AddressData getInstance() {
        return instance;
    }
    
    @Override
    public int initialize(){
        if(addresses == null || addresses.isEmpty()){
            addresses = AddressCache.getInstance().getAll();
            return 0;
        } else {
            return 1;
        }
    }
    
    public boolean isSet(){
        return addresses != null && !addresses.isEmpty();
    }

    public void resetAddresses() {
        addresses.clear();
        AddressCache.getInstance().clear();
    }

    public void addAddress(Address address) {
        addresses.add(address);
        AddressCache.getInstance().put(address.getCode(), address);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Address getLastAddress() {
        return addresses.get(addresses.size() - 1);
    }

    public boolean needsUpdate(Context c) {
        Date lastModified = Network.getLastModifiedDate(LocalConstants.STREETS_URL, c);
        if (lastModified != null) {
            long lastUpdated = c.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getLong(LocalConstants.CacheName.LAST_MOD_ADDRESSES.toString(), 0);
            if (lastUpdated < lastModified.getTime()) {
                PreferenceManager.getDefaultSharedPreferences(c)
                        .edit()
                        .putLong(LocalConstants.CacheName.LAST_MOD_ADDRESSES.toString(), lastModified.getTime())
                        .commit();
                return true;
            }
        }
        return false;
    }
}
