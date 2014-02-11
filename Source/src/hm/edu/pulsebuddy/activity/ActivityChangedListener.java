package hm.edu.pulsebuddy.activity;

import hm.edu.pulsebuddy.data.models.ActivityModel;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public interface ActivityChangedListener {
	public void handleActivityChangedEvent(ActivityModel aActivity);
}
