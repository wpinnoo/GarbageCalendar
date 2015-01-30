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

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;
import eu.pinnoo.garbagecalendar.ui.AbstractActivity;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AboutActivity extends AbstractActivity
{

    @Override
    protected String getActivityName()
    {
        return "AboutActivity";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        AddressCache.initialize(this);
        CollectionCache.initialize(this);
        UserAddressCache.initialize(this);

        clearCachedIfRequired();

        TextView versionView = (TextView) findViewById(R.id.aboutVersion);
        String version = "";
        try {
            version = " v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            Logger.getLogger(AboutActivity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            versionView.setText(getString(R.string.app_name) + version);
        }

        final SpannableString msg = new SpannableString(getString(R.string.aboutMessageAuthor));
        Linkify.addLinks(msg, Linkify.ALL);
        TextView v = (TextView) findViewById(R.id.aboutTextAuthor);
        v.setMovementMethod(LinkMovementMethod.getInstance());
        v.setText(msg);
    }
}
