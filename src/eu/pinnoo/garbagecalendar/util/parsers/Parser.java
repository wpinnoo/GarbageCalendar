/* 
 * Copyright 2013 Wouter Pinnoo
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
package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import eu.pinnoo.garbagecalendar.util.Network;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Parser {

    public static final int NO_INTERNET_CONNECTION = 1;

    protected abstract String getURL();

    protected abstract int fetchData(ArrayList data);

    protected abstract ArrayList downloadData();

    public int loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return NO_INTERNET_CONNECTION;
        }
        ArrayList arr = downloadData();
        return fetchData(arr);
    }
}