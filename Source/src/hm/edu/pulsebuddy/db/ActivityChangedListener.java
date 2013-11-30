package hm.edu.pulsebuddy.db;

import hm.edu.pulsebuddy.model.ActivityModel;

public interface ActivityChangedListener
{
  public void handleActivityChangedEvent( ActivityModel aActivity );

}
