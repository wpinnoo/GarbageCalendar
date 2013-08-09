package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import eu.pinnoo.garbagecalendar.util.Network;
import java.util.List;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Parser<T> {

    public static final int NO_INTERNET_CONNECTION = 1;

    protected abstract String getURL();

    protected abstract int fetchData(List<T> data);

    protected abstract List<T> downloadData();

    public int loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return NO_INTERNET_CONNECTION;
        }
        List<T> arr = downloadData();
        return fetchData(arr);
    }
}