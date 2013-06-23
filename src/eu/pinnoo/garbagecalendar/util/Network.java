package eu.pinnoo.garbagecalendar.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public final class Network {

    public static InputStream getStream(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(1000);
        return urlConnection.getInputStream();
    }
    
    public static boolean networkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
