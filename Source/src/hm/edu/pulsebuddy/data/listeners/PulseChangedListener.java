package hm.edu.pulsebuddy.data.listeners;

import hm.edu.pulsebuddy.data.models.Pulse;

public interface PulseChangedListener
{
  public void handlePulseChangedEvent( Pulse aPulse );
}
