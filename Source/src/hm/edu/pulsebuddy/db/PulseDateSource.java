package hm.edu.pulsebuddy.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PulseDateSource
{ 
  private SQLiteDatabase database;
  private DbOpenHelper dbHelper;
  private String[] allColumns = { DbOpenHelper.COL_ID,
      DbOpenHelper.COL_ADDED_ON, DbOpenHelper.COL_PULSE };

  public PulseDateSource( Context context )
  {
    dbHelper = new DbOpenHelper( context );
  }

  public void open() throws SQLException
  {
    database = dbHelper.getWritableDatabase();
  }

  public void close()
  {
    dbHelper.close();
  }

  public PulseModel createPulse( int pulse )
  {
    ContentValues values = new ContentValues();
    values.put( DbOpenHelper.COL_PULSE, pulse );

    long insertId = database.insert( DbOpenHelper.TABLE_NAME, null, values );

    Cursor cursor = database.rawQuery( "SELECT _id AS _id,"
        + " (strftime('%s', added_on) * 1000) AS added_on," + " pulse"
        + " FROM pulse WHERE _id = '" + insertId + "'", new String[ 0 ] );

    cursor.moveToFirst();

    PulseModel pulseM = cursorToPulseModel( cursor );
    cursor.close();
    return pulseM;
  }

  private PulseModel cursorToPulseModel( Cursor cursor )
  {
    PulseModel pulse = new PulseModel();

    pulse.setId( cursor.getLong( 0 ) );

    long millis = cursor.getLong( cursor.getColumnIndexOrThrow( "added_on" ) );
    Date addedOn = new Date( millis );
    pulse.setDateTime( addedOn );

    pulse.setPulse( cursor.getInt( 2 ) );

    Log.d( "DB output", "Pulse: " + pulse.getPulse() + ", date: "
        + pulse.getDateTime().toString() );

    return pulse;
  }

}
