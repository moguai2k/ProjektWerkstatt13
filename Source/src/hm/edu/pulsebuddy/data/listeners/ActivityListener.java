package hm.edu.pulsebuddy.data.listeners;

import hm.edu.pulsebuddy.data.models.ActivityModel;

public interface ActivityListener {
	public void handleRelevantActivity(ActivityModel aActivity);
}
