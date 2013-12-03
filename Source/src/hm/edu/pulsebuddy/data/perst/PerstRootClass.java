package hm.edu.pulsebuddy.data.perst;

import org.garret.perst.Storage;
import org.garret.perst.TimeSeries;

public class PerstRootClass
{
  public TimeSeries<Pulse> pulses;

  public PerstRootClass( Storage db )
  {
    /* Create time series for the pulse values. */
    this.pulses = db.createTimeSeries( PulseBlock.class,
        (long) PulseBlock.N_ELEMS_PER_BLOCK * (24*60*60*1000) * 2 );
  }

  public PerstRootClass()
  {
  }

}
