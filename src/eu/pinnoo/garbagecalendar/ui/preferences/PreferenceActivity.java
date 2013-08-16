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
