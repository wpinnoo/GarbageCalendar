package eu.pinnoo.garbagecalendar.data;

import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserData implements Serializable{

    private String streetname;
    private int nr;
    private int zipcode;
    private String city;
    private Sector sector;
    private boolean isApartment;
    
    private static final UserData instance = new UserData();
    
    private UserData() {
        streetname = "";
        nr = 0;
        zipcode = 0;
        city = "";
        sector = new Sector();
        isApartment = false;
    }
    
    public static UserData getInstance(){
        return instance;
    }

    public boolean isApartment() {
        return isApartment;
    }

    public void markAsApartment(boolean b) {
        isApartment = b;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
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

    public String getFormattedAddress() {
        return getStreetname() + " " + getNr() + ", " + getZipcode() + " " + getCity();
    }
}
