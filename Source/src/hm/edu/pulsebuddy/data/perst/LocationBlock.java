package hm.edu.pulsebuddy.data.perst;

import org.garret.perst.TimeSeries;

public class LocationBlock extends TimeSeries.Block
{
  private LocationModel[] locations;

  static final int N_ELEMS_PER_BLOCK = 100;

  public TimeSeries.Tick[] getTicks()
  {
    if ( locations == null )
    {
      locations = new LocationModel[ N_ELEMS_PER_BLOCK ];
      for ( int i = 0; i < N_ELEMS_PER_BLOCK; i++ )
      {
        locations[ i ] = new LocationModel();
      }
    }
    return locations;
  }
}
