package hm.edu.pulsebuddy.data.listeners;

import hm.edu.pulsebuddy.data.models.ActivityModel;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public interface ActivityListener {
	public void handleRelevantActivity(ActivityModel aActivity);
}
