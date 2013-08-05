package eu.pinnoo.garbagecalendar.ui.preferences;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import eu.pinnoo.garbagecalendar.R;
import eu.pinnoo.garbagecalendar.ui.AbstractSherlockActivity;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AboutActivity extends AbstractSherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView versionView = (TextView) findViewById(R.id.aboutVersion);
        String version = "";
        try {
            version = " v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            Logger.getLogger(AboutActivity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            versionView.setText(getString(R.string.app_name) + version);
        }

        final SpannableString msg = new SpannableString(getString(R.string.aboutMessageAuthor));
        Linkify.addLinks(msg, Linkify.ALL);
        TextView v = (TextView) findViewById(R.id.aboutTextAuthor);
        v.setMovementMethod(LinkMovementMethod.getInstance());
        v.setText(msg);
    }
}
