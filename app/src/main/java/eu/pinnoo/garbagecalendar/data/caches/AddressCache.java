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
package eu.pinnoo.garbagecalendar.data.caches;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.LocalConstants;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressCache extends Cache<ArrayList<Address>> {

    private static volatile AddressCache instance;

    private AddressCache(File dir) {
        super(dir);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            File dir = new File(context.getCacheDir(), LocalConstants.CacheName.ADDRESS_DATA.toString());
            instance = new AddressCache(dir);
        }
    }

    public static AddressCache getInstance() {
        return instance;
    }

    public boolean isSet() {
        return instance != null;
    }
}
