package eu.pinnoo.garbagecalendar.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum GarbageType {

    REST("rest", Color.TRANSPARENT),
    GFT("gft", Color.rgb(12, 107, 53)),
    PMD("pmd", Color.BLUE),
    PK("pk", Color.GRAY),
    GLAS("glas", Color.rgb(51, 83, 153)),
    NONE("-", Color.TRANSPARENT);
    
    private String strValue;
    private int color;

    private GarbageType(String strValue, int color) {
        this.strValue = strValue;
        this.color = color;
    }

    public final String strValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue, "string", c.getPackageName()));
    }

    public int getColor(AreaType type) {
        if (this == REST) {
            return type.getColor();
        } else {
            return color;
        }
    }
}
