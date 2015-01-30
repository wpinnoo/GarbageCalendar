/* 
 * Copyright 2014 Wouter Pinnoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        addresses = new ArrayList<Address>();
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
