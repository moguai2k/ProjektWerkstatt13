package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.listeners.ActivityListener;
import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.CoconiResultModel;
import hm.edu.pulsebuddy.data.models.LocationModel;
import hm.edu.pulsebuddy.data.models.Pulse;
import hm.edu.pulsebuddy.data.models.UserModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.garret.perst.Key;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class PerstStorage {
	private static final String TAG = "perst.storage";

	/* Stored on the external storage (SD-Card) */
	private static final String DB_PATH = "/pulsebuddy/";
	private static final String DB_FILE = "pulsebuddy.dbs";

	/* The database object. */
	private Storage db;

	/* Perst root class. */
	private PerstRootClass root;

	/* TODO-tof: Investigate */
	private static final int pagePoolSize = 32 * 1024 * 1024;

	private RrdDb rrdDb;
	private Sample rrdSample;

	/* Listeners */
	private List<ActivityListener> _actListeners = new ArrayList<ActivityListener>();

	public PerstStorage(Context context) {
		/* Create the path on the SD-Card if it not exists. */
		File dbPath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + DB_PATH);
		if (!(dbPath.exists() && dbPath.isDirectory())) {
			dbPath.mkdirs();
		}

		File dbFile = new File(dbPath, DB_FILE);
		try {
			dbFile.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, "Error creating DB file.");
		}

		String databasePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + DB_PATH + DB_FILE;

		db = StorageFactory.getInstance().createStorage();
		db.open(databasePath, pagePoolSize);

		root = (PerstRootClass) db.getRoot();
		if (root == null) {
			root = new PerstRootClass(db);
			db.setRoot(root);
		}

		/* DIRTY */
		String rrdPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + DB_PATH + "rrd4j.rrd";

		try {
			rrdDb = new RrdDb(rrdPath);
		} catch (IOException e) {
			/* If the file does not exist, create a new database. */
			RrdDef rrdDef = new RrdDef(rrdPath);

			/* Sun Jan 11 1970 16:46:44 GMT+0100 (CET) */
			rrdDef.setStartTime(920804400L);
			rrdDef.addDatasource("pulse", DsType.GAUGE, 2, Double.NaN,
					Double.NaN);
			/*
			 * one averages the data every time it is read (e.g. there's nothing
			 * to average) and keeps 24 samples (24 times 5 minutes is 2 hours)
			 */
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 24);
			/*
			 * The other averages 6 values (half hour) and contains 10 of such
			 * averages (e.g. 5 hours) The remaining options will be discussed
			 * later on.
			 */
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 6, 10);
			try {
				rrdDb = new RrdDb(rrdDef);
			} catch (IOException e1) {
				Log.e(TAG, "Error creating round robin database");
			}
		}
		try {
			rrdSample = rrdDb.createSample();
		} catch (IOException e) {
			Log.e(TAG, "Error creating sample.");
		}
	}

	/**
	 * Synchronized method to add a pulse value to the database.
	 * 
	 * @param aPulseValue
	 *            the pulse value
	 * @return true on success, false otherwise.
	 */
	public synchronized Boolean addPulseValue(int aPulseValue) {
		Pulse pulse = new Pulse();
		pulse.setTime(new Date().getTime());
		pulse.setValue(aPulseValue);

		Boolean success = root.pulses.add(pulse);
		/* TODO-tof: necessary for every pulse value? */
		db.commit();
		Log.d(TAG, "Saved pulse " + success);

		long date = new Date().getTime();
		String rrdValue = date + ":" + aPulseValue;
		Log.i(TAG, "RRD Value: " + rrdValue);

		try {
			rrdSample.setAndUpdate(rrdValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long startTime = date - (12 * 60 * 60L);
		FetchRequest fetchRequest = rrdDb.createFetchRequest(ConsolFun.MAX,
				startTime, date);
		Log.d(TAG, fetchRequest.dump());

		return success;
	}

	/**
	 * Returns all pulses.
	 * 
	 * @return All available pulses.
	 */
	public ArrayList<Pulse> getAllPulses() {
		ArrayList<Pulse> pulses = new ArrayList<Pulse>();
		Iterator<Pulse> it = root.pulses.iterator();
		Log.d(TAG, "Get all pulses, size: " + root.pulses.size());
		while (it.hasNext())
			pulses.add((Pulse) it.next());
		return pulses;
	}

	/**
	 * Synchronized method to add a location to the database.
	 * 
	 * @param aLocation
	 *            the location to be saved.
	 * @return true on success, false otherwise.
	 */
	public synchronized Boolean addLocation(LocationModel aLocation) {
		Boolean success = root.locations.add(aLocation);
		db.commit();
		Log.d(TAG, "Saved location " + success);

		return success;
	}

	/****************************************************************************
	 * 
	 * Activity related
	 * 
	 ***************************************************************************/

	/**
	 * Synchronized method to add an activity to the database.
	 * 
	 * @param aActivity
	 *            the activity to be saved.
	 * @return true on success, false otherwise.
	 */
	public Boolean addActivity(ActivityModel aActivity) {
		Boolean success = root.activities.add(aActivity);
		db.commit();
		Log.d(TAG, "Saved activty " + success);

		/* Notify the listeners. */
		notifyActivity(aActivity);

		return success;
	}

	/**
	 * Add a listener to be notified for relevant activity changes.
	 * 
	 * @param listener
	 */
	public void addActivityListener(ActivityListener listener) {
		_actListeners.add(listener);
	}

	/**
	 * Remove a previously added activity change listener.
	 * 
	 * @param listener
	 */
	public void removeActivityListener(ActivityListener listener) {
		_actListeners.remove(listener);
	}

	/**
	 * Forwards the current activity to the listeners.
	 * 
	 * @param aPulse
	 */
	private void notifyActivity(ActivityModel aActivity) {
		Iterator<ActivityListener> i = _actListeners.iterator();
		while (i.hasNext()) {
			((ActivityListener) i.next()).handleRelevantActivity(aActivity);
		}
	}

	/**
	 * Returns a given number of recent activities. If the list contains less
	 * activities then requested, no more are available in the database.
	 * 
	 * @param aNumberOfActivities
	 *            The number of activities to be returned.
	 * @return a list containing recent activities.
	 */
	public List<ActivityModel> getLastActivities(int aNumberOfActivities) {
		List<ActivityModel> activities = new ArrayList<ActivityModel>();
		int i;
		Iterator<ActivityModel> it = root.activities.iterator(false);
		for (i = aNumberOfActivities; i > 0; i--) {
			if (it.hasNext()) {
				ActivityModel a = (ActivityModel) it.next();
				if (a != null)
					activities.add(a);
			}
		}

		return activities;

	}

	/****************************************************************************
	 * 
	 * Location related
	 * 
	 ***************************************************************************/

	/**
	 * Returns the the last saved location.
	 * 
	 * @return The last saved location, null if no location is available.
	 */
	public LocationModel getLastLocation() {
		Iterator<LocationModel> it = root.locations.iterator(false);
		if (it.hasNext())
			return (LocationModel) it.next();
		return null;
	}

	/**
	 * Returns all locations.
	 * 
	 * @return All available locations.
	 */
	public ArrayList<LocationModel> getAllLocations() {
		ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
		Iterator<LocationModel> it = root.locations.iterator();
		Log.d(TAG, "Get all locations, size: " + root.locations.size());
		while (it.hasNext())
			locations.add((LocationModel) it.next());
		return locations;
	}

	/****************************************************************************
	 * 
	 * User related
	 * 
	 ***************************************************************************/

	/**
	 * Returns the default user if it already has been created, otherwise it
	 * returns a new user object with default values.
	 * 
	 * @return The user object.
	 */
	public UserModel getUser() {
		UserModel u = root.user.get(new Key(UserModel.intIndex));
		if (u != null)
			Log.d(TAG, "Get user: " + u.toString());
		else {
			Log.d(TAG, "Creating default user");
			u = new UserModel("defaultUser");
			root.user.put(u);
		}
		return u;
	}

	/**
	 * Method to update the user object.
	 * 
	 * @param aUser
	 *            the user to be updated.
	 * @return true on success, false otherwise.
	 */
	public Boolean setUser(UserModel aUser) {
		Boolean success = root.user.remove(aUser);
		success = root.user.put(aUser);
		db.store(aUser);
		db.commit();
		Log.d(TAG, "Updated user " + success);
		Log.d(TAG, "User: " + aUser.toString());

		return success;
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
	public Boolean addCoconiResult(CoconiResultModel aCoconiResult) {
		root.coconiResult.append(aCoconiResult);
		db.commit();
		Log.d(TAG, "Added coconi result");
		return true;
	}

	/**
	 * Get all coconi test result objects.
	 * 
	 * @return List containing all coconi test results.
	 */
	public ArrayList<CoconiResultModel> getCoconiTestResults() {
		ArrayList<CoconiResultModel> results = new ArrayList<CoconiResultModel>();
		Iterator<CoconiResultModel> it = root.coconiResult.iterator();
		Log.d(TAG,
				"Get all coconi test results, size: "
						+ root.coconiResult.size());
		while (it.hasNext())
			results.add((CoconiResultModel) it.next());
		return results;
	}

	/****************************************************************************
	 * 
	 * Testing Methods
	 * 
	 ***************************************************************************/

	/**
	 * Get some database related statistics.
	 */
	public void printStatistics() {
		Iterator<ActivityModel> iterator = root.activities.iterator();
		int length = root.activities.size();
		while (iterator.hasNext()) {
			ActivityModel activity = iterator.next();
			Log.d(TAG, activity.toString());
		}
		Log.d(TAG, "Number of activity entries: " + length);
	}

	/**
	 * Generates a XML file of the database.
	 */
	public void exportXml() {
		Long start = System.currentTimeMillis();
		String exportPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + DB_PATH + "dbExport.xml";
		Writer writer;
		try {
			writer = new BufferedWriter(new FileWriter(exportPath));
			db.exportXML(writer);
			writer.close();
		} catch (IOException e) {
			Log.e(TAG, "Error writing export XML");
		}
		Log.i(TAG, "Elapsed time for XML export "
				+ (System.currentTimeMillis() - start) + " milliseconds");
	}
}
