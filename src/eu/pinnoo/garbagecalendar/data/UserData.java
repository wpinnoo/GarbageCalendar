package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.AddressCache;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserData {

    private static final UserData instance = new UserData();
    private Address address;
    
    private UserData(){
        address = AddressCache.getInstance().get(LocalConstants.CacheName.USER_DATA.toString());
    }
    
    public static UserData getInstance(){
        return instance;
    }
    
    public boolean isSet(){
        return address != null;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        AddressCache.getInstance().put(LocalConstants.CacheName.USER_DATA.toString(), address);
    }
}
