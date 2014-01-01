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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Collection implements Serializable {

    private static final long serialVersionUID = -4787102199050536373L;
    private Date date;
    private ArrayList<Type> types;
    private Sector sector;

    public Collection(PrimitiveCollection col) {
        try {
            this.date = LocalConstants.DateFormatType.REVERSE.getDateFormatter(null).parse(col.datum);
        } catch (ParseException ex) {
            this.date = new Date();
        }
        this.types = parseGarbageType(col.Fractie);
        this.sector = new Sector(col.sector);
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public String getTypesToString(Context c) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < types.size(); i++) {
            text.append(types.get(i).shortStrValue(c));
            if (i < types.size() - 2) {
                text.append(", ");
            }
            if (i == types.size() - 2) {
                text.append(", ");
                text.append(c.getString(R.string.and));
                text.append(" ");
            }
        }
        text.append(".");
        return text.toString();
    }

    public boolean hasType(Type t) {
        if (t != null) {
            for (Type type : getTypes()) {
                if (type.equals(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Sector getSector() {
        return sector;
    }

    public static ArrayList<Type> parseGarbageType(String str) {
        HashMap<String, Type> map = new HashMap<String, Type>();
        map.put("REST", Type.REST);
        map.put("GFT", Type.GFT);
        map.put("PMD", Type.PMD);
        map.put("PAPIER", Type.PK);
        map.put("GLAS", Type.GLAS);
        map.put("GROFVUIL OP AANVRAAG", Type.GROF);
        map.put("KERSTBOMEN", Type.KERSTBOOM);

        String[] splittedTypes = str.split("/");
        ArrayList<Type> results = new ArrayList<Type>();

        for (int i = 0; i < splittedTypes.length; i++) {
            if (map.containsKey(splittedTypes[i].trim())) {
                results.add(map.get(splittedTypes[i].trim()));
            } else {
                results.add(Type.NONE);
            }
        }

        return results;
    }
}
