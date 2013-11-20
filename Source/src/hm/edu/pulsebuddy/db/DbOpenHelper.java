package hm.edu.pulsebuddy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper
{

  private static final String TAG = "DbHelper";

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "db.pulse";

  public static final String TABLE_NAME = "pulse";
  public static final String COL_ID = "_id";
  public static final String COL_ADDED_ON = "added_on";
  public static final String COL_PULSE = "pulse";

  private static final String DB_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
      + " (" + COL_ID + " integer primary key autoincrement, " + COL_ADDED_ON
      + " TIMESTAMP NOT NULL DEFAULT current_timestamp, " + COL_PULSE
      + " INTEGER NOT NULL);";

  public DbOpenHelper( Context context )
  {
    super( context, DB_NAME, null, DB_VERSION );
  }

  @Override
  public void onCreate( SQLiteDatabase db )
  {
    Log.d( TAG, "created database" );
    db.execSQL( DB_TABLE_CREATE );
  }

  @Override
  public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
  {
    Log.w( DbOpenHelper.class.getName(), "upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data" );
    db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME );
    onCreate( db );
  }
}
