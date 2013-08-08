package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import android.content.res.Resources;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum Type implements Serializable {

    REST("rest"),
    GFT("gft"),
    PMD("pmd"),
    PK("pk"),
    GLAS("glas"),
    NONE("none");
    private String strValue;

    private Type(String strValue) {
        this.strValue = strValue;
    }

    public final String shortStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_short", "string", c.getPackageName()));
    }

    public final String longStrValue(Context c) {
        Resources res = c.getResources();
        return res.getString(res.getIdentifier(strValue + "_long", "string", c.getPackageName()));
    }

    public final String getStrValue() {
        return strValue;
    }

    public int getColor(Context c, AreaType type) {
        if (this == REST) {
            return type.getColor(c);
        } else {
            Resources res = c.getResources();
            return res.getColor(res.getIdentifier(strValue, "color", c.getPackageName()));
        }
    }
}
