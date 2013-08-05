package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.data.util.AddressComparator;
import eu.pinnoo.garbagecalendar.ui.AbstractSherlockListActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressListActivity extends AbstractSherlockListActivity implements SearchView.OnQueryTextListener {

    private List<Address> list;
    private AddressAdapter adapter;
    private ListView lv;

    static {
        SHOW_HOME_BUTTON = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses);

        lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                submitAddress(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserData.getInstance().isSet()) {
            getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                    .edit()
                    .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), false)
                    .commit();
        }
        fillList();
    }

    public void fillList() {
        list = new ArrayList<Address>();
        list.addAll(AddressData.getInstance().getAddresses());
        Collections.sort(list, new AddressComparator());
        adapter = new AddressAdapter(this, R.layout.address_table_row, list);
        setListAdapter(adapter);
    }

    public void submitAddress(int position) {
        getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), true)
                .commit();
        UserData.getInstance().setAddress((Address) getListView().getItemAtPosition(position));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.address_list_menu, menu);

        MenuItem item = menu.findItem(R.id.address_search);
        SearchView searchView = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (lv.getChildCount() == 1) {
            submitAddress(0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }
}
