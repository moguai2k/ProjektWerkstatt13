package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.data.perst.Pulse;

public interface PulseChangedListener
{
  public void handlePulseChangedEvent( Pulse aPulse );
}
