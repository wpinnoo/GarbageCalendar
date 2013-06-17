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
    private final GarbageType[] types;
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

    public Sector getSector() {
        return sector;
    }
}
