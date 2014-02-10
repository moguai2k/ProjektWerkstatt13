package hm.edu.pulsebuddy.data;

import android.content.Context;

public class DataManager {
	private static DataHandler _instance;
	private static DataInterface _dataInterface;

	private DataManager() {
	}

	public static DataHandler getStorageInstance(Context context) {
		if (_instance == null) {
			_instance = new DataHandler(context);
			_dataInterface = _instance.getDataInterface();
		}
		return _instance;
	}

	public static DataHandler getStorageInstance() {
		if (_instance != null)
			return _instance;
		return null;
	}

	public static DataInterface getDataInterface() {
		if (_instance != null)
			return _dataInterface;
		return null;
	}

}
