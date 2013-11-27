package hm.edu.pulsebuddy.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataStorage
{
  private SQLiteDatabase database;
  private DbOpenHelper dbHelper;  
  private StorageLogic storageLogic;

  public DataStorage( Context context )
  {
    dbHelper = new DbOpenHelper( context );
    storageLogic = new StorageLogic();
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
   * @param aPulse the pulse value.
   * @return
   */
  public synchronized PulseModel savePulseValue( int aPulse )
  {
    if ( ! storageLogic.pulseToBeSaved() )
      return null;
    
    ContentValues values = new ContentValues();
    values.put( DbOpenHelper.PULSE_COL_PULSE, aPulse );

    long insertId = database.insert( DbOpenHelper.PULSE_TABLE_NAME, null,
        values );

    getLocation();
    
    Cursor cursor = database.rawQuery( "SELECT _id AS _id,"
        + " (strftime('%s', timestamp) * 1000) AS timestamp," + " pulse"
        + " FROM pulse WHERE _id = '" + insertId + "'", new String[ 0 ] );

    cursor.moveToFirst();

    PulseModel pulseM = cursorToPulseModel( cursor );
    cursor.close();
    return pulseM;
  }
  
  private void getLocation()
  {
    ContentValues values = new ContentValues();
    values.put( DbOpenHelper.LOCATION_COL_LATITUDE, "" );
    values.put( DbOpenHelper.LOCATION_COL_LONGITUDE, "" );
    values.put( DbOpenHelper.LOCATION_COL_SPEED, "" );
    values.put( DbOpenHelper.LOCATION_COL_ELEVATION, "" );

    long insertId = database.insert( DbOpenHelper.PULSE_TABLE_NAME, null,
        values );

    Cursor cursor = database.rawQuery( "SELECT _id AS _id,"
        + " (strftime('%s', timestamp) * 1000) AS timestamp," + " pulse"
        + " FROM pulse WHERE _id = '" + insertId + "'", new String[ 0 ] );

    cursor.moveToFirst();
  }

  /**
   * Transforms a pulse database cursor to a POJO.
   * 
   * @param cursor pointing to the desired pulse row.
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

}
