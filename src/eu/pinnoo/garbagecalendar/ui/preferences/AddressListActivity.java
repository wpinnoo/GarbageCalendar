package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.data.caches.AddressCache;
import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;
import eu.pinnoo.garbagecalendar.data.caches.UserAddressCache;
import eu.pinnoo.garbagecalendar.data.util.AddressComparator;
import eu.pinnoo.garbagecalendar.ui.AbstractSherlockListActivity;
import eu.pinnoo.garbagecalendar.util.Network;
import eu.pinnoo.garbagecalendar.util.parsers.StreetsParser;
import eu.pinnoo.garbagecalendar.util.tasks.CacheTask;
import eu.pinnoo.garbagecalendar.util.tasks.ParserTask;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressListActivity extends AbstractSherlockListActivity implements SearchView.OnQueryTextListener, PullToRefreshAttacher.OnRefreshListener {

    private List<Address> list;
    private AddressAdapter adapter;
    private ListView lv;
    private boolean loading = false;
    private PullToRefreshAttacher attacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses);

        AddressCache.initialize(this);
        CollectionCache.initialize(this);
        UserAddressCache.initialize(this);

        clearCachedIfRequired();
        
        lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                submitAddress(i);
            }
        });
        attacher = PullToRefreshAttacher.get(this);
        attacher.addRefreshableView(lv, (PullToRefreshAttacher.OnRefreshListener) this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), !UserData.getInstance().isSet())
                .commit();

        if (!loading) {
            initializeCacheAndLoadStreets(false, false);
        }
    }

    private void initializeCacheAndLoadStreets(boolean force, boolean isPullToRefresh) {
        if (force || !AddressData.getInstance().isSet()) {
            if (!Network.networkAvailable(this)) {
                loading = true;
                if (isPullToRefresh) {
                    loading = false;
                    attacher.setRefreshComplete();
                    Toast.makeText(getApplicationContext(), getString(R.string.needConnectionAddress), Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.noInternetConnection))
                            .setMessage(getString(R.string.needConnectionAddress))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            loading = false;
                            attacher.setRefreshComplete();
                            finish();
                        }
                    })
                            .create().show();
                }
            } else {
                new ParserTask(this, getString(R.string.loadingStreets), !isPullToRefresh) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = true;
                    }

                    @Override
                    protected void onPostExecute(Integer[] result) {
                        super.onPostExecute(result);
                        loading = false;
                        attacher.setRefreshComplete();
                        loadStreets();
                    }
                }.execute(new StreetsParser());
            }
        } else {
            loadStreets();
        }
    }

    private void loadStreets() {
        if (!AddressData.getInstance().isSet()) {
            if (!loading) {
                new CacheTask(this, getString(R.string.loadingStreets)) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = true;
                    }

                    @Override
                    protected void onPostExecute(Integer[] result) {
                        super.onPostExecute(result);
                        loading = false;
                        fillList();
                    }
                }.execute(AddressData.getInstance());
            }
        } else {
            fillList();
        }
    }

    private void fillList() {
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

    @Override
    public void onRefreshStarted(View view) {
        initializeCacheAndLoadStreets(true, true);
    }
}
