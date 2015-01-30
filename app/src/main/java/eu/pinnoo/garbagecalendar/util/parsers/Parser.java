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
package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import android.util.Log;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.ExceptionHandler;
import eu.pinnoo.garbagecalendar.util.Network;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Parser {

    public enum Result {

        SUCCESSFUL, EMPTY_RESPONSE, NO_INTERNET_CONNECTION, CONNECTION_FAIL, UNKNOWN_ERROR
    }

    protected abstract String getURL();

    protected abstract Result fetchData(ArrayList data);

    protected abstract ArrayList downloadData() throws IOException;

    public Result loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return Result.NO_INTERNET_CONNECTION;
        }
        ArrayList arr;
        try {
            arr = downloadData();
        } catch (IOException e) {
            Log.d(LocalConstants.LOG, ExceptionHandler.getDetailedMessage(e));
            return Result.CONNECTION_FAIL;
        }
        if (arr == null) {
            return Result.EMPTY_RESPONSE;
        }
        return fetchData(arr);
    }
}