package hm.edu.pulsebuddy.data.perst;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.garret.perst.Aggregator;
import org.garret.perst.Aggregator.Aggregate;
import org.garret.perst.IterableIterator;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class PerstStorage
{
  private static final String TAG = "perst.storage";

  /* Stored on the external storage (SD-Card) */
  private static final String DB_PATH = "/perst/pulsebuddy.dbs";

  /* The database object. */
  private Storage db;

  /* Perst root class. */
  private PerstRootClass root;

  private static final int pagePoolSize = 32 * 1024 * 1024;

  private static final String[] months = { "Jan", "Feb", "Mar", "Apr", "May",
      "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  public PerstStorage( Context context )
  {
    String databasePath = Environment.getExternalStorageDirectory()
        .getAbsolutePath() + DB_PATH;

    db = StorageFactory.getInstance().createStorage();
    db.open( databasePath, pagePoolSize );

    root = (PerstRootClass) db.getRoot();
    if ( root == null )
    {
      root = new PerstRootClass( db );
      db.setRoot( root );
    }
  }

  public Boolean addPulseValue( int aPulseValue )
  {
    Pulse pulse = new Pulse();
    pulse.date = new Date().getTime();
    pulse.value = aPulseValue;

    Boolean success = root.pulses.add( pulse );
    db.commit();
    Log.d( TAG, "Saved pulse " + success );

    return success;
  }

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
