package eu.pinnoo.garbagecalendar.util;

import android.graphics.Color;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum GarbageType {

    REST("Rest", Color.TRANSPARENT), GFT("GFT", Color.rgb(12, 107, 53)), PMD("PMD", Color.BLUE), PK("Papier", Color.GRAY), GLAS("Glas", Color.rgb(51, 83, 153)), NONE("-", Color.TRANSPARENT);
    private String strValue;
    private int color;

    private GarbageType(String strValue, int color) {
        this.strValue = strValue;
        this.color = color;
    }

    @Override
    public final String toString() {
        return strValue;
    }

    public int getColor(AreaType type) {
        if (this == REST) {
            return type.getColor();
        } else {
            return color;
        }
    }
}
