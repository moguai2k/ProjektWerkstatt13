package hm.edu.pulsebuddy.data.listeners;

import hm.edu.pulsebuddy.data.models.Pulse;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public interface PulseChangedListener {
	public void handlePulseChangedEvent(Pulse aPulse);
}
