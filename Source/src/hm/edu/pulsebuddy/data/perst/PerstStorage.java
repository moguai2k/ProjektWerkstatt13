package hm.edu.pulsebuddy.data.perst;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class PerstStorage
{
  private static final String TAG = "perst.storage";

  /* Stored on the external storage (SD-Card) */
  private static final String DB_PATH = "/perst/";
  private static final String DB_FILE = "pulsebuddy.dbs";

  /* The database object. */
  private Storage db;

  /* Perst root class. */
  private PerstRootClass root;

  /* TODO-tof: Investigate */
  private static final int pagePoolSize = 32 * 1024 * 1024;

  public PerstStorage( Context context )
  {
    /* Create the path on the SD-Card if it not exists. */
    File dbPath = new File( Environment.getExternalStorageDirectory()
        .getAbsolutePath() + DB_PATH );
    if ( !( dbPath.exists() && dbPath.isDirectory() ) )
    {
      dbPath.mkdirs();
    }

    File dbFile = new File( dbPath, DB_FILE );
    try
    {
      dbFile.createNewFile();
    }
    catch ( IOException e )
    {
      Log.e( TAG, "Error creating DB file." );
    }

    String databasePath = Environment.getExternalStorageDirectory()
        .getAbsolutePath() + DB_PATH + DB_FILE;

    db = StorageFactory.getInstance().createStorage();
    db.open( databasePath, pagePoolSize );

    root = (PerstRootClass) db.getRoot();
    if ( root == null )
    {
      root = new PerstRootClass( db );
      db.setRoot( root );
    }
  }

  public synchronized Boolean addPulseValue( int aPulseValue )
  {
    Pulse pulse = new Pulse();
    pulse.date = new Date().getTime();
    pulse.value = aPulseValue;

    Boolean success = root.pulses.add( pulse );
    db.commit();
    Log.d( TAG, "Saved pulse " + success );

    return success;
  }

  /**
   * Get some pulse related statistics.
   */
  public void printPulseStatistics()
  {
    int i;
    Iterator<Pulse> iterator = root.pulses.iterator();
    for ( i = 0; iterator.hasNext(); i++ )
    {
      Pulse pulse = iterator.next();
      Log.d( TAG, pulse.toString() );
    }
    Log.d( TAG, "Number of pulse values in DB: " + i );
  }
}
