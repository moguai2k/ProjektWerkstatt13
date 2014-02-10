package hm.edu.pulsebuddy.activity;

import hm.edu.pulsebuddy.data.models.ActivityModel;

public interface ActivityChangedListener {
	public void handleActivityChangedEvent(ActivityModel aActivity);

}
