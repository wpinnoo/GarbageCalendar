package eu.pinnoo.garbagecalendar.util.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import eu.pinnoo.garbagecalendar.data.DataContainer;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CacheTask extends AsyncTask<DataContainer, Integer, Integer[]> {

    protected ProgressDialog dialog;
    private String msg;

    public CacheTask(Context context, String msg) {
        dialog = new ProgressDialog(context);
        this.msg = msg;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(msg);
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected Integer[] doInBackground(DataContainer... params) {
        Integer[] results = new Integer[params.length];
        for (int i = 0; i < params.length; i++) {
            results[i] = params[i].initialize();
            publishProgress((int) ((i / (float) params.length) * 100));
            if (isCancelled()) {
                break;
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(Integer[] result) {
        dialog.dismiss();
    }
}
