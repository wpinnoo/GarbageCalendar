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
package eu.pinnoo.garbagecalendar.util.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.parsers.Parser;
import eu.pinnoo.garbagecalendar.util.parsers.Parser.Result;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class ParserTask extends AsyncTask<Parser, Integer, Result[]> {

    protected ProgressDialog dialog;
    private Context context;
    private String msg;
    private boolean showDialog;

    public ParserTask(Context context, String msg, boolean showDialog) {
        if (showDialog) {
            dialog = new ProgressDialog(context);
        }
        this.context = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public ParserTask(Context context, String msg) {
        this(context, msg, true);
    }

    public ParserTask(Context context) {
        this(context, "", false);
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
    protected Result[] doInBackground(Parser... params) {
        Result[] results = new Result[params.length];
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
    protected void onPostExecute(Result[] result) {
        if (showDialog) {
            dialog.dismiss();
        }
    }
}
