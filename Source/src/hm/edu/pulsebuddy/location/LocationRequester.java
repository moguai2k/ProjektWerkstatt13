package hm.edu.pulsebuddy.location;

import hm.edu.pulsebuddy.data.models.LocationModel;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class LocationRequester implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private static final String TAG = "location.locationRequester";

	/* A request to connect to Location Services */
	private LocationRequest locationRequest;

	/* Stores the current instantiation of the location client in this object */
	private LocationClient locationClient;

	/* The application context */
	private Context context;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the application context.
	 */
	public LocationRequester(Context context) {
		/* The application context. */
		this.context = context;

		/* Create a new global location parameters object */
		locationRequest = LocationRequest.create();

		/* Update interval */
		locationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

		/* Use high accuracy */
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		/* Set the interval ceiling to one minute */
		locationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		/*
		 * Create a new location client, using this class to handle the
		 * callbacks.
		 */
		locationClient = new LocationClient(this.context, this, this);

		locationClient.connect();
	}

	/**
	 * Get the current location, related to the Google Play Services.
	 * 
	 * @return LocationModel
	 */
	public LocationModel getCurrentLocation() {
		/* If Google Play Services is available */
		if (servicesConnected()) {
			/* Get the current location */
			Location currentLocation = locationClient.getLastLocation();

			if (currentLocation == null)
				return null;

			LocationModel l = new LocationModel();
			l.setElevation(currentLocation.getAltitude());
			l.setSpeed(currentLocation.getSpeed());
			l.setLatitude(currentLocation.getLatitude());
			l.setLongitude(currentLocation.getLongitude());
			l.setTime(currentLocation.getTime());
			Log.d(TAG, l.toString());

			return l;
		}

		return null;
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 */
	private boolean servicesConnected() {

		/* Check that Google Play services is available */
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this.context);

		/* If Google Play services is available */
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude()) + ","
				+ location.getSpeed();
		Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
	}
}
