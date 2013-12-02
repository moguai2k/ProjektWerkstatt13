package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.model.PulseModel;

public interface PulseChangedListener
{
  public void handlePulseChangedEvent( PulseModel aPulse );
}
