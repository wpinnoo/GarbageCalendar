package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.AreaType;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum Type implements Serializable {

    REST("rest", Color.TRANSPARENT),
    GFT("gft", Color.rgb(12, 107, 53)),
    PMD("pmd", Color.BLUE),
    PK("pk", Color.GRAY),
    GLAS("glas", Color.rgb(51, 83, 153)),
    NONE("-", Color.TRANSPARENT);
    private String strValue;
    private int color;

    private Type(String strValue, int color) {
        this.strValue = strValue;
        this.color = color;
    }

    public final String shortStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_short", "string", c.getPackageName()));
    }

    public final String longStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_long", "string", c.getPackageName()));
    }

    public int getColor(AreaType type) {
        if (this == REST) {
            return type.getColor();
        } else {
            return color;
        }
    }
}
