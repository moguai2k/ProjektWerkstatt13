package hm.edu.pulsebuddy.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class StorageLogic
{
  private final static String TAG = "db.storageLogic";
  
  /* TODO-tof: Move this to the settings. */
  private int numOfPulseValuesTillPersist = 5;
  private int pulseValueCounter;

  private Context context;

  public StorageLogic( Context context )
  {
    this.context = context;
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

}
