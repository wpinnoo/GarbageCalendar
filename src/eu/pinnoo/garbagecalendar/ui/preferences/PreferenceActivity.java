package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.receivers.TrashDayReceiver;
import java.util.Calendar;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class PreferenceActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LocalConstants.DEBUG) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
            googleAnalytics.setAppOptOut(true);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("notif_pref")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int i = 0;
            for (Collection col : CollectionsData.getInstance().getCollections()) {
                Intent intent = new Intent(getBaseContext(), TrashDayReceiver.class);
                intent.putExtra(LocalConstants.NOTIF_INTENT_COL, col);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i++, intent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(col.getDate());
                calendar.add(Calendar.HOUR, -4);

                if (sharedPreferences.getBoolean("notif_pref", false)) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                }
            }
        }
    }

    public class Preferences extends PreferenceFragment {

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
                pref.setSummary("No address set yet.");
            } else {
                pref.setSummary("Set on " + address.getStreetname() + ", " + address.getCity());
            }
        }
    }
}
