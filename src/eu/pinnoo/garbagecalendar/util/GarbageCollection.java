package eu.pinnoo.garbagecalendar.util;

import java.util.Date;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class GarbageCollection {

    private final String weekCode;
    private final String day;
    private final Date date;
    private GarbageType[] types;
    private final Sector sector;

    public GarbageCollection(String weekCode, String day, Date date, GarbageType[] types, Sector sector) {
        this.weekCode = weekCode;
        this.day = day;
        this.date = date;
        this.types = types;
        this.sector = sector;
    }

    public String getWeekCode() {
        return weekCode;
    }

    public String getDay() {
        return day;
    }

    public Date getDate() {
        return date;
    }

    public GarbageType[] getTypes() {
        return types;
    }

    public void addTypes(GarbageType[] toAdd) {
        GarbageType[] newTypes = new GarbageType[toAdd.length + this.types.length];
        int i = 0;
        while (i < this.types.length) {
            newTypes[i] = this.types[i];
            i++;
        }
        while (i < this.types.length + toAdd.length) {
            newTypes[i] = toAdd[i - this.types.length];
            i++;
        }
        this.types = newTypes;
    }

    public Sector getSector() {
        return sector;
    }
}
