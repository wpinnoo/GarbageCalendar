package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressData implements Serializable{

    private static final AddressData instance = new AddressData();
    private List<Address> addresses;

    private AddressData() {
        addresses = AddressCache.getInstance().getAll();
        if(addresses == null){
            addresses = new ArrayList<Address>();
        }
    }

    public static AddressData getInstance() {
        return instance;
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
}
