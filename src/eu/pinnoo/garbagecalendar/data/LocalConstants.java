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
import java.text.SimpleDateFormat;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class LocalConstants {

    public static final boolean DEBUG = false;
    public static final String ENCODING = "iso-8859-1";
    public static final String CALENDAR_URL = "http://datatank.gent.be/MilieuEnNatuur/IvagoOphaalkalender2014.json";
    public static final String STREETS_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Stratenlijst.json";
    public static final String LOG = "eu.pinnoo.garbagecalendar";

    public enum DateFormatType {

        MAIN_TABLE("EEE, d MMMM"), WEEKDAY("EEEE"), SHORT_WEEKDAY("EEE"), FULL("EEEE d MMMM"), NORMAL("dd/MM/yyyy"), REVERSE("yyyy-MM-dd"), WIDGET("EEE,\nd MMM");
        private String dateFormat;

        private DateFormatType(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public SimpleDateFormat getDateFormatter(Context c) {
            if (c == null) {
                return new SimpleDateFormat(dateFormat);
            } else {
                return new SimpleDateFormat(dateFormat, c.getResources().getConfiguration().locale);
            }
        }
    }

    public enum CacheName {

        COLLECTIONS_DATA("collectionsdata"),
        USER_DATA("userdata"),
        ADDRESS_DATA("addressdata"),
        COL_REFRESH_NEEDED("colrefreshneeded"),
        VERSION("eu.pinnoo.garbagecalendar.version");
        private String s;

        private CacheName(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
