package eu.pinnoo.garbagecalendar.util.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.util.parsers.Parser;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class ParserTask extends AsyncTask<Parser, Integer, Integer[]> {

    private ProgressDialog dialog;
    private Context context;
    private String msg;
    private ResultHandler resulthandler;

    public ParserTask(Context context, String msg, ResultHandler<Integer[]> resulthandler) {
        dialog = new ProgressDialog(context);
        this.context = context;
        this.msg = msg;
        this.resulthandler = resulthandler;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(msg);
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected Integer[] doInBackground(Parser... params) {
        Integer[] results = new Integer[params.length];
        for (int i=0; i < params.length; i++){
             results[i] = params[i].loadData(context);
             publishProgress((int) ((i / (float) params.length) * 100));
             if(isCancelled()) break;
        }
        return results;
    }
    
    @Override
    protected void onProgressUpdate(Integer... progress){
        resulthandler.onProgressUpdate(progress);
    }
    
    @Override
    protected void onPostExecute(Integer[] result) {
        dialog.dismiss();
        resulthandler.handle(result);
    }
}
