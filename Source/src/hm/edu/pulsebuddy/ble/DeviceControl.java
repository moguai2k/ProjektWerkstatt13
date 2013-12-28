package hm.edu.pulsebuddy.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import hm.edu.pulsebuddy.MainActivity;
import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

  private BluetoothLeService mBluetoothLeService;

  private ArrayList<ArrayList<BluetoothGattCharacteristic>> gattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

  private Boolean connected;

  /* Code to manage Service lifecycle */
  private final ServiceConnection mServiceConnection = new ServiceConnection()
  {

    @Override
    public void onServiceConnected( ComponentName componentName, IBinder service )
    {
      mBluetoothLeService = ( (BluetoothLeService.LocalBinder) service )
          .getService();
      if ( !mBluetoothLeService.initialize() )
      {
        Log.e( TAG, "Unable to initialize Bluetooth" );
        return;
      }
      Log.d( TAG, "onServiceConnected" );
      mBluetoothLeService.connect( deviceAddress );
    }

    @Override
    public void onServiceDisconnected( ComponentName componentName )
    {
      Log.d( TAG, "onServiceDisconnected" );
      mBluetoothLeService.disconnect();
      mBluetoothLeService = null;
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
      else if ( BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
          .equals( action ) )
      {
        // TODO
      }
      else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals( action ) )
      {
        if ( connected == true )
        {
          if ( dataHandler != null )
          {
            int heartRate = intent.getExtras().getInt( BluetoothLeService.EXTRA_DATA );
            Log.d(TAG, "Heart rate: " + heartRate );
            //dataHandler.savePulseValue( heartRate );
          }
        }
      }
    }
  };

  public DeviceControl( Context aContext )
  {
    this.context = aContext;
    this.dataHandler = DataManager.getStorageInstance();
  }

  /**
   * Close everything.
   */
  public void close()
  {
    this.context.unregisterReceiver( mGattUpdateReceiver );
    this.context.unbindService( mServiceConnection );
    mBluetoothLeService = null;
  }

  public void setDevice( String aDeviceName, String aDeviceAddress )
  {
    this.deviceName = aDeviceName;
    this.deviceAddress = aDeviceAddress;
  }

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
