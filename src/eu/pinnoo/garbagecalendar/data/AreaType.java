package eu.pinnoo.garbagecalendar.data;

import android.graphics.Color;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum AreaType implements Serializable {

    V("V", Color.rgb(191, 201, 79)), L("L", Color.rgb(110, 110, 110)), NONE("-", Color.TRANSPARENT);
    private final String str;
    private final int color;

    private AreaType(final String str, final int color) {
        this.str = str;
        this.color = color;
    }

    @Override
    public String toString() {
        return str;
    }

    public int getColor() {
        return color;
    }
}
