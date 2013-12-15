package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.models.ActivityModel;

import org.garret.perst.TimeSeries;

public class ActivityBlock extends TimeSeries.Block
{
  private ActivityModel[] activities;

  static final int N_ELEMS_PER_BLOCK = 100;

  public TimeSeries.Tick[] getTicks()
  {
    if ( activities == null )
    {
      activities = new ActivityModel[ N_ELEMS_PER_BLOCK ];
      for ( int i = 0; i < N_ELEMS_PER_BLOCK; i++ )
      {
        activities[ i ] = new ActivityModel();
      }
    }
    return activities;
  }
}
