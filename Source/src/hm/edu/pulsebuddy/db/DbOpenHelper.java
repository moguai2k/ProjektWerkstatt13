package hm.edu.pulsebuddy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper
{
  private static final String TAG = "pb.DbHelper";

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "db.pulse";

  public static final String PULSE_TABLE_NAME = "pulse";
  public static final String PULSE_COL_ID = "_id";
  public static final String PULSE_COL_TIMESTAMP = "timestamp";
  public static final String PULSE_COL_PULSE = "pulse";

  public static final String LOCATION_TABLE_NAME = "location";
  public static final String LOCATION_COL_ID = "_id";
  public static final String LOCATION_COL_LATITUDE = "latitude";
  public static final String LOCATION_COL_LONGITUDE = "longitude";
  public static final String LOCATION_COL_ELEVATION = "elevation";
  public static final String LOCATION_COL_SPEED = "speed";
  public static final String LOCATION_COL_TIMESTAMP = "timestamp";

  private static final String PULSE_TABLE_CREATE = "CREATE TABLE "
      + PULSE_TABLE_NAME + " (" + PULSE_COL_ID
      + " integer primary key autoincrement, " + PULSE_COL_TIMESTAMP
      + " TIMESTAMP NOT NULL DEFAULT current_timestamp, " + PULSE_COL_PULSE
      + " INTEGER NOT NULL);";

  private static final String LOCATION_TABLE_CREATE = "CREATE TABLE "
      + LOCATION_TABLE_NAME + " (" + LOCATION_COL_ID
      + " integer primary key autoincrement, " + LOCATION_COL_LATITUDE
      + " TEXT NOT NULL, " + LOCATION_COL_LONGITUDE + " TEXT NOT NULL, "
      + LOCATION_COL_ELEVATION + " dobule NOT NULL," + LOCATION_COL_SPEED
      + " double NOT NULL);";

  public DbOpenHelper( Context context )
  {
    super( context, DB_NAME, null, DB_VERSION );
  }

  @Override
  public void onCreate( SQLiteDatabase db )
  {
    Log.d( TAG, "created table pulse" );
    db.execSQL( PULSE_TABLE_CREATE );
    Log.d( TAG, "created table location" );
    db.execSQL( LOCATION_TABLE_CREATE );
  }

  @Override
  public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
  {
    Log.w( DbOpenHelper.class.getName(), "upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data" );
    db.execSQL( "DROP TABLE IF EXISTS " + PULSE_TABLE_NAME );
    db.execSQL( "DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME );
    onCreate( db );
  }
}
