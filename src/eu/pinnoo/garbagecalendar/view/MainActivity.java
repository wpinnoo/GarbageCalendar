package eu.pinnoo.garbagecalendar.view;

import android.app.Activity;
import android.os.Bundle;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.models.DataModel;
import eu.pinnoo.garbagecalendar.models.UserModel;
import eu.pinnoo.garbagecalendar.util.AreaType;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Sector;
import eu.pinnoo.garbagecalendar.util.scrapers.ApartmentsScraper;
import eu.pinnoo.garbagecalendar.util.scrapers.CalendarScraper;
import eu.pinnoo.garbagecalendar.util.scrapers.StreetsScraper;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        initializeModels();
        scrapeData(false);
    }

    private void initializeModels() {
        UserModel.getInstance().setContainer(this);
        DataModel.getInstance().setContainer(this);
        
        Sector s = new Sector(UserModel.getInstance().getContainer().getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE).getString("user_sector", LocalConstants.DEFAULT_SECTOR));
        if(s.getType().equals(AreaType.NONE)){
            promptUserData();
        } else {
            UserModel.getInstance().restoreFromCache();
        }
    }

    private void promptUserData() {
        // TODO: show dialog
    }
    
    private void isApartmentAddress(){
        // TODO: notify user
    }
    
    private void scrapeData(boolean force){
        new ApartmentsScraper().loadData(force);
        if(UserModel.getInstance().isApartment()){
            isApartmentAddress();
            return;
        }
        
        if(force || UserModel.getInstance().getSector().toString().equals(LocalConstants.DEFAULT_SECTOR)){
            new StreetsScraper().loadData(force);
        }
        
        new CalendarScraper().loadData(force);
    }
}
