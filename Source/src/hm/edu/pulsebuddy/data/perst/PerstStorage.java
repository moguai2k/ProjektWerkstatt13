package hm.edu.pulsebuddy.data.perst;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;

import org.garret.perst.Key;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;

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

  private RrdDb rrdDb;
  private Sample rrdSample;

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

    /* DIRTY */
    String rrdPath = Environment.getExternalStorageDirectory()
        .getAbsolutePath() + DB_PATH + "rrd4j.rrd";

    try
    {
      rrdDb = new RrdDb( rrdPath );
    }
    catch ( IOException e )
    {
      /* If the file does not exist, create a new database. */
      RrdDef rrdDef = new RrdDef( rrdPath );

      /* Sun Jan 11 1970 16:46:44 GMT+0100 (CET) */
      rrdDef.setStartTime( 920804400L );
      rrdDef.addDatasource( "pulse", DsType.COUNTER, 600, Double.NaN,
          Double.NaN );
      /* one averages the data every time it is read (e.g. there's nothing to
       * average) and keeps 24 samples (24 times 5 minutes is 2 hours) */
      rrdDef.addArchive( ConsolFun.AVERAGE, 0.5, 1, 24 );
      /* The other averages 6 values (half hour) and contains 10 of such
       * averages (e.g. 5 hours) The remaining options will be discussed later
       * on. */
      rrdDef.addArchive( ConsolFun.AVERAGE, 0.5, 6, 10 );
      try
      {
        rrdDb = new RrdDb( rrdDef );
      }
      catch ( IOException e1 )
      {
        Log.e( TAG, "Error creating round robin database" );
      }
    }
    try
    {
      rrdSample = rrdDb.createSample();
    }
    catch ( IOException e )
    {
      Log.e( TAG, "Error creating sample." );
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

    long date = new Date().getTime();
    String rrdValue = date + ":" + aPulseValue;
    Log.i( TAG, "RRD Value: " + rrdValue );

    try
    {
      rrdSample.setAndUpdate( rrdValue );
    }
    catch ( IOException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

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
   * Synchronized method to update the user object.
   * 
   * @param aUser
   *          the user to be updated.
   * @return true on success, false otherwise.
   */
  public synchronized Boolean setUser( UserModel aUser )
  {
    Boolean success = root.user.remove( aUser );
    success = root.user.put( aUser );
    db.store( aUser );
    db.commit();
    Log.d( TAG, "Updated user " + success );
    Log.d( TAG, "User: " + aUser.toString() );

    return success;
  }

  /**
   * Returns the default user if it already has been created, otherwise it
   * returns a new user object with default values.
   * 
   * @return The user object.
   */
  public synchronized UserModel getUser()
  {
    UserModel u = root.user.get( new Key( UserModel.intIndex ) );
    if ( u != null )
      Log.d( TAG, "Get user: " + u.toString() );
    else
    {
      Log.d( TAG, "Creating default user" );
      u = new UserModel( "defaultUser" );
      root.user.put( u );
    }
    return u;
  }

  /**
   * Get some database related statistics.
   */
  public void printStatistics()
  {
    int i;
    Iterator<LocationModel> iterator = root.locations.iterator();
    for ( i = 0; iterator.hasNext(); i++ )
    {
      LocationModel location = iterator.next();
      Log.d( TAG, location.toString() );
    }
    Log.d( TAG, "Number of database entries: " + i );
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
