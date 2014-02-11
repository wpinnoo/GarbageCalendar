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
import eu.pinnoo.garbagecalendar.data.Address;
import eu.pinnoo.garbagecalendar.data.AddressData;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Network;
import eu.pinnoo.garbagecalendar.data.PrimitiveAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class StreetsParser extends Parser {

    @Override
    protected String getURL() {
        return LocalConstants.STREETS_URL;
    }

    @Override
    protected Result fetchData(ArrayList data) {
        try {
            ArrayList<Address> list = new ArrayList<Address>();
            for (int i = 0; i < data.size(); i++) {
                PrimitiveAddress prAddr = (PrimitiveAddress) data.get(i);
                list.add(new Address(prAddr));
            }
            AddressData.getInstance().setAddresses(list);
        } catch (NullPointerException e) {
            Log.d(LocalConstants.LOG, e.getMessage());
            return Result.UNKNOWN_ERROR;
        } catch (ClassCastException e) {
            Log.d(LocalConstants.LOG, e.getMessage());
            return Result.UNKNOWN_ERROR;
        }
        return Result.SUCCESSFUL;
    }

    @Override
    protected ArrayList downloadData() throws IOException {
        ArrayList<PrimitiveAddress> list = new ArrayList<PrimitiveAddress>();
        InputStream inp = Network.getStream(getURL());
        JsonReader reader = new JsonReader(new InputStreamReader(inp, LocalConstants.ENCODING));
        PrimitiveAddressList results = new GsonBuilder().create().fromJson(reader, PrimitiveAddressList.class);
        list.addAll((Collection<PrimitiveAddress>) results.list);
        reader.close();
        return list;
    }

    public class PrimitiveAddressList {

        @SerializedName("IVAGO-Stratenlijst")
        public ArrayList<PrimitiveAddress> list;
    }
}
