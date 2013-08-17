/* 
 * Copyright 2013 Wouter Pinnoo
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
package eu.pinnoo.garbagecalendar.ui;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import java.text.SimpleDateFormat;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class TableRowListener implements OnClickListener {

    private Collection col;

    public TableRowListener(Collection col) {
        this.col = col;
    }

    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View customView = li.inflate(R.layout.col_row_dialog, null);

        TextView textview = (TextView) customView.findViewById(R.id.rowDialogDate);
        SimpleDateFormat formatter = LocalConstants.DateFormatType.FULL.getDateFormatter(v.getContext());
        textview.setText(formatter.format(col.getDate()) + ":");

        TextView typesview = (TextView) customView.findViewById(R.id.rowDialogTypes);
        typesview.setText(col.getTypesToString(v.getContext()));

        new AlertDialog.Builder(v.getContext())
                .setView(customView)
                .setCancelable(true)
                .create().show();
    }
}
