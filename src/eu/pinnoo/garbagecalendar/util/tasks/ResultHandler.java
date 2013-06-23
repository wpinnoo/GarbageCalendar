package eu.pinnoo.garbagecalendar.util.tasks;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public interface ResultHandler<T> {

    public void handle(T results);
    public void onProgressUpdate(Integer... progress);
}
