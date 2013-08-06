package eu.pinnoo.garbagecalendar.ui;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.Type;
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
        View customView = li.inflate(R.layout.row_dialog, null);

        TextView textview = (TextView) customView.findViewById(R.id.rowDialogDate);
        SimpleDateFormat formatter = LocalConstants.DateFormatType.FULL.getDateFormatter(v.getContext());
        textview.setText(formatter.format(col.getDate()) + ":");

        TextView typesview = (TextView) customView.findViewById(R.id.rowDialogTypes);
        Type[] types = col.getTypes();
        String text = "";
        for (int i = 0; i < types.length; i++) {
            text += types[i].longStrValue(v.getContext());
            if (i < types.length - 2) {
                text += ", ";
            }
            if (i == types.length - 2) {
                text += ", " + v.getContext().getString(R.string.and) + " ";
            }
        }
        text += ".";
        typesview.setText(text);

        AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
        b.setView(customView);
        b.setCancelable(true);
        b.create().show();
    }
}
