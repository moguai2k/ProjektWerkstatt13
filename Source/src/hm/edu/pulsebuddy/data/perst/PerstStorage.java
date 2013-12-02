package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.perst.Pulse.Root;

import org.garret.perst.FieldIndex;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

public class PerstStorage
{

  Storage db = null;

  final static int nElements = 10 * 365;
  final static int pagePoolSize = 32 * 1024 * 1024;

  static final String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  public PerstStorage( String aStorageName )
  {
    db = StorageFactory.getInstance().createStorage();
    db.open( aStorageName + ".dbs", pagePoolSize );
    FieldIndex<PerstRootClass> root = db.<FieldIndex<PerstRootClass>> getRoot();
    
    if ( root == null )
    {
     root.pulses
      pulses. = db.createTimeSeries( QuoteBlock.class,
          (long) QuoteBlock.N_ELEMS_PER_BLOCK * MSECS_PER_DAY * 2 );
      stocks.put( stock );
      db.setRoot( stocks );
    }
  }

}
