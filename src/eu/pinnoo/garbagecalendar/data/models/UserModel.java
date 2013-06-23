package eu.pinnoo.garbagecalendar.data.models;

import android.app.Activity;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Sector;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class UserModel {

    private static final UserModel instance = new UserModel();
    private Activity container;
    private String streetname;
    private int nr;
    private int zipcode;
    private String city;
    private Sector sector;
    private boolean isApartment;

    private UserModel() {
        streetname = "";
        nr = 0;
        zipcode = 0;
        city = "";
        sector = new Sector();
        isApartment = false;
    }

    public void setContainer(Activity act) {
        container = act;
    }

    public Activity getContainer() {
        return container;
    }

    public static UserModel getInstance() {
        return instance;
    }

    public boolean isApartment() {
        return isApartment;
    }

    public void markAsApartment(boolean b) {
        isApartment = b;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(LocalConstants.CacheName.USER_APARTMENT.toString(), isApartment)
                .commit();
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putString(LocalConstants.CacheName.USER_STREET.toString(), streetname)
                .commit();
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putInt(LocalConstants.CacheName.USER_NR.toString(), nr)
                .commit();
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putInt(LocalConstants.CacheName.USER_PC.toString(), zipcode)
                .commit();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putString(LocalConstants.CacheName.USER_CITY.toString(), city)
                .commit();
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
        getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putString(LocalConstants.CacheName.USER_SECTOR.toString(), sector.toString())
                .commit();
    }
    
    public void restoreFromCache(){
        isApartment = container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getBoolean(LocalConstants.CacheName.USER_APARTMENT.toString(), false);
        streetname = container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getString(LocalConstants.CacheName.USER_STREET.toString(), "");
        nr = container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getInt(LocalConstants.CacheName.USER_NR.toString(), 0);
        zipcode = container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getInt(LocalConstants.CacheName.USER_PC.toString(), 0);
        city = container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getString(LocalConstants.CacheName.USER_CITY.toString(), "");
        sector = new Sector(container.getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getString(LocalConstants.CacheName.USER_SECTOR.toString(), LocalConstants.DEFAULT_SECTOR));
    }
    
    public String getFormattedAddress(){
        return getStreetname() + " " + getNr() + ", " + getZipcode() + " " + getCity();
    }
}
