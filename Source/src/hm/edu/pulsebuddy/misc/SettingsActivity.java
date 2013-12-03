package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity
{

  @SuppressWarnings( "deprecation" )
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    addPreferencesFromResource( R.xml.settings_general );
  }
}
