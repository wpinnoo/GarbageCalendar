package eu.pinnoo.garbagecalendar.view;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.util.GarbageCollection;
import eu.pinnoo.garbagecalendar.util.GarbageType;
import eu.pinnoo.garbagecalendar.util.LocalConstants;
import java.text.SimpleDateFormat;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class TableRowListener implements OnClickListener {

    private GarbageCollection col;

    public TableRowListener(GarbageCollection col) {
        this.col = col;
    }

    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View customView = li.inflate(R.layout.row_dialog, null);

        TextView textview = (TextView) customView.findViewById(R.id.rowDialogDate);
        SimpleDateFormat formatter = LocalConstants.getDateFormatter(LocalConstants.DateFormatType.FULL, v.getContext());
        textview.setText(formatter.format(col.getDate()) + ":");

        TextView typesview = (TextView) customView.findViewById(R.id.rowDialogTypes);
        GarbageType[] types = col.getTypes();
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
