package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.perst.Pulse.PulseBlock;

import org.garret.perst.Persistent;
import org.garret.perst.Storage;
import org.garret.perst.TimeSeries;

public class PerstRootClass extends Persistent
{
  public TimeSeries<Pulse> pulses;

  public PerstRootClass( Storage db )
  {
    this.pulses = db.createTimeSeries( PulseBlock.class,
        (long) PulseBlock.N_ELEMS_PER_BLOCK * Pulse.MSECS_PER_DAY * 2 );
    
    
  }
}
