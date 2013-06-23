package eu.pinnoo.garbagecalendar.data;

import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Address implements Serializable{

    private String streetname;
    private String code;
    private int nr;
    private int zipcode;
    private String city;
    private Sector sector;
    private boolean isApartment;
    
    public Address() {
        streetname = "";
        code = "";
        nr = 0;
        zipcode = 0;
        city = "";
        sector = new Sector();
        isApartment = false;
    }

    public boolean isApartment() {
        return isApartment;
    }

    public void markAsApartment(boolean b) {
        isApartment = b;
        AddressCache.getInstance().put(city, this);
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }
    
    public String getCode(){
        return code;
    }
    
    public void setCode(String code){
        this.code = code;
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
