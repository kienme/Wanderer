package rustybus.wanderer;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

//import greenapple.wanderer.R;


public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.prefs_screen, false);
        addPreferencesFromResource(R.xml.prefs_screen);
    }

}
