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
public enum Type {

    REST("rest", false),
    GFT("gft", false),
    PMD("pmd", false),
    PK("pk", false),
    GLAS("glas", false),
    KERSTBOOM("kerst", true),
    GROF("grof", true),
    NONE("none", false);
    private String strValue;
    private boolean isExtraType;

    private Type(String strValue, boolean isExtraType) {
        this.strValue = strValue;
        this.isExtraType = isExtraType;
    }
    
    public boolean isExtraType(){
        return isExtraType;
    }

    public final String shortStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_short", "string", c.getPackageName()));
    }

    public final String longStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_long", "string", c.getPackageName()));
    }

    public final String getStrValue() {
        return strValue;
    }

    public int getColor(Context c, AreaType type) {
        if (this == REST) {
            return type.getColor(c);
        } else {
            Resources res = c.getResources();
            return res.getColor(res.getIdentifier(strValue, "color", c.getPackageName()));
        }
    }
}
