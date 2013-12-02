package hm.edu.pulsebuddy.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class StorageLogic implements OnSharedPreferenceChangeListener
{
  private final static String TAG = "db.storageLogic";
  
  private int numOfPulseValuesTillPersist;
  private int pulseValueCounter;

  private Context context;

  public StorageLogic( Context context )
  {
    this.context = context;
    
    /* Preferences */
    SharedPreferences settings = PreferenceManager
        .getDefaultSharedPreferences( this.context );
    numOfPulseValuesTillPersist = Integer.parseInt( settings.getString(
        "prefPulseSave", "5" ) );
    settings.registerOnSharedPreferenceChangeListener( this );
  }

  /**
   * 
   * @return true if the pulse has to be saved, fals otherwise.
   */
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
