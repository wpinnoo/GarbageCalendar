package eu.pinnoo.garbagecalendar.ui.preferences;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.data.util.AddressComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressListActivity extends ListActivity {

    private EditText filterText;
    private List<Address> list;
    private AddressAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses);

        if (LocalConstants.DEBUG) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
            googleAnalytics.setAppOptOut(true);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);

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


        filterText = (EditText) findViewById(R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);
        final ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                UserData.getInstance().setAddress((Address) lv.getItemAtPosition(i));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onResume(){
        super.onResume();
        fillList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    public void fillList() {
        list = new ArrayList<Address>();
        list.addAll(AddressData.getInstance().getAddresses());
        Collections.sort(list, new AddressComparator());
        adapter = new AddressAdapter(this, R.layout.address_table_row, list);
        setListAdapter(adapter);
    }
}
