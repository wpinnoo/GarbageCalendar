/* 
 * Copyright 2014 Wouter Pinnoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
import eu.pinnoo.garbagecalendar.ui.widget.WidgetProvider;
import eu.pinnoo.garbagecalendar.util.parsers.Parser.Result;
import static eu.pinnoo.garbagecalendar.util.parsers.Parser.Result.NO_INTERNET_CONNECTION;
import eu.pinnoo.garbagecalendar.util.parsers.StreetsParser;
import eu.pinnoo.garbagecalendar.util.tasks.CacheTask;
import eu.pinnoo.garbagecalendar.util.tasks.ParserTask;
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
    private volatile boolean loading = false;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), !UserData.getInstance().isSet())
                .commit();

        if (!loading) {
            initializeCacheAndLoadStreets(false, true);
        }
    }

    private void initializeCacheAndLoadStreets(boolean force, final boolean requiredRefresh) {
        if (force || !AddressData.getInstance().isSet()) {
            new ParserTask(this, getString(R.string.loadingStreets)) {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = true;
                }

                @Override
                protected void onPostExecute(Result[] result) {
                    super.onPostExecute(result);
                    switch (result[0]) {
                        case EMPTY_RESPONSE:
                        case CONNECTION_FAIL:
                            Toast.makeText(getApplicationContext(), getString(R.string.errorDownload), Toast.LENGTH_SHORT).show();
                            break;
                        case UNKNOWN_ERROR:
                            Toast.makeText(getApplicationContext(), getString(R.string.unknownError), Toast.LENGTH_SHORT).show();
                            break;
                        case NO_INTERNET_CONNECTION:
                            if (!requiredRefresh) {
                                Toast.makeText(getApplicationContext(), getString(R.string.needConnectionAddress), Toast.LENGTH_SHORT).show();
                            } else {
                                new AlertDialog.Builder(AddressListActivity.this)
                                        .setTitle(getString(R.string.noInternetConnection))
                                        .setMessage(getString(R.string.needConnectionAddress))
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        loading = false;
                                        finish();
                                    }
                                }).create().show();
                            }
                            break;
                        case SUCCESSFUL:
                            loadStreets();
                            break;
                    }
                    loading = false;
                }
            }.execute(new StreetsParser());
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
        updateAllWidgets();
        finish();
    }

    private void updateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        if (appWidgetIds.length > 0) {
            new WidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                initializeCacheAndLoadStreets(true, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
