package eu.pinnoo.garbagecalendar.ui;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class AbstractSherlockActivity extends SherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LocalConstants.DEBUG) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
            googleAnalytics.setAppOptOut(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void clearCachedIfRequired() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int old = prefs.getInt(LocalConstants.CacheName.VERSION.toString(), 0);
        int current = getVersionCode();
        if (old < current) {
            clearCaches();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(LocalConstants.CacheName.VERSION.toString(), current);
            editor.commit();
        }
    }

    public void clearCaches() {
        try {
            AddressCache.getInstance().clear();
            CollectionCache.getInstance().clear();
            UserAddressCache.getInstance().clear();
        } catch (NullPointerException ex) {
        }
    }

    protected int getVersionCode() {
        try {
            ComponentName componentName = new ComponentName(this, AbstractSherlockActivity.class);
            PackageInfo info = getPackageManager().getPackageInfo(componentName.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}
