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
}
