package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import android.graphics.Color;
import java.text.SimpleDateFormat;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class LocalConstants {

    public static final boolean DEBUG = true;
    public static final String ENCODING = "iso-8859-1";
    public static final String CALENDAR_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Inzamelkalender.json";
    public static final String STREETS_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Stratenlijst.json";
    public static final String GOOGLE_MAPS_API = "http://maps.googleapis.com/maps/api/geocode/json";
    public static final String DEFAULT_SECTOR = "-00";
    public static final String NOTIF_INTENT_COL = "collection";
    public static final String LOG = "eu.pinnoo.garbagecalendar";
    public static final int COLOR_TABLE_EVEN_ROW = Color.rgb(240, 240, 240);
    public static final int COLOR_TABLE_ODD_ROW = Color.rgb(219, 219, 219);
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public enum DateFormatType {

        MAIN_TABLE("EEE, d MMMM"), WEEKDAY("EEEE"), SHORT_WEEKDAY("EEE"), FULL("EEEE d MMMM");
        
        private String dateFormat;
        private DateFormatType(String dateFormat){
            this.dateFormat = dateFormat;
        }
        
        public SimpleDateFormat getDateFormatter(Context c){
            return new SimpleDateFormat(dateFormat, c.getResources().getConfiguration().locale);
        }
    }

    public enum CacheName {

        COLLECTIONS_DATA("collectionsdata"),
        USER_DATA("userdata"),
        ADDRESS_DATA("addressdata"),
        LAST_MOD_COL("lastmodcol"),
        LAST_MOD_ADDRESSES("lastmodaddresses"),
        COL_REFRESH_NEEDED("colrefreshneeded");
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
