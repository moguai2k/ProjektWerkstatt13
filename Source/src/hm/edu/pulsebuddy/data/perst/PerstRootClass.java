package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.LocationModel;
import hm.edu.pulsebuddy.data.models.Pulse;
import hm.edu.pulsebuddy.data.models.UserModel;

import org.garret.perst.FieldIndex;
import org.garret.perst.Storage;
import org.garret.perst.TimeSeries;

public class PerstRootClass
{
  public TimeSeries<Pulse> pulses;
  public TimeSeries<LocationModel> locations;
  public TimeSeries<ActivityModel> activities;
  
  public FieldIndex<UserModel> user;

  public PerstRootClass( Storage db )
  {
    /* Create time series for the pulse objects. */
    this.pulses = db.createTimeSeries( PulseBlock.class,
        (long) PulseBlock.N_ELEMS_PER_BLOCK * (24*60*60*1000) * 2 );
    
    /* Create time series for the location objects. */
    this.locations = db.createTimeSeries( LocationBlock.class,
        (long) LocationBlock.N_ELEMS_PER_BLOCK * (24*60*60*1000) * 2 );
    
    /* Create time series for the activity objects. */
    this.activities = db.createTimeSeries( ActivityBlock.class,
        (long) ActivityBlock.N_ELEMS_PER_BLOCK * (24*60*60*1000) * 2 );
    
    /* Create user object index. */
    this.user = db.createFieldIndex( UserModel.class, "intIndex", true );
  }

  public PerstRootClass()
  {
  }

}
