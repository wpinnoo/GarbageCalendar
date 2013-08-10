package eu.pinnoo.garbagecalendar.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.data.AreaType;
import static eu.pinnoo.garbagecalendar.data.AreaType.L;
import static eu.pinnoo.garbagecalendar.data.AreaType.V;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.Type;
import eu.pinnoo.garbagecalendar.data.UserData;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class WidgetProvider extends AppWidgetProvider {

    private final String SET_BACKGROUND_COLOR = "setBackgroundColor";
    private final String SET_BACKGROUND_RES = "setBackgroundResource";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            if (!CollectionsData.getInstance().isSet()
                    || !UserData.getInstance().isSet()) {
                return;
            }

            List<Collection> cols = CollectionsData.getInstance().getCollections();
            Iterator<Collection> it = cols.iterator();
            Calendar dayToBeShown = Calendar.getInstance();
            dayToBeShown.add(Calendar.DATE, -1);
            Collection col = null;
            while (it.hasNext()) {
                col = it.next();
                if (col.getDate().after(dayToBeShown.getTime())) {
                    break;
                }
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            if (col != null && UserData.getInstance().isSet()) {
                String date = LocalConstants.DateFormatType.WIDGET.getDateFormatter(context).format(col.getDate());
                remoteViews.setTextViewText(R.id.widget_date, date);

                AreaType currentAreaType = UserData.getInstance().getAddress().getSector().getType();
                int backgroundColor = Color.argb(0, 0, 0, 0);

                boolean hasType = col.hasType(Type.REST);
                remoteViews.setTextViewText(R.id.widget_rest, hasType ? Type.REST.shortStrValue(context) : "");
                if (hasType) {
                    switch (currentAreaType) {
                        case V:
                            remoteViews.setInt(R.id.widget_rest, SET_BACKGROUND_RES, R.drawable.widget_rest_v_activated_shape);
                            break;
                        case L:
                            remoteViews.setInt(R.id.widget_rest, SET_BACKGROUND_RES, R.drawable.widget_rest_l_activated_shape);
                    }
                } else {
                    remoteViews.setInt(R.id.widget_rest, SET_BACKGROUND_COLOR, backgroundColor);
                }

                hasType = col.hasType(Type.GFT);
                remoteViews.setTextViewText(R.id.widget_gft, hasType ? Type.GFT.shortStrValue(context) : "");
                remoteViews.setInt(R.id.widget_gft, SET_BACKGROUND_COLOR, hasType ? Type.GFT.getColor(context, currentAreaType) : backgroundColor);

                hasType = col.hasType(Type.PMD);
                remoteViews.setTextViewText(R.id.widget_pmd, hasType ? Type.PMD.shortStrValue(context) : "");
                remoteViews.setInt(R.id.widget_pmd, SET_BACKGROUND_COLOR, hasType ? Type.PMD.getColor(context, currentAreaType) : backgroundColor);

                hasType = col.hasType(Type.PK);
                remoteViews.setTextViewText(R.id.widget_pk, hasType ? Type.PK.shortStrValue(context) : "");
                remoteViews.setInt(R.id.widget_pk, SET_BACKGROUND_COLOR, hasType ? Type.PK.getColor(context, currentAreaType) : backgroundColor);

                hasType = col.hasType(Type.GLAS);
                remoteViews.setTextViewText(R.id.widget_glas, hasType ? Type.GLAS.shortStrValue(context) : "");
                if (hasType) {
                    remoteViews.setInt(R.id.widget_glas, SET_BACKGROUND_RES, R.drawable.widget_glas_activated_shape);
                } else {
                    remoteViews.setInt(R.id.widget_glas, SET_BACKGROUND_COLOR, backgroundColor);
                }
            } else {
                remoteViews.setTextViewText(R.id.widget_date, context.getString(R.string.none));
            }

            Intent intent = new Intent(context, WidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_date, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
