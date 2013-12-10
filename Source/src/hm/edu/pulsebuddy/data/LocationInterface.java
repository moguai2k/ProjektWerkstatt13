package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.data.perst.LocationModel;
import hm.edu.pulsebuddy.data.perst.PerstStorage;
import hm.edu.pulsebuddy.location.LocationRequester;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationInterface implements OnSharedPreferenceChangeListener
{
  private static final String TAG = "data.location";

  private static final int EXECUTION_DELAY = 10000;

  /* The application context */
  private Context context;

  private LocationRequester locationRequester;

  /* The perst storage interace */
  private PerstStorage storageInterface;

  /* The location timer */
  private Timer locationTimer;
  private TimerTask locationTimerTask;

  private Boolean isRunning;

  public LocationInterface( Context context, PerstStorage aStorage )
  {
    this.context = context;
    this.storageInterface = aStorage;

    this.locationTimer = new Timer( "Location timer" );
    this.locationTimerTask = new LocationFetcher();
    this.isRunning = false;

    this.locationRequester = new LocationRequester( this.context );
    
    /* Preferences */
    SharedPreferences settings = PreferenceManager
        .getDefaultSharedPreferences( context );
    settings.registerOnSharedPreferenceChangeListener( this );
  }

  public Boolean startLocationFetcher()
  {
    if ( !this.isRunning )
    {
      this.locationTimer = new Timer( "Location timer" );
      this.isRunning = true;
      locationTimer.schedule( locationTimerTask, EXECUTION_DELAY,
          EXECUTION_DELAY );
      return true;
    }
    else
    {
      Log.e( TAG, "Fetcher already running" );
      return false;
    }
  }

  public Boolean stopLocationFetcher()
  {
    if ( this.isRunning )
    {
      locationTimer.cancel();
      this.isRunning = false;
      return true;
    }
    else
    {
      Log.e( TAG, "Fetcher ist not running" );
      return false;
    }
  }

  private class LocationFetcher extends TimerTask
  {
    @Override
    public void run()
    {
      LocationModel l = locationRequester.getCurrentLocation();

      if ( l != null )
      {
        storageInterface.addLocation( l );
      }
    }
  }

  @Override
  public void onSharedPreferenceChanged( SharedPreferences sharedPreferences,
      String key )
  {
    if ( key.equals( "location_fetcher" ) )
    {
      Boolean isRunning = sharedPreferences.getBoolean( key, false );
      Log.d( TAG, "Location fetcher enabled " + isRunning );
      if ( !isRunning )
      {
        stopLocationFetcher();
      }
      else
      {
        startLocationFetcher();
      }
    }

  }

}
