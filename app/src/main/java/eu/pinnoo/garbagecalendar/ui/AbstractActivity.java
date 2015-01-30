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
package eu.pinnoo.garbagecalendar.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import eu.pinnoo.garbagecalendar.GarbageCalenderApplication;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class AbstractActivity extends Activity
{

    protected abstract String getActivityName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LocalConstants.DEBUG) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
            googleAnalytics.setAppOptOut(true);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((GarbageCalenderApplication) this.getApplication()).getTracker(GarbageCalenderApplication.TrackerName.APP_TRACKER);
        t.setScreenName(getActivityName());
        t.send(new HitBuilders.AppViewBuilder().build());
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
            ComponentName componentName = new ComponentName(this, AbstractActivity.class);
            PackageInfo info = getPackageManager().getPackageInfo(componentName.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}
