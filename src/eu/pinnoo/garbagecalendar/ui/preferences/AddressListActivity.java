package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
public class AddressListActivity extends AbstractSherlockListActivity {

    private EditText filterText;
    private List<Address> list;
    private AddressAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses);

        final TextWatcher filterTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                adapter.getFilter().filter(s);
            }
        };

        final ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                        .edit()
                        .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), true)
                        .commit();
                UserData.getInstance().setAddress((Address) lv.getItemAtPosition(i));
                finish();
            }
        });

        filterText = (EditText) findViewById(R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);
        filterText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (!event.isShiftPressed()
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (lv.getChildCount() == 1) {
                        getSharedPreferences("PREFERENCE", Activity.MODE_PRIVATE)
                                .edit()
                                .putBoolean(LocalConstants.CacheName.COL_REFRESH_NEEDED.toString(), true)
                                .commit();
                        UserData.getInstance().setAddress((Address) lv.getItemAtPosition(0));
                        AddressListActivity.this.finish();
                        return true;
                    }
                }
                return false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.address_list_menu, menu);
        
        MenuItem item = menu.findItem(R.id.address_search);
        SearchView searchView = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        
        return super.onCreateOptionsMenu(menu);
    }
}
