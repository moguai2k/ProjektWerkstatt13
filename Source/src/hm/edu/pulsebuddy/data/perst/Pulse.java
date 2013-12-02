package hm.edu.pulsebuddy.data.perst;

import org.garret.perst.Persistent;
import org.garret.perst.TimeSeries;

public class Pulse implements TimeSeries.Tick
{

  public static class PulseBlock extends TimeSeries.Block
  {
    private Pulse[] pulses;

    static final int N_ELEMS_PER_BLOCK = 100;

    public TimeSeries.Tick[] getTicks()
    {
      if ( pulses == null )
      {
        pulses = new Pulse[ N_ELEMS_PER_BLOCK ];
        for ( int i = 0; i < N_ELEMS_PER_BLOCK; i++ )
        {
          pulses[ i ] = new Pulse();
        }
      }
      return pulses;
    }
  }

  static final int MSECS_PER_DAY = 24 * 60 * 60 * 1000;

  int date;
  int pulse;

  public long getTime()
  {
    return (long) date * MSECS_PER_DAY;
  }
}
