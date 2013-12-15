package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.activity.ActivityChangedListener;
import hm.edu.pulsebuddy.activity.ActivityRequester;
import hm.edu.pulsebuddy.data.listeners.PulseChangedListener;
import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.Pulse;
import hm.edu.pulsebuddy.data.perst.PerstStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataHandler implements OnSharedPreferenceChangeListener,
    ActivityChangedListener
{
  private final static String TAG = "data.handler";

  private DataInterface dataInterface;

  private PerstStorage perst;
  private StorageLogic storageLogic;

  /* Location interface */
  private LocationInterface locI;
  /* Activity requester */
  private ActivityRequester activityRequester;

  private List<PulseChangedListener> _listeners = new ArrayList<PulseChangedListener>();

  private DemoPulseGenerator demoGen = null;
  private Boolean demoGenIsRunning = false;

  /**
   * CTOR
   * 
   * @param context
   *          the application context.
   */
  public DataHandler( Context context )
  {
    perst = new PerstStorage( context );
    dataInterface = new DataInterface( perst );

    /* Create the location interface */
    locI = new LocationInterface( context, perst );

    activityRequester = new ActivityRequester( context );
    activityRequester.addActivityChangedListener( this );

    storageLogic = new StorageLogic( context );

    /* Preferences */
    SharedPreferences settings = PreferenceManager
        .getDefaultSharedPreferences( context );
    settings.registerOnSharedPreferenceChangeListener( this );
  }

  /**
   * Getter for the data interface.
   * 
   * @return the data interface.
   */
  public DataInterface getDataInterface()
  {
    return dataInterface;
  }

  /**
   * Saves the pulse value.
   * 
   * @param aPulse
   *          the pulse value.
   * @return true if the pulse has been saved, false otherwise.
   */
  public synchronized Boolean savePulseValue( int aPulse )
  {
    Boolean success = false;

    /* Notify the listeners. */
    notifyPulseChanged( aPulse );

    if ( !storageLogic.pulseToBeSaved( aPulse ) )
      return false;

    success = perst.addPulseValue( aPulse );

    return success;
  }

  /**
   * Add a listener to receive pulse changes.
   * 
   * @param listener
   */
  public synchronized void addPulseChangedListener(
      PulseChangedListener listener )
  {
    _listeners.add( listener );
  }

  /**
   * Remove a previously added pulse change listener.
   * 
   * @param listener
   */
  public synchronized void removePulseChangedListener(
      PulseChangedListener listener )
  {
    _listeners.remove( listener );
  }

  /**
   * Forwards the current pulse value to the listeners.
   * 
   * @param aPulse
   */
  private synchronized void notifyPulseChanged( int aPulse )
  {
    Pulse p = new Pulse( aPulse );
    Iterator<PulseChangedListener> i = _listeners.iterator();
    while ( i.hasNext() )
    {
      ( (PulseChangedListener) i.next() ).handlePulseChangedEvent( p );
    }
  }

  /**
   * Demo pulse generator.
   */
  private class DemoPulseGenerator extends AsyncTask<Void, Integer, String>
  {
    @Override
    protected String doInBackground( Void... params )
    {
      // demo and algorithm for never-stop-puslin
      int lastPulse = 50 + (int) ( Math.random() * ( ( 210 - 50 ) + 1 ) );

      while ( demoGenIsRunning )
      {
        int nextPulse = 50 + (int) ( Math.random() * ( ( 210 - 50 ) + 1 ) );

        publishProgress( lastPulse );

        int avgPulseSteps = 0;
        int currentPulse = lastPulse;

        if ( nextPulse > lastPulse )
        { // +pulse
          avgPulseSteps = ( nextPulse - lastPulse ) / 10;
        }
        else
        { // -pulse
          avgPulseSteps = ( lastPulse - nextPulse ) / 10;
        }

        for ( int i = 0; i < 9; i++ )
        {
          if ( nextPulse > lastPulse )
          { // +pulse
            currentPulse += avgPulseSteps;
          }
          else
          { // -pulse
            currentPulse -= avgPulseSteps;
          }

          publishProgress( currentPulse );

          try
          {
            Thread.sleep( 1000 );
          }
          catch ( InterruptedException e )
          {
            e.printStackTrace();
          }
        }

        lastPulse = nextPulse;
      }
      return null;
    }

    protected void onProgressUpdate( Integer... aPulse )
    {
      savePulseValue( aPulse[ 0 ] );
    }

    @Override
    protected void onPostExecute( String result )
    {
      demoGen.cancel( true );
      demoGen = null;
      /* TODO-tof: TESTING */
      perst.printStatistics();
      perst.exportXml();
    }
  }

  @Override
  public void onSharedPreferenceChanged( SharedPreferences sharedPreferences,
      String key )
  {
    if ( key.equals( "test_mode" ) )
    {
      Boolean testMode = sharedPreferences.getBoolean( key, false );
      Log.d( TAG, "Debug mode enabled " + testMode );
      if ( !testMode )
      {
        demoGenIsRunning = false;
      }
      else
      {
        demoGenIsRunning = true;
        demoGen = new DemoPulseGenerator();
        demoGen.execute();
      }
    }
  }

  @Override
  public void handleActivityChangedEvent( ActivityModel aActivity )
  {
    if ( storageLogic.activityToBeSaved( aActivity ) )
    {
      perst.addActivity( aActivity );
    }
  }
}
