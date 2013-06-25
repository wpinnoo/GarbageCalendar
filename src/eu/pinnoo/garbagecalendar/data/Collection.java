package eu.pinnoo.garbagecalendar.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Collection implements Serializable {

    private final String weekCode;
    private final String day;
    private final Date date;
    private Type[] types;
    private final Sector sector;

    public Collection(String weekCode, String day, Date date, Type[] types, Sector sector) {
        this.weekCode = weekCode;
        this.day = day;
        this.date = date;
        this.types = types;
        this.sector = sector;
    }

    public String getWeekCode() {
        return weekCode;
    }

    public String getCollectionCode() {
        return getWeekCode() + getSector().getCode();
    }

    public String getDay() {
        return day;
    }

    public Date getDate() {
        return date;
    }

    public Type[] getTypes() {
        return types;
    }

    public void addTypes(Type[] toAdd) {
        Type[] newTypes = new Type[toAdd.length + this.types.length];
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

    public boolean hasType(Type t) {
        for (Type type : getTypes()) {
            if (type.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public Sector getSector() {
        return sector;
    }
}
