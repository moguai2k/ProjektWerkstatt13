package hm.edu.pulsebuddy.db;

import hm.edu.pulsebuddy.location.LocationRequester;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataStorage
{
  private final static String TAG = "db.dataStorage";

  private SQLiteDatabase database;
  private DbOpenHelper dbHelper;
  private StorageLogic storageLogic;

  private LocationRequester locationRequester;

  private List<PulseChangedListener> _listeners = new ArrayList<PulseChangedListener>();

  public DataStorage( Context context )
  {
    dbHelper = new DbOpenHelper( context );
    locationRequester = new LocationRequester( context );
    storageLogic = new StorageLogic( context );
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
    /* Notify the listeners. */
    notifyPulseChanged( aPulse );

    if ( !storageLogic.pulseToBeSaved() )
      return false;

    Log.d( TAG, "Pulse: " + aPulse );

    /* ContentValues values = new ContentValues(); values.put(
     * DbOpenHelper.PULSE_COL_PULSE, aPulse );
     * 
     * long insertId = database.insert( DbOpenHelper.PULSE_TABLE_NAME, null,
     * values ); */
    
    
    getCurrentLocation();

    return true;

    /* Cursor cursor = database.rawQuery( "SELECT _id AS _id," +
     * " (strftime('%s', timestamp) * 1000) AS timestamp," + " pulse" +
     * " FROM pulse WHERE _id = '" + insertId + "'", new String[ 0 ] );
     * 
     * cursor.moveToFirst();
     * 
     * PulseModel pulseM = cursorToPulseModel( cursor ); cursor.close(); return
     * pulseM; */
  }

  private void getCurrentLocation()
  {
    LocationModel l = locationRequester.getCurrentLocation();

    /* ContentValues values = new ContentValues(); values.put(
     * DbOpenHelper.LOCATION_COL_LATITUDE, "" ); values.put(
     * DbOpenHelper.LOCATION_COL_LONGITUDE, "" ); values.put(
     * DbOpenHelper.LOCATION_COL_SPEED, "" ); values.put(
     * DbOpenHelper.LOCATION_COL_ELEVATION, "" );
     * 
     * long insertId = database.insert( DbOpenHelper.PULSE_TABLE_NAME, null,
     * values );
     * 
     * Cursor cursor = database.rawQuery( "SELECT _id AS _id," +
     * " (strftime('%s', timestamp) * 1000) AS timestamp," + " pulse" +
     * " FROM pulse WHERE _id = '" + insertId + "'", new String[ 0 ] );
     * 
     * cursor.moveToFirst(); */
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
   * Transforms a pulse database cursor to a POJO.
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

  private synchronized void notifyPulseChanged( int aPulse )
  {
    PulseModel p = new PulseModel( aPulse );
    Iterator<PulseChangedListener> i = _listeners.iterator();
    while ( i.hasNext() )
    {
      ( (PulseChangedListener) i.next() ).handlePulseChangedEvent( p );
    }
  }

}
