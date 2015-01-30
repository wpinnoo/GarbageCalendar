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
import android.content.res.Resources;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum AreaType {

    V("V"), L("L"), CITY("city"), NONE("none");
    private final String str;

    private AreaType(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public int getColor(Context c) {
        Resources res = c.getResources();
        if (this == NONE || this == CITY) {
            return res.getColor(res.getIdentifier(str, "color", c.getPackageName()));
        } else {
            return res.getColor(res.getIdentifier(Type.REST.getStrValue() + str, "color", c.getPackageName()));
        }
    }
}
