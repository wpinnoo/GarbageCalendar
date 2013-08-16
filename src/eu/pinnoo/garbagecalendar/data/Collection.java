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
package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import eu.pinnoo.garbagecalendar.R;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Collection implements Serializable {

    private static final long serialVersionUID = -4787102199050536373L;
    private String weekCode;
    private String day;
    private Date date;
    private Type[] types;
    private Sector sector;

    public Collection(String weekCode, String day, Date date, Type[] types, Sector sector) {
        this.weekCode = weekCode;
        this.day = day;
        this.date = date;
        this.types = types;
        this.sector = sector;
    }

    public Collection(PrimitiveCollection col) {
        this.weekCode = col.week;
        this.day = col.dag;
        try {
            this.date = LocalConstants.DateFormatType.NORMAL.getDateFormatter(null).parse(col.datum);
        } catch (ParseException ex) {
            this.date = new Date();
        }
        this.types = parseGarbageType(col.fractie);
        this.sector = new Sector(col.locatie);
    }

    public Date getDate() {
        return date;
    }

    public Type[] getTypes() {
        return types;
    }

    public String getTypesToString(Context c) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            text.append(types[i].shortStrValue(c));
            if (i < types.length - 2) {
                text.append(", ");
            }
            if (i == types.length - 2) {
                text.append(", ");
                text.append(c.getString(R.string.and));
                text.append(" ");
            }
        }
        text.append(".");
        return text.toString();
    }

    public void addTypes(Type[] toAdd) {
        Type[] newTypes = new Type[toAdd.length + this.types.length];
        int i = 0;
        while (i < this.types.length) {
            newTypes[i] = this.types[i];
            i++;
        }
        while (i < this.types.length + toAdd.length) {
            newTypes[i] = toAdd[i - this.types.length];
            i++;
        }
        this.types = newTypes;
    }

    public boolean hasType(Type t) {
        for (Type type : getTypes()) {
            if (type.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public Sector getSector() {
        return sector;
    }

    public static Type[] parseGarbageType(String str) {
        HashMap<String, Type> map = new HashMap<String, Type>();
        map.put("Rest", Type.REST);
        map.put("GFT", Type.GFT);
        map.put("PMD", Type.PMD);
        map.put("Papier & karton", Type.PK);
        map.put("Glas", Type.GLAS);

        String[] types = str.split("/");
        Type[] results = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            if (map.containsKey(types[i].trim())) {
                results[i] = map.get(types[i].trim());
            }
        }
        return results;
    }
}
