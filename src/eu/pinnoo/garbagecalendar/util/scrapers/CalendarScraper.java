package eu.pinnoo.garbagecalendar.util.scrapers;

import eu.pinnoo.garbagecalendar.models.DataModel;
import eu.pinnoo.garbagecalendar.models.UserModel;
import eu.pinnoo.garbagecalendar.util.GarbageCollection;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Sector;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CalendarScraper extends Scraper {

    @Override
    protected URL getURL() throws MalformedURLException {
        return new URL(LocalConstants.CALENDAR_URL);
    }

    @Override
    protected String getJSONArrayName() {
        return "IVAGO-Inzamelkalender";
    }

    @Override
    protected String getCacheName() {
        return "cache_calendar";
    }

    /**
     * 
     * @param data
     * @return 0 when fetching was successful, otherwise 1 
     */
    @Override
    protected int fetchData(JSONArray data) {
        if (data == null) {
            return 1;
        }
        DataModel.getInstance().resetCollections();
        String previousDate = "";
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);
                Sector sector = new Sector(obj.getString("locatie"));
                if (UserModel.getInstance().getSector().equals(sector)) {
                    if (obj.getString("datum").equals(previousDate)) {
                        DataModel.getInstance().getLastCollection().addTypes(parseGarbageType(obj.getString("fractie")));
                    } else {
                        GarbageCollection col = new GarbageCollection(obj.getString("week"), obj.getString("dag"), LocalConstants.DATE_FORMATTER.parse(obj.getString("datum")), parseGarbageType(obj.getString("fractie")), sector);
                        DataModel.getInstance().addCollection(col);
                    }
                    previousDate = obj.getString("datum");
                }
            } catch (JSONException ex) {
                Logger.getLogger(CalendarScraper.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            } catch (NullPointerException e) {
                Logger.getLogger(CalendarScraper.class.getName()).log(Level.SEVERE, null, e);
                return 1;
            } catch (ParseException ex) {
                Logger.getLogger(CalendarScraper.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected String getSharedPrefName() {
        return "data_cal_update";
    }
}
