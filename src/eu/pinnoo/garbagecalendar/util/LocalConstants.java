package eu.pinnoo.garbagecalendar.util;

import android.content.Context;
import android.graphics.Color;
import java.text.SimpleDateFormat;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class LocalConstants {

    public static final String ENCODING = "iso-8859-1";
    public static final String CALENDAR_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Inzamelkalender.json";
    public static final String STREETS_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Stratenlijst.json";
    public static final String APARTMENTS_URL = "http://datatank.gent.be/MilieuEnNatuur/IVAGO-Appartementen.json";
    public static final String GOOGLE_MAPS_API = "http://maps.googleapis.com/maps/api/geocode/json";
    public static final String DEFAULT_SECTOR = "-00";
    public static final String NOTIF_INTENT_COL = "collection";
    
    public static final int COLOR_TABLE_EVEN_ROW = Color.rgb(240, 240, 240);
    public static final int COLOR_TABLE_ODD_ROW = Color.rgb(219, 219, 219);
    
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    
    public enum DateFormatType {
        MAIN_TABLE, WEEKDAY, SHORT_WEEKDAY, FULL 
    }
    
    public enum CacheName {
        NOTIFICATION("notif"),
        USER_APARTMENT("user_apartment"),
        USER_STREET("user_street"),
        USER_NR("user_nr"),
        USER_PC("user_pc"),
        USER_CITY("user_city"),
        USER_SECTOR("user_sector"),
        STREETS("cache_streets"),
        CALENDAR("cache_calendar"),
        APARTMENTS("cache_apartments"),
        UPDATE_STREETS("data_streets_update"),
        UPDATE_CALENDAR("data_cal_update"),
        UPDATE_APARTMENTS("data_app_update");
        
        private String s;
        
        private CacheName(String s){
            this.s = s;
        }
        
        @Override
        public String toString(){
            return s;
        }
    }
    
    public static SimpleDateFormat getDateFormatter(DateFormatType type, Context c){
        switch(type){
            case MAIN_TABLE:
                return new SimpleDateFormat("EEE, d MMMM", c.getResources().getConfiguration().locale);
            case WEEKDAY:
                return new SimpleDateFormat("EEEE", c.getResources().getConfiguration().locale); 
            case SHORT_WEEKDAY:
                return new SimpleDateFormat("EEE", c.getResources().getConfiguration().locale); 
            case FULL:
                return new SimpleDateFormat("EEEE d MMMM", c.getResources().getConfiguration().locale); 
        }
        return null;
    }
}
