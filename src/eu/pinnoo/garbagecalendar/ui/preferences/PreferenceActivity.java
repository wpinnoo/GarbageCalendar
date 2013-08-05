package eu.pinnoo.garbagecalendar.ui.preferences;

import android.os.Bundle;
import android.preference.Preference;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.ui.AbstractSherlockPreferenceActivity;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class PreferenceActivity extends AbstractSherlockPreferenceActivity {

    static {
        SHOW_HOME_BUTTON = true;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
