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
package eu.pinnoo.garbagecalendar.ui.preferences;

import android.os.Bundle;
import android.preference.Preference;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;
import eu.pinnoo.garbagecalendar.ui.AbstractSherlockPreferenceActivity;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class PreferenceActivity extends AbstractSherlockPreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddressCache.initialize(this);
        CollectionCache.initialize(this);
        UserAddressCache.initialize(this);

        clearCachedIfRequired();

        addPreferencesFromResource(R.xml.preferences);
        updateAddressSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAddressSummary();
    }

    public void updateAddressSummary() {
        Preference pref = (Preference) findPreference("address");
        Address address = UserData.getInstance().getAddress();
        if (address == null || address.getCode() == null || address.getCode().isEmpty()) {
            pref.setSummary(getString(R.string.noAddressSet));
        } else {
            pref.setSummary(getString(R.string.locationSet) + " " + address.getStreetname() + ", " + address.getCity());
        }
    }
}
