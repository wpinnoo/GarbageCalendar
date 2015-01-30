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

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Address;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressAdapter extends ArrayAdapter<Address> implements SectionIndexer, StickyListHeadersAdapter {

    private Context context;
    private List<Address> originalValues = new ArrayList<Address>();
    private static final LinkedHashMap<String, Integer> SECTION_MAP = new LinkedHashMap<String, Integer>();
    private LayoutInflater inflater;

    public AddressAdapter(Context context, int textViewResourceId, List<Address> initObjects) {
        super(context, textViewResourceId, initObjects);
        this.context = context;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        originalValues.addAll(initObjects);
        int objectsIndex = -1;
        String previousSection = "";
        while (++objectsIndex < initObjects.size()) {
            Address a = initObjects.get(objectsIndex);
            if (!a.getStreetname().toUpperCase().substring(0, 2).equals(previousSection.toUpperCase())) {
                previousSection = a.getStreetname().substring(0, 2);
                SECTION_MAP.put(previousSection, objectsIndex);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.address_table_row, null);
        }
        v.setBackgroundColor(position % 2 == 0 ? context.getResources().getColor(R.color.table_even_row) : Color.WHITE);
        TextView addressText = (TextView) v.findViewById(R.id.toptext);
        try {
            addressText.setText(getItem(position).getStreetname() + ", " + getItem(position).getCity());
        } catch (NullPointerException e) {
            addressText.setText(context.getString(R.string.none));
        } catch (IndexOutOfBoundsException e) {
            addressText.setText("");
        }


        TextView nrText = (TextView) v.findViewById(R.id.nrtext);
        try {
            nrText.setText(getItem(position).getFormattedNr(context));
        } catch (NullPointerException e) {
            nrText.setText(context.getString(R.string.none));
        } catch (IndexOutOfBoundsException e) {
            nrText.setText("");
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
                    results.values = originalValues;
                    results.count = originalValues.size();
                } else {
                    ArrayList<Address> filtered = new ArrayList<Address>();
                    ArrayList<Address> filteredLowPriority = new ArrayList<Address>();
                    for (Address item : originalValues) {
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
                clear();
                addAll((ArrayList<Address>) filterResults.values);
            }
        };
    }

    /**
     * addAll is only available in API levels 11+, so here an alternative.
     *
     * @param list
     */
    @Override
    public void addAll(Collection<? extends Address> list) {
        Iterator<? extends Address> it = list.iterator();
        while (it.hasNext()) {
            add((Address) it.next());
        }
    }

    @Override
    public Object[] getSections() {
        return getCount() == originalValues.size() ? SECTION_MAP.keySet().toArray() : new Object[0];
    }

    @Override
    public int getPositionForSection(int section) {
        return getCount() == originalValues.size() ? SECTION_MAP.get((String) SECTION_MAP.keySet().toArray()[section]) : 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (getCount() != originalValues.size()) {
            return 0;
        }
        String[] array = (String[]) SECTION_MAP.keySet().toArray(new String[SECTION_MAP.size()]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].toUpperCase().equals(getItem(position).getStreetname().toUpperCase().substring(0, 2))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.sticky_header, parent, false);
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String name = getItem(position).getStreetname().substring(0, 2);
        String headerText = name.substring(0, 1).toUpperCase() + name.substring(1, 2).toLowerCase();
        holder.text1.setText(headerText);
        return convertView;

    }

    private class HeaderViewHolder {

        TextView text1;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getStreetname().toUpperCase().substring(0, 2).hashCode();
    }
}
