package hm.edu.pulsebuddy.activity;

import hm.edu.pulsebuddy.model.ActivityModel;

public interface ActivityChangedListener
{
  public void handleActivityChangedEvent( ActivityModel aActivity );

}
