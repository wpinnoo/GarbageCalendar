package eu.pinnoo.garbagecalendar.util.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import eu.pinnoo.garbagecalendar.data.DataContainer;
import eu.pinnoo.garbagecalendar.data.LocalConstants;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CacheTask extends AsyncTask<DataContainer, Integer, Integer[]> {

    protected ProgressDialog dialog;
    private String msg;
    private boolean showDialog;

    public CacheTask(Context context, String msg, boolean showDialog) {
        if (showDialog) {
            dialog = new ProgressDialog(context);
        }
        this.msg = msg;
        this.showDialog = showDialog;
    }
    
    public CacheTask(Context context, String msg){
        this(context, msg, true);
    }
    
    public CacheTask(){
        this(null, null, false);
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            dialog.setMessage(msg);
            dialog.show();
            dialog.setCancelable(false);
        }
    }

    @Override
    protected Integer[] doInBackground(DataContainer... params) {
        Integer[] results = new Integer[params.length];
        for (int i = 0; i < params.length; i++) {
            Log.d(LocalConstants.LOG, "Starting with " + params[i].getClass().getName() + "...");
            long start = System.currentTimeMillis();
            results[i] = params[i].initialize();
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
        if (showDialog) {
            dialog.dismiss();
        }
    }
}
