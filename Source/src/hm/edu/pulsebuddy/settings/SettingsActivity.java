package hm.edu.pulsebuddy.settings;

import hm.edu.pulsebuddy.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsActivity extends PreferenceFragment
{
  private static final String TAG = "settings";

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    addPreferencesFromResource( R.xml.settings_general );
  }
}
