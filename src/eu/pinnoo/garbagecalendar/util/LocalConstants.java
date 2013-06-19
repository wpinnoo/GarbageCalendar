package eu.pinnoo.garbagecalendar.util;

import android.content.Context;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    
    public static final int COLOR_TABLE_EVEN_ROW = Color.rgb(240, 240, 240);
    public static final int COLOR_TABLE_ODD_ROW = Color.rgb(219, 219, 219);
    
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    
    public enum DateFormatType {
        MAIN_TABLE, WEEKDAY, FULL
    }
    
    public static SimpleDateFormat getDateFormatter(DateFormatType type, Context c){
        switch(type){
            case MAIN_TABLE:
                return new SimpleDateFormat("EEE, d MMMM", c.getResources().getConfiguration().locale);
            case WEEKDAY:
                return new SimpleDateFormat("EEEE", c.getResources().getConfiguration().locale); 
            case FULL:
                return new SimpleDateFormat("EEEE d MMMM", c.getResources().getConfiguration().locale); 
        }
        return null;
    }
}
