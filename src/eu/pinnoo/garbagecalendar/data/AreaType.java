package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import android.content.res.Resources;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum AreaType implements Serializable {

    V("V"), L("L"), NONE("none");
    private final String str;

    private AreaType(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public int getColor(Context c) {
        Resources res = c.getResources();
        if (this == NONE) {
            return res.getColor(res.getIdentifier(str, "color", c.getPackageName()));
        } else {
            return res.getColor(res.getIdentifier(Type.REST.getStrValue() + str, "color", c.getPackageName()));
        }
    }
}
