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
import eu.pinnoo.garbagecalendar.R;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Address implements Serializable {

    private static final long serialVersionUID = -727877370981133226L;
    private String streetname;
    private String code;
    private int nrOddBegin;
    private int nrOddEnd;
    private int nrEvenBegin;
    private int nrEvenEnd;
    private int zipcode;
    private String city;
    private Sector sector;
    public static final int NO_MATCH = 0;
    public static final int PARTIAL_MATCH = 1;
    public static final int FULL_MATCH = 2;

    public Address(PrimitiveAddress addr) {
        streetname = addr.straatnaam;
        code = addr.straatcode;
        nrOddBegin = parseNumber(addr.oneven_van);
        nrOddEnd = parseNumber(addr.oneven_tot);
        nrEvenBegin = parseNumber(addr.even_van);
        nrEvenEnd = parseNumber(addr.even_tot);
        zipcode = parseNumber(addr.postcode);
        city = addr.gemeente;
        sector = new Sector(addr.sector);
    }

    public int matches(String s) {
        s = s.toLowerCase();
        if (streetname.toLowerCase().startsWith(s)) {
            return FULL_MATCH;
        }
        if (streetname.toLowerCase().contains(s) || city.toLowerCase().contains(s)) {
            return PARTIAL_MATCH;
        }
        return NO_MATCH;
    }

    public String getStreetname() {
        return streetname;
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Sector getSector() {
        return sector;
    }

    public String getFormattedNr(Context c) {
        String str = "";
        if (nrEvenBegin != -1) {
            str += nrEvenBegin;
            if (nrEvenEnd != -1 && nrEvenBegin != nrEvenEnd) {
                str += "-" + nrEvenEnd;
            }
        } else {
            if (nrEvenEnd != -1) {
                str += nrEvenEnd;
            }
        }

        if (nrOddBegin != -1) {
            if (!str.isEmpty()) {
                str += ", ";
            }
            str += nrOddBegin;
            if (nrOddEnd != -1 && nrOddBegin != nrOddEnd) {
                str += "-" + nrOddEnd;
            }
        } else {
            if (nrOddEnd != -1) {
                if (!str.isEmpty()) {
                    str += ", ";
                }
                str += nrOddEnd;
            }
        }

        if (str.isEmpty()) {
            return str;
        } else {
            return (str.contains(" ") || str.contains("-") ? c.getString(R.string.nrs) : c.getString(R.string.nr)) + " " + str;
        }
    }

    private int parseNumber(String str) {
        int nr = -1;
        try {
            nr = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        } finally {
            return nr;
        }
    }
}
