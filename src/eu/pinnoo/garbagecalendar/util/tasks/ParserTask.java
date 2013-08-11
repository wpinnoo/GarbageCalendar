package eu.pinnoo.garbagecalendar.util.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.parsers.Parser;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class ParserTask extends AsyncTask<Parser, Integer, Integer[]> {

    protected ProgressDialog dialog;
    private Context context;
    private String msg;
    private boolean isPullToRefresh;

    public ParserTask(Context context, String msg, boolean isPullToRefresh) {
        if (!isPullToRefresh) {
            dialog = new ProgressDialog(context);
        }
        this.context = context;
        this.msg = msg;
        this.isPullToRefresh = isPullToRefresh;
    }

    public ParserTask(Context context, String msg) {
        this(context, msg, false);
    }

    @Override
    protected void onPreExecute() {
        if (!isPullToRefresh) {
            dialog.setMessage(msg);
            dialog.show();
            dialog.setCancelable(false);
        }
    }

    @Override
    protected Integer[] doInBackground(Parser... params) {
        Integer[] results = new Integer[params.length];
        for (int i = 0; i < params.length; i++) {
            Log.d(LocalConstants.LOG, "Starting with " + params[i].getClass().getName() + "...");
            long start = System.currentTimeMillis();
            results[i] = params[i].loadData(context);
            long end = System.currentTimeMillis();
            publishProgress((int) ((i / (float) params.length) * 100));
            Log.d(LocalConstants.LOG, "... done with " + params[i].getClass().getName() + ", took " + (end - start) + "ms.");
            if (isCancelled()) {
                break;
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(Integer[] result) {
        if (!isPullToRefresh) {
            dialog.dismiss();
        }
    }
}
