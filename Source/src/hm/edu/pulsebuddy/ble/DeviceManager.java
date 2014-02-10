package hm.edu.pulsebuddy.ble;

import android.content.Context;

public class DeviceManager {
	private static DeviceControl deviceControl;

	public static DeviceControl getDeviceControlInstance(Context context) {
		if (deviceControl == null) {
			deviceControl = new DeviceControl(context);
		}
		return deviceControl;
	}
}
