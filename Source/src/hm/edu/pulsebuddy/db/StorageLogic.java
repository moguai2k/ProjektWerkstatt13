package hm.edu.pulsebuddy.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class StorageLogic implements OnSharedPreferenceChangeListener
{
  private final static String TAG = "db.storageLogic";

  /* TODO-tof: Move this to the settings. */
  private int numOfPulseValuesTillPersist = 5;
  private int pulseValueCounter;

  private Context context;

  public StorageLogic( Context context )
  {
    this.context = context;

    SharedPreferences settings = PreferenceManager
        .getDefaultSharedPreferences( context );
    Log.d( TAG, settings.getString( "prefPulseSave", "5" ) );
    settings.registerOnSharedPreferenceChangeListener( this );
  }

  public Boolean pulseToBeSaved()
  {
    if ( this.pulseValueCounter < numOfPulseValuesTillPersist )
    {
      this.pulseValueCounter++;
      return false;
    }
    else
    {
      this.pulseValueCounter = 0;
      return true;
    }
  }

  public Boolean locationToBeSaved()
  {
    return true;
  }

  @Override
  public void onSharedPreferenceChanged( SharedPreferences sharedPreferences,
      String key )
  {
    if ( key.equals( "prefPulseSave" ) )
    {
      Log.d( TAG, "Changing number of pulse values till save "
          + sharedPreferences.getString( key, "" ) );
      this.numOfPulseValuesTillPersist = Integer.parseInt( sharedPreferences
          .getString( key, "" ) );
    }
  }
}
