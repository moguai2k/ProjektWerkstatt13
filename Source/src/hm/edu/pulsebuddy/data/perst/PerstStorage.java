package hm.edu.pulsebuddy.data.perst;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
  private static final String DB_PATH = "/pulsebuddy/";
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

  /**
   * Synchronized method to add a pulse value to the database.
   * 
   * @param aPulseValue
   *          the pulse value
   * @return true on success, false otherwise.
   */
  public synchronized Boolean addPulseValue( int aPulseValue )
  {
    Pulse pulse = new Pulse();
    pulse.setTime( new Date().getTime() );
    pulse.setValue( aPulseValue );

    Boolean success = root.pulses.add( pulse );
    /* TODO-tof: necessary for every pulse value? */
    db.commit();
    Log.d( TAG, "Saved pulse " + success );

    return success;
  }

  /**
   * Synchronized method to add a location to the database.
   * 
   * @param aLocation
   *          the location to be saved.
   * @return true on success, false otherwise.
   */
  public synchronized Boolean addLocation( LocationModel aLocation )
  {
    Boolean success = root.locations.add( aLocation );
    db.commit();
    Log.d( TAG, "Saved location " + success );

    return success;
  }

  /**
   * Synchronized method to add an activity to the database.
   * 
   * @param aActivity
   *          the activity to be saved.
   * @return true on success, false otherwise.
   */
  public synchronized Boolean addActivity( ActivityModel aActivity )
  {
    Boolean success = root.activities.add( aActivity );
    db.commit();
    Log.d( TAG, "Saved activty " + success );

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

  /**
   * Generates a XML file of the database.
   */
  public void exportXml()
  {
    Long start = System.currentTimeMillis();
    String exportPath = Environment.getExternalStorageDirectory()
        .getAbsolutePath() + DB_PATH + "dbExport.xml";
    Writer writer;
    try
    {
      writer = new BufferedWriter( new FileWriter( exportPath ) );
      db.exportXML( writer );
      writer.close();
    }
    catch ( IOException e )
    {
      Log.e( TAG, "Error writing export XML" );
    }
    Log.i( TAG, "Elapsed time for XML export "
        + ( System.currentTimeMillis() - start ) + " milliseconds" );
  }
}
