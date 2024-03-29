package hm.edu.pulsebuddy.data;

import hm.edu.pulsebuddy.data.listeners.ActivityListener;
import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.CoconiResultModel;
import hm.edu.pulsebuddy.data.models.LocationModel;
import hm.edu.pulsebuddy.data.models.Pulse;
import hm.edu.pulsebuddy.data.models.UserModel;
import hm.edu.pulsebuddy.data.perst.PerstStorage;
import hm.edu.pulsebuddy.location.LocationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class DataInterface implements ActivityListener {
	private static final String TAG = "data.interface";

	private PerstStorage perst;

	/* Location relevant */
	private LocationModel lastLocation = null;

	/* Listeners */
	private ArrayList<ActivityListener> _actListeners = new ArrayList<ActivityListener>();

	public DataInterface(PerstStorage aPerstStoreage) {
		perst = aPerstStoreage;
	}

	/****************************************************************************
	 * 
	 * Pulse related
	 * 
	 ***************************************************************************/

	/**
	 * Return all available pulse values.
	 * 
	 * @return All available pulse values.
	 */
	public ArrayList<Pulse> getAllPulses() {
		return perst.getAllPulses();
	}

	/****************************************************************************
	 * 
	 * User related
	 * 
	 ***************************************************************************/

	/**
	 * Get the user instance object.
	 * 
	 * @return The user object.
	 */
	public synchronized UserModel getUserInstance() {
		return perst.getUser();
	}

	/**
	 * Save the user instance object.
	 * 
	 * @param aUser
	 *            The user object.
	 */
	public synchronized void savaUserInstance(UserModel aUser) {
		perst.setUser(aUser);
	}

	/****************************************************************************
	 * 
	 * Activity related
	 * 
	 ***************************************************************************/

	/**
	 * Returns the last activities in form of a simple String array. You can
	 * specify the number of desired activities. It can happen that the returned
	 * array contains less entries, if they are no available in the database.
	 * 
	 * @param aActivityCount
	 *            The number of returned activities.
	 * 
	 * @return String array containing the activities.
	 */
	public synchronized String[] getLastActivities(int aActivityCount) {
		List<ActivityModel> activities = perst
				.getLastActivities(aActivityCount);
		String[] result = new String[activities.size()];
		Log.d(TAG, "Activities requested: " + aActivityCount);
		Log.d(TAG, "Activities fetched: " + result.length);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < activities.size(); i++) {
			ActivityModel a = activities.get(i);
			result[i] = df.format(new Date(a.getTime())) + " T: " + a.getType()
					+ ", C: " + a.getConfidence();
		}

		return result;
	}

	/**
	 * Add a listener for relevant activity updates.
	 * 
	 * @param listener
	 */
	public synchronized void addActivityListener(ActivityListener listener) {
		_actListeners.add(listener);
		perst.addActivityListener(this);
		Log.d(TAG, "Added activity listener");
	}

	/**
	 * Remove a previously added activity listener.
	 * 
	 * @param listener
	 */
	public synchronized void removeActivityListener(ActivityListener listener) {
		_actListeners.remove(listener);
		perst.removeActivityListener(this);
		Log.d(TAG, "Removed activity listener");
	}

	@Override
	public void handleRelevantActivity(ActivityModel aActivity) {
		Iterator<ActivityListener> i = _actListeners.iterator();
		while (i.hasNext()) {
			((ActivityListener) i.next()).handleRelevantActivity(aActivity);
		}
	}

	/****************************************************************************
	 * 
	 * Location related
	 * 
	 ***************************************************************************/

	/**
	 * Returns the recently received location.
	 * 
	 * @param aMinimumDistance
	 *            The minimum distance related to the previous location.
	 * 
	 * @return An location, null otherwise.
	 */
	public synchronized LocationModel getLastLocation(int aMinimumDistance) {
		LocationModel l = perst.getLastLocation();
		if (l != null) {
			if (lastLocation != null) {
				long dis = LocationUtils.calculateDistance(lastLocation, l);
				Log.d(TAG, "Distance between new and recent location: " + dis);
				Log.d(TAG, "Minimum distance to return a location: "
						+ aMinimumDistance);
				if (dis >= aMinimumDistance) {
					Log.d(TAG, "Got new location");
					lastLocation = l;
					return l;
				} else
					return null;
			}
			lastLocation = l;
			return l;
		}
		return null;
	}

	/**
	 * Returns all locations with a give minimum distance from each other.
	 * 
	 * @param aMinimumDistance
	 *            The minimum distance between the locations.
	 * @return The array with locations.
	 */
	public synchronized ArrayList<LocationModel> getAllLocations(
			int aMinimumDistance) {
		ArrayList<LocationModel> locations = perst.getAllLocations();
		ArrayList<LocationModel> results = new ArrayList<LocationModel>();
		LocationModel currentLocation = null;

		for (int i = 0; locations.size() > i; i++) {
			LocationModel l = locations.get(i);
			if (currentLocation == null) {
				currentLocation = l;
				results.add(l);
			} else {
				long dis = LocationUtils.calculateDistance(currentLocation, l);
				if (dis > aMinimumDistance) {
					results.add(l);
					currentLocation = l;
				}
			}
		}
		return results;
	}

	/****************************************************************************
	 * 
	 * Coconi related
	 * 
	 ***************************************************************************/

	/**
	 * Add a coconi test result to the database.
	 * 
	 * @param aCoconiResult
	 *            The result object.
	 * @return True on success, false otherwise.
	 */
	public synchronized Boolean addCoconiResult(CoconiResultModel aCoconiResult) {
		return perst.addCoconiResult(aCoconiResult);
	}

	/**
	 * Get all coconi test result objects.
	 * 
	 * @return List containing all coconi test results.
	 */
	public synchronized ArrayList<CoconiResultModel> getCoconiTestResults() {
		return perst.getCoconiTestResults();
	}
}
