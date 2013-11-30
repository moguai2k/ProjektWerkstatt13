package hm.edu.pulsebuddy.db;

import hm.edu.pulsebuddy.model.PulseModel;

public interface PulseChangedListener
{
  public void handlePulseChangedEvent( PulseModel aPulse );
}
