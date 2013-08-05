package eu.pinnoo.garbagecalendar.ui.preferences;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressAdapter extends ArrayAdapter<Address> implements SectionIndexer {

    private List<Address> objects;
    private List<Address> original;
    private Context context;
    private static final LinkedHashMap<Character, Integer> SECTION_MAP = new LinkedHashMap<Character, Integer>();

    public AddressAdapter(Context context, int textViewResourceId, List<Address> initObjects) {
        super(context, textViewResourceId, initObjects);
        this.context = context;
        original = new ArrayList<Address>();
        original.addAll(initObjects);

        objects = new ArrayList<Address>();
        int sectionPosition = 0;
        int objectsIndex = 0;
        char previousSection = '\n';
        while (objectsIndex < initObjects.size()) {
            Address a = initObjects.get(objectsIndex);
            if (a.getStreetname().toUpperCase().charAt(0) != previousSection) {
                previousSection = a.getStreetname().toUpperCase().charAt(0);
                SECTION_MAP.put(previousSection, objectsIndex);
            }
            objects.add(a);
            objectsIndex++;
        }
        Object[] lijst = SECTION_MAP.keySet().toArray();
        for (int i = 0; i < lijst.length; i++) {
            Log.d("eu.pinnoo.garbagecalendar.ui.preferences",((Character)lijst[i]).charValue() + ", " + SECTION_MAP.get((Character)lijst[i]) + "(" + objects.get(SECTION_MAP.get((Character)lijst[i])).getStreetname() + ")");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.address_table_row, null);
        }
        v.setBackgroundColor(position % 2 == 0 ? LocalConstants.COLOR_TABLE_EVEN_ROW : Color.WHITE);
        TextView addressText = (TextView) v.findViewById(R.id.toptext);
        try {
            addressText.setText(objects.get(position).getStreetname() + ", " + objects.get(position).getCity());
        } catch (NullPointerException e) {
            addressText.setText(context.getString(R.string.none));
        }


        TextView nrText = (TextView) v.findViewById(R.id.nrtext);
        try {
            nrText.setText(objects.get(position).getFormattedNr(context));
        } catch (NullPointerException e) {
            nrText.setText(context.getString(R.string.none));
        }

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    results.values = original;
                    results.count = original.size();
                } else {
                    ArrayList<Address> filtered = new ArrayList<Address>();
                    ArrayList<Address> filteredLowPriority = new ArrayList<Address>();
                    for (Address item : original) {
                        int match = item.matches(charSequence.toString());
                        if (match == Address.FULL_MATCH) {
                            filtered.add(item);
                        } else if (match == Address.PARTIAL_MATCH) {
                            filteredLowPriority.add(item);
                        }
                    }
                    for (Address item : filteredLowPriority) {
                        filtered.add(item);
                    }
                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                objects.clear();
                objects.addAll((ArrayList<Address>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Object[] getSections() {
        return SECTION_MAP.keySet().toArray();
    }

    @Override
    public int getPositionForSection(int section) {
        return SECTION_MAP.get((Character) SECTION_MAP.keySet().toArray()[section]);
    }

    @Override
    public int getSectionForPosition(int position) {
        Character[] array = (Character[]) SECTION_MAP.keySet().toArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i].charValue() == getItem(position).getStreetname().toUpperCase().charAt(0)) {
                return i;
            }
        }
        return 0;
    }
}
