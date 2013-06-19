package eu.pinnoo.garbagecalendar.util;

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
    
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATE_FORMATTER_MAIN_TABLE = new SimpleDateFormat("EEE, d MMM");
    
    public static final int COLOR_TABLE_EVEN_ROW = Color.rgb(240, 240, 240);
    public static final int COLOR_TABLE_ODD_ROW = Color.rgb(219, 219, 219);
}
