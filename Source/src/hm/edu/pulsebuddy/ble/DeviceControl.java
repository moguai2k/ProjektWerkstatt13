package hm.edu.pulsebuddy.ble;

import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;

import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class DeviceControl
{
  private final static String TAG = "DeciveControl";

  private Context context;

  private DataHandler dataHandler;

  private String deviceName;
  private String deviceAddress;

  private BluetoothLeService bluetoothLeService;

  private Boolean connected = false;

  /* UUID Heart Rate Service */
  public final static UUID HR_SERVICE_UUID = UUID
      .fromString( "0000180d-0000-1000-8000-00805f9b34fb" );

  /* UUID Heart Rate Characteristic */
  public final static UUID HR_CHAR_UUID = UUID
      .fromString( "00002a37-0000-1000-8000-00805f9b34fb" );

  public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

  /* Code to manage Service lifecycle */
  private final ServiceConnection mServiceConnection = new ServiceConnection()
  {

    @Override
    public void onServiceConnected( ComponentName componentName, IBinder service )
    {
      bluetoothLeService = ( (BluetoothLeService.LocalBinder) service )
          .getService();
      if ( !bluetoothLeService.initialize() )
      {
        Log.e( TAG, "Unable to initialize Bluetooth" );
        return;
      }
      Log.d( TAG, "onServiceConnected" );
      bluetoothLeService.connect( deviceAddress );
    }

    @Override
    public void onServiceDisconnected( ComponentName componentName )
    {
      Log.d( TAG, "onServiceDisconnected" );
      bluetoothLeService.disconnect();
      bluetoothLeService = null;
    }
  };

  private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive( Context context, Intent intent )
    {
      final String action = intent.getAction();
      if ( BluetoothLeService.ACTION_GATT_CONNECTED.equals( action ) )
      {
        connected = true;
        Log.d( TAG, "GATT connected " + deviceName );
      }
      else if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals( action ) )
      {
        connected = false;
        Log.w( TAG, "GATT disconnected " + deviceName );
      }
      /* Process the pulse value. */
      else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals( action ) )
      {
        int heartRate = intent.getIntExtra( BluetoothLeService.EXTRA_DATA, 0 );
        dataHandler.savePulseValue( heartRate );
      }
    }
  };

  /**
   * Ctor
   * 
   * @param aContext
   *          The application context.
   */
  public DeviceControl( Context aContext )
  {
    this.context = aContext;
    this.dataHandler = DataManager.getStorageInstance();
  }

  /**
   * Set a previously scanned device.
   * 
   * @param aDeviceName
   *          The device name.
   * @param aDeviceAddress
   *          The device address.
   */
  public void setDevice( String aDeviceName, String aDeviceAddress )
  {
    this.deviceName = aDeviceName;
    this.deviceAddress = aDeviceAddress;
  }

  /**
   * Start the service.
   * 
   * @return
   */
  public Boolean startService()
  {
    this.context.registerReceiver( mGattUpdateReceiver,
        makeGattUpdateIntentFilter() );

    Intent gattServiceIntent = new Intent( this.context,
        BluetoothLeService.class );
    this.context.bindService( gattServiceIntent, mServiceConnection,
        Context.BIND_AUTO_CREATE );

    return true;
  }

  /**
   * Stop the service.
   * 
   * @return
   */
  public Boolean stopService()
  {
    if ( bluetoothLeService != null )
    {
      bluetoothLeService.disconnect();
      this.context.unregisterReceiver( mGattUpdateReceiver );
      this.context.unbindService( mServiceConnection );
    }
    return true;
  }

  /**
   * Heart rate sensor is connected.
   * 
   * @return true if a device is connected, false otherwise.
   */
  public Boolean isConnected()
  {
    Log.d( TAG, "isConnected " + connected );
    return connected;
  }
  
  /**
   * Return the connected Bluetooth device.
   * 
   * @return The bluetooth device, null otherwise.
   */
  public BluetoothDevice getBluetoothDevice()
  {
    if ( connected )
      return bluetoothLeService.getBluetoothDevice();
    else 
      return null;
  }

  private static IntentFilter makeGattUpdateIntentFilter()
  {
    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction( BluetoothLeService.ACTION_GATT_CONNECTED );
    intentFilter.addAction( BluetoothLeService.ACTION_GATT_DISCONNECTED );
    intentFilter.addAction( BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED );
    intentFilter.addAction( BluetoothLeService.ACTION_DATA_AVAILABLE );
    return intentFilter;
  }

}
