package hm.edu.pulsebuddy.ble;

import hm.edu.pulsebuddy.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity
{
  private final static String TAG = DeviceScanActivity.class.getSimpleName();

  private TextView mEmptyList;
  private TextView mTitleView;

  private BluetoothAdapter mBluetoothAdapter;
  private DeviceAdapter mDeviceAdapter;
  private List<BluetoothDevice> mDeviceList;

  private DeviceControl deviceControl;

  private static final int REQUEST_ENABLE_BT = 1;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    Log.d( TAG, "onCreate" );

    /* Get a device control instance. */
    deviceControl = DeviceManager
        .getDeviceControlInstance( getApplicationContext() );

    /* UI */
    requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
    setContentView( R.layout.device_list );
    mTitleView = (TextView) findViewById( R.id.title_devices );
    mEmptyList = (TextView) findViewById( R.id.empty );

    /* Use this check to determine whether BLE is supported on the device. Then
     * you can selectively disable BLE-related features. */
    if ( !getPackageManager().hasSystemFeature(
        PackageManager.FEATURE_BLUETOOTH_LE ) )
    {
      Toast.makeText( this, R.string.ble_not_supported, Toast.LENGTH_SHORT )
          .show();
      finish();
    }

    /* Initializes a Bluetooth adapter. For API level 18 and above, get a
     * reference to BluetoothAdapter through BluetoothManager. */
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService( Context.BLUETOOTH_SERVICE );
    mBluetoothAdapter = bluetoothManager.getAdapter();

    /* Checks if Bluetooth is supported on the device. */
    if ( mBluetoothAdapter == null )
    {
      Toast.makeText( this, R.string.error_bluetooth_not_supported,
          Toast.LENGTH_SHORT ).show();
      finish();
      return;
    }
  }

  @Override
  protected void onResume()
  {
    super.onResume();

    Log.d( TAG, "onResume" );

    /* Ensures Bluetooth is enabled on the device. If Bluetooth is not currently
     * enabled, fire an intent to display a dialog asking the user to grant
     * permission to enable it. */
    if ( !mBluetoothAdapter.isEnabled() )
    {
      Intent enableBtIntent = new Intent(
          BluetoothAdapter.ACTION_REQUEST_ENABLE );
      startActivityForResult( enableBtIntent, REQUEST_ENABLE_BT );

    }

    /* Cancel button finishes activity without returning a device. */
    final Button cancelButton = (Button) findViewById( R.id.btn_disconnectall );
    cancelButton.setOnClickListener( new OnClickListener()
    {
      public void onClick( View paramView )
      {
        if ( deviceControl.isConnected() )
        {
          Log.d( TAG, "Disconnecting device "
              + deviceControl.getBluetoothDevice().getAddress() );

          deviceControl.stopService();
          mBluetoothAdapter.cancelDiscovery();
          mBluetoothAdapter.startDiscovery();
          mDeviceList.clear();
        }
        else
        {
          Log.d( TAG, "No device connected." );
          mTitleView.setText( "No device connected." );
        }
      }
    } );

    /* Scan button restarts the device scan. */
    final Button scanButton = (Button) findViewById( R.id.btn_scan );
    scanButton.setOnClickListener( new OnClickListener()
    {
      public void onClick( View paramView )
      {
        mDeviceList.clear();
        deviceControl.stopService();
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
      }
    } );

    /* Listen for Bluetooth Discovery broadcasts */
    IntentFilter intentFilter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
    intentFilter.addAction( BluetoothAdapter.ACTION_DISCOVERY_STARTED );
    intentFilter.addAction( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
    intentFilter.addAction( BluetoothLeService.HR_NOTIFICATION_SUCCESS );
    intentFilter.addAction( BluetoothLeService.HR_NOTIFICATION_FAIL );
    registerReceiver( mReceiver, intentFilter );

    /* Populate list */
    mDeviceList = new ArrayList<BluetoothDevice>();
    mDeviceAdapter = new DeviceAdapter();

    final ListView listView = (ListView) findViewById( R.id.new_devices );
    listView.setAdapter( mDeviceAdapter );
    listView.setOnItemClickListener( mDeviceClickListener );

    if ( deviceControl.isConnected() )
    {
      BluetoothDevice dev = deviceControl.getBluetoothDevice();
      if ( dev != null )
      {
        Log.d( TAG, "Already connected decvice " + dev.getAddress() );
        mTitleView.setText( dev.getName() + " connected." );
        addDevice( deviceControl.getBluetoothDevice() );
      }
      else
        Log.e( TAG, "Error getting connected Bluetooth device" );

    }
    else
    {
      /* Discover other devices. */
      mBluetoothAdapter.cancelDiscovery();
      mBluetoothAdapter.startDiscovery();
    }
  }

  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent data )
  {
    /* User chose not to enable Bluetooth. */
    if ( requestCode == REQUEST_ENABLE_BT
        && resultCode == Activity.RESULT_CANCELED )
    {
      finish();
      return;
    }
    super.onActivityResult( requestCode, resultCode, data );
  }

  @Override
  public void onStop()
  {
    super.onStop();
    unregisterReceiver( mReceiver );
    if ( mBluetoothAdapter != null )
    {
      // mBluetoothAdapter.cancelDiscovery();
    }
    finish();
  }

  /**
   * Scanning via broadcast.
   */
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive( final Context aContext, final Intent aIntent )
    {
      final String action = aIntent.getAction();
      /* Scanning for devices started. */
      if ( BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals( action ) )
      {
        Log.d( TAG, "Scanning for devices" );
        setProgressBarIndeterminateVisibility( true );
        mTitleView.setText( "Scanning..." );
        if ( mDeviceList.size() == 0 )
        {
          mEmptyList.setText( "No device found yet..." );
        }
      }
      /* Found a new device. */
      if ( BluetoothDevice.ACTION_FOUND.equals( action ) )
      {
        final BluetoothDevice device = (BluetoothDevice) aIntent
            .getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );

        Log.d( TAG, "Found device " + device.getAddress() );

        /* Add the device. */
        addDevice( device );

      }
      /* Scanning for devices finished. */
      if ( BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals( action ) )
      {
        setProgressBarIndeterminateVisibility( false );
        //mTitleView.setText( "Scan finished" );
      }
      /* New */
      if ( BluetoothLeService.HR_NOTIFICATION_SUCCESS.equals( action ) )
      {
        Log.d( TAG, "BLE HR SUCCESS!!!!!" );
        mTitleView.setText( "Connected..." );
        finish();
      }
      if ( BluetoothLeService.HR_NOTIFICATION_FAIL.equals( action ) )
      {
        Log.d( TAG, "BLE HR FAIL!!!!!" );
      }
    }
  };

  /**
   * Simple list adapter for the BLE device list.
   * 
   * @author toffermann
   * 
   */
  private class DeviceAdapter extends BaseAdapter
  {

    private LayoutInflater mInflater;

    public DeviceAdapter()
    {
      mInflater = LayoutInflater.from( DeviceScanActivity.this );
    }

    public int getCount()
    {
      return mDeviceList.size();
    }

    public Object getItem( final int aPosition )
    {
      return mDeviceList.get( aPosition );
    }

    public long getItemId( final int aPosition )
    {
      return aPosition;
    }

    public View getView( final int aPosition, final View aViewToReuse,
        final ViewGroup aParent )
    {
      ViewGroup view = null;
      if ( aViewToReuse != null )
      {
        view = (ViewGroup) aViewToReuse;
      }
      else
      {
        view = (ViewGroup) mInflater.inflate( R.layout.device_element, null );
      }
      final BluetoothDevice device = (BluetoothDevice) mDeviceList
          .get( aPosition );

      final TextView addressView = (TextView) view.findViewById( R.id.address );
      addressView.setText( device.getAddress() );

      final TextView nameView = (TextView) view.findViewById( R.id.name );
      nameView.setText( device.getName() );

      return view;
    }
  }

  /**
   * Handle click on a device in the list
   */
  private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener()
  {
    @Override
    public void onItemClick( final AdapterView<?> aAdapterView,
        final View aView, final int aPosition, final long aId )
    {
      final BluetoothDevice device = mDeviceList.get( aPosition );
      if ( device == null )
        return;

      if ( deviceControl.isConnected() )
      {
        mTitleView.setText( "Already connected..." );
        return;
      }
      else
      {
        mTitleView.setText( "Connecting..." );
        deviceControl.setDevice( device.getName(), device.getAddress() );
        deviceControl.startService();
        // finish();
      }
    }
  };

  /**
   * Add a device.
   * 
   * @param aDevice
   *          The device.
   */
  private void addDevice( final BluetoothDevice aDevice )
  {
    /* If the device list already contains the device, we're done. */
    for ( final BluetoothDevice listItem : mDeviceList )
    {
      if ( listItem.getAddress().equals( aDevice.getAddress() ) )
      {
        Log.d( TAG, "Device already in list " + aDevice.getAddress() );
        return;
      }
    }
    /* Otherwise add the device. */
    mEmptyList.setVisibility( View.GONE );
    mDeviceList.add( aDevice );
    mDeviceAdapter.notifyDataSetChanged();
  }
}