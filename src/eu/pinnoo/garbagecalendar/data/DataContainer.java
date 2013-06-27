
package eu.pinnoo.garbagecalendar.data;

import android.content.Context;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public interface DataContainer {
    public int initialize();
    public boolean needsUpdate(Context c);
}
