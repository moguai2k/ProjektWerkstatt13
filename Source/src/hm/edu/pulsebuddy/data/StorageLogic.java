package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.ActivityModel.Type;
import android.content.Context;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class StorageLogic {
	private final static String TAG = "db.storageLogic";

	/* The application context. */
	private Context context;

	/* Pulse part. */
	private int pulseOffsetToSave = 2;
	private int lastPulseValue;

	/* Activity part */
	private ActivityModel.Type lastActivityType = Type.UNKNOWN;
	private final static int MIN_CONFIDENCE = 50;

	public StorageLogic(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @return true if the pulse has to be saved, false otherwise.
	 */
	public Boolean pulseToBeSaved(int aPulseValue) {
		if ((aPulseValue - this.lastPulseValue) >= pulseOffsetToSave
				|| (aPulseValue - this.lastPulseValue) <= -pulseOffsetToSave) {
			this.lastPulseValue = aPulseValue;
			return true;
		} else
			return false;
	}

	/**
	 * Checks if the given activity should be saved.
	 * 
	 * @param aActivity
	 *            the activity to be checked.
	 * @return true if it should be saved, false otherwise.
	 */
	public Boolean activityToBeSaved(ActivityModel aActivity) {
		if (aActivity.getType() == lastActivityType)
			return false;
		else if (aActivity.getConfidence() <= MIN_CONFIDENCE)
			return false;
		else {
			this.lastActivityType = aActivity.getType();
			return true;
		}
	}
}
