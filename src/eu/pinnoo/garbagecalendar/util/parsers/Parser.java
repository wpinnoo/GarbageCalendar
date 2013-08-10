package eu.pinnoo.garbagecalendar.util.parsers;

import android.content.Context;
import eu.pinnoo.garbagecalendar.util.Network;
import java.util.ArrayList;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public abstract class Parser {

    public static final int NO_INTERNET_CONNECTION = 1;

    protected abstract String getURL();

    protected abstract int fetchData(ArrayList data);

    protected abstract ArrayList downloadData();

    public int loadData(Context c) {
        if (!Network.networkAvailable(c)) {
            return NO_INTERNET_CONNECTION;
        }
        ArrayList arr = downloadData();
        return fetchData(arr);
    }
}