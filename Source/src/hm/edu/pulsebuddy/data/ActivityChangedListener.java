package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.model.ActivityModel;

public interface ActivityChangedListener
{
  public void handleActivityChangedEvent( ActivityModel aActivity );

}
