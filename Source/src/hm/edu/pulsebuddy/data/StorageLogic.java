package hm.edu.pulsebuddy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

public class StorageLogic implements OnSharedPreferenceChangeListener
{
  private final static String TAG = "db.storageLogic";

  private int numOfPulseValuesTillPersist;
  private int pulseOffsetToSave;
  private int pulseValueCounter;

  private int lastPulseValue;

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
   * @return true if the pulse has to be saved, false otherwise.
   */
  public Boolean pulseToBeSaved( int aPulseValue )
  {
    /*
    if ( aPulseValue - this.lastPulseValue >= pulseOffsetToSave
        || aPulseValue - this.lastPulseValue <= -pulseOffsetToSave )
    {
      this.lastPulseValue = aPulseValue;
      return true;
    }
    */
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
  
  /**
   * Determine if an activity means that the user is moving.
   * 
   * @param type
   *          The type of activity the user is doing
   * @return true if the user seems to be moving from one location to another,
   *         otherwise false
   */
  private boolean isMoving( int type )
  {
    switch ( type )
    {
      case DetectedActivity.STILL:
      case DetectedActivity.TILTING:
      case DetectedActivity.UNKNOWN:
        return false;
      default:
        return true;
    }
  }
}
