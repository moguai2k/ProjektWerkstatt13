package hm.edu.pulsebuddy.db;

import hm.edu.pulsebuddy.settings.SettingsActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

public class StorageLogic
{
  private int numOfPulseValuesTillPersist = 5;
  private int pulseValueCounter;

  private Context context;

  public StorageLogic( Context context )
  {
    this.context = context;
   
   
    
    Log.d( "Test", "TEST: " + test );
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

}
