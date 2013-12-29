package hm.edu.pulsebuddy.ble;

import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;

import java.util.UUID;

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

  /* UUID Heart Rate Characteristic */
  public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
      .fromString( SampleGattAttributes.HEART_RATE_MEASUREMENT );

  private Context context;

  private DataHandler dataHandler;

  private String deviceName;
  private String deviceAddress;

  private BluetoothLeService bluetoothLeService;

  @SuppressWarnings( "unused" )
  private Boolean connected = false;

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
      Log.d( TAG, action );
      if ( BluetoothLeService.ACTION_GATT_CONNECTED.equals( action ) )
      {
        connected = true;
        Log.d( TAG, "GATT connected " + deviceName );
      }
      else if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals( action ) )
      {
        connected = false;
        Log.w( TAG, "GATT disconnected" + deviceName );
      }
      else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals( action ) )
      {
        int heartRate = intent.getIntExtra( BluetoothLeService.EXTRA_DATA,
            0 );
        dataHandler.savePulseValue( heartRate );
      }
    }
  };

  /**
   * Ctor
   * 
   * @param aContext
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
    bluetoothLeService.disconnect();
    this.context.unregisterReceiver( mGattUpdateReceiver );
    this.context.unbindService( mServiceConnection );
    
    return true;
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
