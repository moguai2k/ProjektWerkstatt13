package hm.edu.pulsebuddy.ble;

import android.content.Context;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class DeviceManager {
	private static DeviceControl deviceControl;

	public static DeviceControl getDeviceControlInstance(Context context) {
		if (deviceControl == null) {
			deviceControl = new DeviceControl(context);
		}
		return deviceControl;
	}
}
