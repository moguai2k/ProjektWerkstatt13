package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.activity.ActivityChangedListener;
import hm.edu.pulsebuddy.activity.ActivityRequester;
import hm.edu.pulsebuddy.location.LocationRequester;
import hm.edu.pulsebuddy.model.ActivityModel;
import hm.edu.pulsebuddy.model.LocationModel;
import hm.edu.pulsebuddy.model.PulseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataStorage implements OnSharedPreferenceChangeListener,
    ActivityChangedListener
{
  private final static String TAG = "db.dataStorage";

  private SQLiteDatabase database;
  private DbOpenHelper dbHelper;
  private StorageLogic storageLogic;

  private LocationRequester locationRequester;
  private ActivityRequester activityRequester;

  private List<PulseChangedListener> _listeners = new ArrayList<PulseChangedListener>();

  private DemoPulseGenerator demoGen = null;
  private Boolean demoGenIsRunning = false;

  public DataStorage( Context context )
  {
    dbHelper = new DbOpenHelper( context );

    activityRequester = new ActivityRequester( context );
    activityRequester.addActivityChangedListener( this );
    
    locationRequester = new LocationRequester( context );

    storageLogic = new StorageLogic( context );

    /* Preferences */
    SharedPreferences settings = PreferenceManager
        .getDefaultSharedPreferences( context );
    settings.registerOnSharedPreferenceChangeListener( this );
  }

  public void open() throws SQLException
  {
    database = dbHelper.getWritableDatabase();
  }

  public void close()
  {
    dbHelper.close();
  }

  /**
   * Saves the pulse value.
   * 
   * @param aPulse
   *          the pulse value.
   * @return true if the operation has been successful, false otherwise.
   */
  public synchronized Boolean savePulseValue( int aPulse )
  {
    Boolean success = false;

    /* Notify the listeners. */
    notifyPulseChanged( aPulse );

    if ( !storageLogic.pulseToBeSaved( aPulse ) )
      return false;

    ContentValues values = new ContentValues();
    values.put( DbOpenHelper.PULSE_COL_PULSE, aPulse );

    long insertId = database.insert( DbOpenHelper.PULSE_TABLE_NAME, null,
        values );

    if ( insertId != -1 )
      success = true;

    /* Do other stuff. */
    getCurrentLocation();

    return success;
  }

  private void getCurrentLocation()
  {
    LocationModel l = locationRequester.getCurrentLocation();

    ContentValues values = new ContentValues();
    values.put( DbOpenHelper.LOCATION_COL_LATITUDE, l.getLatitude() );
    values.put( DbOpenHelper.LOCATION_COL_LONGITUDE, l.getLongitude() );
    values.put( DbOpenHelper.LOCATION_COL_SPEED, l.getSpeed() );
    values.put( DbOpenHelper.LOCATION_COL_ELEVATION, l.getElevation() );

    database.insert( DbOpenHelper.LOCATION_TABLE_NAME, null, values );
  }

  public synchronized void addPulseChangedListener(
      PulseChangedListener listener )
  {
    _listeners.add( listener );
  }

  public synchronized void removePulseChangedListener(
      PulseChangedListener listener )
  {
    _listeners.remove( listener );
  }

  /**
   * Transforms a pulse database cursor to a pulse POJO.
   * 
   * @param cursor
   *          pointing to the desired pulse row.
   * @return the PulseModel object representation.
   */
  private PulseModel cursorToPulseModel( Cursor cursor )
  {
    PulseModel pulse = new PulseModel();

    pulse.setId( cursor.getLong( 0 ) );

    long millis = cursor.getLong( cursor.getColumnIndexOrThrow( "timestamp" ) );
    Date addedOn = new Date( millis );
    pulse.setDateTime( addedOn );

    pulse.setPulse( cursor.getInt( 2 ) );

    Log.d( "DB output", "Pulse: " + pulse.getPulse() + ", date: "
        + pulse.getDateTime().toString() );

    return pulse;
  }

  /**
   * Forwards the current pulse value to the listeners.
   * 
   * @param aPulse
   */
  private synchronized void notifyPulseChanged( int aPulse )
  {
    PulseModel p = new PulseModel( aPulse );
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
      while ( demoGenIsRunning )
      {
        int pulse = 50 + (int) ( Math.random() * ( ( 210 - 50 ) + 1 ) );
        publishProgress( pulse );
        try
        {
          Thread.sleep( 1000 );
        }
        catch ( InterruptedException e )
        {
          e.printStackTrace();
        }
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
    Log.d( TAG, aActivity.toString() );
  }
}
