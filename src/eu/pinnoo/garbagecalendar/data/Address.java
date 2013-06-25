package eu.pinnoo.garbagecalendar.data;

import android.content.Context;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Address implements Serializable {

    private String streetname;
    private String code;
    private int nrOddBegin;
    private int nrOddEnd;
    private int nrEvenBegin;
    private int nrEvenEnd;
    private int zipcode;
    private String city;
    private Sector sector;

    public Address() {
        streetname = "";
        code = "";
        nrOddBegin = 0;
        nrOddEnd = 0;
        nrEvenBegin = 0;
        nrEvenEnd = 0;
        zipcode = 0;
        city = "";
        sector = new Sector();
    }

    public boolean matches(String s) {
        s = s.toLowerCase();
        return streetname.toLowerCase().contains(s) || city.toLowerCase().contains(s);
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNrOddBegin() {
        return nrOddBegin;
    }

    public int getNrOddEnd() {
        return nrOddEnd;
    }

    public int getNrEvenBegin() {
        return nrEvenBegin;
    }

    public int getNrEvenEnd() {
        return nrEvenEnd;
    }

    public void setNrOddBegin(String nr) {
        this.nrOddBegin = parseNumber(nr);
    }

    public void setNrOddEnd(String nr) {
        this.nrOddEnd = parseNumber(nr);
    }

    public void setNrEvenBegin(String nr) {
        this.nrEvenBegin = parseNumber(nr);
    }

    public void setNrEvenEnd(String nr) {
        this.nrEvenEnd = parseNumber(nr);
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getFormattedNr(Context c) {
        String str = "";
        if (nrEvenBegin != -1) {
            str += nrEvenBegin;
            if (nrEvenEnd != -1 && nrEvenBegin != nrEvenEnd) {
                str += "-" + nrEvenEnd;
            }
        } else {
            if (nrEvenEnd != -1) {
                str += nrEvenEnd;
            }
        }

        if (nrOddBegin != -1) {
            if (!str.isEmpty()) {
                str += ", ";
            }
            str += nrOddBegin;
            if (nrOddEnd != -1 && nrOddBegin != nrOddEnd) {
                str += "-" + nrOddEnd;
            }
        } else {
            if (nrOddEnd != -1) {
                if (!str.isEmpty()) {
                    str += ", ";
                }
                str += nrOddEnd;
            }
        }

        if(str.isEmpty()){
            return str;
        } else {
            return (str.contains(" ") || str.contains("-") ? c.getString(R.string.nrs) : c.getString(R.string.nr)) + " " + str;
        }
    }

    private int parseNumber(String str) {
        int nr = -1;
        try {
            nr = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        } finally {
            return nr;
        }
    }
}
