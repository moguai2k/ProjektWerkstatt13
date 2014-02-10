package hm.edu.pulsebuddy.activity;

import hm.edu.pulsebuddy.data.models.ActivityModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRequester implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private static final String TAG = "activity.activityRequester";

	private Context context;

	/* The Google Play Services Activity Client */
	private ActivityRecognitionClient activityClient;

	/* The current pending intent */
	private PendingIntent pIntent;

	/* Detection intervall in millis */
	private static final int detectionIntervalMillis = 5000;

	private Boolean isConnected = false;

	private LocalBroadcastManager locBroadcastManager;

	private List<ActivityChangedListener> _listeners = new ArrayList<ActivityChangedListener>();

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the application context.
	 */
	public ActivityRequester(Context context) {
		/* The application context. */
		this.context = context;

		/*
		 * Create a new activity client, using this class to handle the
		 * callbacks.
		 */
		activityClient = new ActivityRecognitionClient(this.context, this, this);

		activityClient.connect();

		locBroadcastManager = LocalBroadcastManager.getInstance(context);
	}

	/**
	 * TEST
	 */
	public void stopRequester() {
		LocalBroadcastManager.getInstance(this.context).unregisterReceiver(
				messageReceiver);
	}

	/**
	 * Add a activity changed listener.
	 * 
	 * @param listener
	 */
	public synchronized void addActivityChangedListener(
			ActivityChangedListener listener) {
		_listeners.add(listener);

		locBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(
				"hm.edu.pulsebuddy.activity.ACTIVITY_RECOGNITION_DATA"));

		if (!_listeners.isEmpty() && isConnected) {
			startUpdates();
		}
	}

	/**
	 * Remove a previously added activity listener.
	 * 
	 * @param listener
	 */
	public synchronized void removeActivityChangedListener(
			ActivityChangedListener listener) {
		_listeners.remove(listener);
		if (_listeners.isEmpty()) {
			stopUpdates();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.e(TAG, "Connection failed");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		isConnected = true;
		if (_listeners.isEmpty())
			stopUpdates();
		else
			startUpdates();
	}

	@Override
	public void onDisconnected() {
		Log.e(TAG, "Disconnected");
		isConnected = false;
	}

	/* PRIVATE */

	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Serializable ser = intent.getExtras().getSerializable(
					"hm.edu.pulsebuddy.model.ActivityModel");

			ActivityModel activity = (ActivityModel) ser;
			notifyActivityChanged(activity);
		}
	};

	private void startUpdates() {
		if (servicesConnected() && isConnected) {
			Log.d(TAG, "Starting updates");

			Intent intent = new Intent(context,
					ActivityRecognitionIntentService.class);

			pIntent = PendingIntent.getService(context, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			activityClient.requestActivityUpdates(detectionIntervalMillis,
					pIntent);
		}
	}

	private void stopUpdates() {
		if (isConnected) {
			if (pIntent != null) {
				Log.d(TAG, "Stopping updates");
				activityClient.removeActivityUpdates(pIntent);
			}
		}
	}

	/**
	 * 
	 * @param aActivity
	 *            the activity to be notified
	 */
	private synchronized void notifyActivityChanged(ActivityModel aActivity) {
		Iterator<ActivityChangedListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((ActivityChangedListener) i.next())
					.handleActivityChangedEvent(aActivity);
		}
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
}
