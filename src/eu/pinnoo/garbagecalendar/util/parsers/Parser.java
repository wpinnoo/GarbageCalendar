package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import eu.pinnoo.garbagecalendar.data.Collection;
import eu.pinnoo.garbagecalendar.data.Type;
import eu.pinnoo.garbagecalendar.data.LocalConstants;
import eu.pinnoo.garbagecalendar.util.Network;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Parser<T> {

    public static final int NO_INTERNET_CONNECTION = 1;

    protected abstract String getURL();

    protected abstract String getJSONArrayName();

    protected abstract int fetchData(List<T> data);

    /**
     *
     * @param data
     * @return 0 when fetching was successful, otherwise 1
     */
    public int loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return NO_INTERNET_CONNECTION;
        }
        List<T> arr = downloadData();
        return fetchData(arr);
    }

    private List<T> downloadData() {
        List<T> list = new ArrayList<T>();
        try {
            InputStream inp = Network.getStream(getURL());
            JsonReader reader = new JsonReader(new InputStreamReader(inp, LocalConstants.ENCODING));
            reader.beginArray();
            while(reader.hasNext()){
                T element = new Gson().fromJson(reader, new TypeToken<T>(){}.getType());
                list.add(element);
            }
            reader.endArray();
            reader.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException e) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            return list;
        }
    }
}
