package hm.edu.pulsebuddy.location;

import hm.edu.pulsebuddy.data.models.LocationModel;

/**
 * Utility class for the location services. Contains specific settings and and
 * constants.
 */
public final class LocationUtils {

	/* Name of shared preferences repository that stores persistent state */
	public static final String SHARED_PREFERENCES = "hm.edu.pulsebuddy.location.SHARED_PREFERENCES";

	/* Key for storing the "updates requested" flag in shared preferences */
	public static final String KEY_UPDATES_REQUESTED = "hm.edu.pulsebuddy.location.KEY_UPDATES_REQUESTED";

	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/* Constants for location update parameters */
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	public static final int FAST_CEILING_IN_SECONDS = 1;

	/* Update interval in milliseconds */
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;

	/* A fast ceiling of update intervals, used when the app is visible */
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;

	/**
	 * Calculates the distance between two location objects.
	 * 
	 * @param aLocOne
	 *            The firt location.
	 * @param aLocTwo
	 *            The second location.
	 * @return Returns the distance between both locations.
	 */
	public static long calculateDistance(LocationModel aLocOne,
			LocationModel aLocTwo) {
		double lat1 = aLocOne.getLatitude();
		double lat2 = aLocTwo.getLatitude();
		double lng1 = aLocOne.getLongitude();
		double lng2 = aLocTwo.getLongitude();

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		long distanceInMeters = Math.round(6371000 * c);
		return distanceInMeters;
	}
}
