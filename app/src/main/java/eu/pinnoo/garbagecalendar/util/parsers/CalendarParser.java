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

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.CollectionsData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.data.PrimitiveCollection;
import eu.pinnoo.garbagecalendar.data.UserData;
import eu.pinnoo.garbagecalendar.util.DateComparator;
import eu.pinnoo.garbagecalendar.util.ExceptionHandler;
import eu.pinnoo.garbagecalendar.util.Network;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CalendarParser extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.CALENDAR_URL;
    }

    @Override
    protected Result fetchData(ArrayList data) {
        try {
            ArrayList<Collection> list = new ArrayList<Collection>();
            HashMap<String, Integer> previousCollections = new HashMap<String, Integer>();
            for (int i = 0; i < data.size(); i++) {
                PrimitiveCollection prCol = (PrimitiveCollection) data.get(i);
                Collection col = new Collection(prCol);
                if (UserData.getInstance().getAddress().getSector().equals(col.getSector())) {
                    if (previousCollections.containsKey(prCol.datum)) {
                        list.get(previousCollections.get(prCol.datum)).addTypes(Collection.parseGarbageType(prCol.fractie));
                    } else {
                        list.add(col);
                        previousCollections.put(prCol.datum, list.indexOf(col));
                    }
                }
            }
            Collections.sort(list, new DateComparator());
            CollectionsData.getInstance().setCollections(list);
        } catch (NullPointerException e) {
            Log.d(LocalConstants.LOG, ExceptionHandler.getDetailedMessage(e));
            return Result.UNKNOWN_ERROR;
        } catch (ClassCastException e) {
            Log.d(LocalConstants.LOG, ExceptionHandler.getDetailedMessage(e));
            return Result.UNKNOWN_ERROR;
        }
        return Result.SUCCESSFUL;
    }

    @Override
    protected ArrayList downloadData() throws IOException {
        ArrayList<PrimitiveCollection> list = new ArrayList<PrimitiveCollection>();
        InputStream inp = Network.getStream(getURL());
        JsonReader reader = new JsonReader(new InputStreamReader(inp, LocalConstants.ENCODING));
        PrimitiveCollectionList results = new GsonBuilder().create().fromJson(reader, PrimitiveCollectionList.class);
        list.addAll((java.util.Collection<PrimitiveCollection>) results.list);
        reader.close();
        return list;
    }

    public class PrimitiveCollectionList {

        @SerializedName("IvagoOphaalkalender2014")
        public ArrayList<PrimitiveCollection> list;
    }
}
