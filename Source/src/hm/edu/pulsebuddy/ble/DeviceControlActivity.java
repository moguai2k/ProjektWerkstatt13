/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hm.edu.pulsebuddy.ble;

import hm.edu.pulsebuddy.MainActivity;
import hm.edu.pulsebuddy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
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
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * For a given BLE device, this Activity provides the user interface to connect,
 * display data, and display GATT services and characteristics supported by the
 * device. The Activity communicates with {@code BluetoothLeService}, which in
 * turn interacts with the Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity
{
  private final static String TAG = DeviceControlActivity.class.getSimpleName();

  public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
  public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

  private TextView mConnectionState;
  private TextView mDataField;
  private String mDeviceName;
  private String mDeviceAddress;
  private ExpandableListView mGattServicesList;
  private BluetoothLeService mBluetoothLeService;
  private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
  private boolean mConnected = false;
  private BluetoothGattCharacteristic mNotifyCharacteristic;

  // UUID der Heart Rate Caracteristic
  public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
      .fromString( SampleGattAttributes.HEART_RATE_MEASUREMENT );

  // Code to manage Service lifecycle.
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
        finish();
      }
      // Automatically connects to the device upon successful start-up
      // initialization.
      mBluetoothLeService.connect( mDeviceAddress );
    }

    @Override
    public void onServiceDisconnected( ComponentName componentName )
    {
      mBluetoothLeService = null;
    }
  };

  // Handles various events fired by the Service.
  // ACTION_GATT_CONNECTED: connected to a GATT server.
  // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
  // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
  // ACTION_DATA_AVAILABLE: received data from the device. This can be a result
  // of read or notification operations.
  private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
  {
    private LayoutInflater inflater;
    private View layout;
    private TextView toastText;

    @Override
    public void onReceive( Context context, Intent intent )
    {
      final String action = intent.getAction();
      if ( BluetoothLeService.ACTION_GATT_CONNECTED.equals( action ) )
      {
        mConnected = true;
        updateConnectionState( R.string.connected );
        invalidateOptionsMenu();
      }
      else if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals( action ) )
      {
        mConnected = false;
        String messageText = "Disconnected from: " + mDeviceName;
        customToast( messageText );
        updateConnectionState( R.string.disconnected );
        invalidateOptionsMenu();
        clearUI();
      }
      else if ( BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
          .equals( action ) )
      {
        // Show all the supported services and
        // characteristics on the user interface.
        // displayGattServices( mBluetoothLeService.getSupportedGattServices());
        getHeartRateService( mBluetoothLeService.getSupportedGattServices() );
        // Log.d( TAG, "ACTION_GATT_SERVICES_DISCOVERED: ..." );
        String messageText = "Connectet to: " + mDeviceName;
        customToast( messageText );
        // TODO back to MainActivity
        Intent mainInt = new Intent( DeviceControlActivity.this,
            MainActivity.class );
        mainInt.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY ); // Quick and Dirty:
                                                             // deactivate
                                                             // History
        startActivity( mainInt );
        // finish(); //finishing the activity would stop the heart reat

      }
      else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals( action ) )
      {
        // displayData( intent.getStringExtra( BluetoothLeService.EXTRA_DATA )
        // );
      }
    }

    // Custom toast aka alert box
    private void customToast( String text )
    {
      inflater = getLayoutInflater();
      layout = inflater.inflate( R.layout.custom_toast,
          (ViewGroup) findViewById( R.id.toast_layout ) );
      toastText = (TextView) layout.findViewById( R.id.toastText );
      toastText.setText( text );
      Toast t = new Toast( getApplicationContext() );
      t.setDuration( Toast.LENGTH_SHORT );
      t.setView( layout );
      t.show();
    }
  };

  // If a given GATT characteristic is selected, check for supported features.
  // This sample demonstrates 'Read' and 'Notify' features. See
  // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the
  // complete list of supported characteristic features.

  private void clearUI()
  {
    mGattServicesList.setAdapter( (SimpleExpandableListAdapter) null );
    mDataField.setText( R.string.no_data );
  }

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.gatt_services_characteristics );
    getActionBar().setDisplayHomeAsUpEnabled( true );

    final Intent intent = getIntent();
    mDeviceName = intent.getStringExtra( EXTRAS_DEVICE_NAME );
    mDeviceAddress = intent.getStringExtra( EXTRAS_DEVICE_ADDRESS );

    // Sets up UI references.
    ( (TextView) findViewById( R.id.device_address ) ).setText( mDeviceAddress );
    mGattServicesList = (ExpandableListView) findViewById( R.id.gatt_services_list );
    // mGattServicesList.setOnChildClickListener( servicesListClickListner );
    mConnectionState = (TextView) findViewById( R.id.connection_state );
    mDataField = (TextView) findViewById( R.id.data_value );

    getActionBar().setTitle( mDeviceName );
    getActionBar().setDisplayHomeAsUpEnabled( true );
    Intent gattServiceIntent = new Intent( this, BluetoothLeService.class );
    bindService( gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE );
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    registerReceiver( mGattUpdateReceiver, makeGattUpdateIntentFilter() );
    if ( mBluetoothLeService != null )
    {
      final boolean result = mBluetoothLeService.connect( mDeviceAddress );
      Log.d( TAG, "Connect request result=" + result );
    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    unregisterReceiver( mGattUpdateReceiver );
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    unbindService( mServiceConnection );
    mBluetoothLeService = null;
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    getMenuInflater().inflate( R.menu.gatt_services, menu );
    if ( mConnected )
    {
      menu.findItem( R.id.menu_connect ).setVisible( false );
      menu.findItem( R.id.menu_disconnect ).setVisible( true );
    }
    else
    {
      menu.findItem( R.id.menu_connect ).setVisible( true );
      menu.findItem( R.id.menu_disconnect ).setVisible( false );
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.menu_connect:
        mBluetoothLeService.connect( mDeviceAddress );
        return true;
      case R.id.menu_disconnect:
        mBluetoothLeService.disconnect();
        return true;
      case android.R.id.home:
        onBackPressed();
        // NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected( item );
  }

  private void updateConnectionState( final int resourceId )
  {
    runOnUiThread( new Runnable()
    {
      @Override
      public void run()
      {
        mConnectionState.setText( resourceId );
      }
    } );
  }

  private void displayData( String data )
  {
    if ( data != null )
    {
      mDataField.setText( data );
    }
  }

  private void getHeartRateService( List<BluetoothGattService> gattServices )
  {

    if ( gattServices == null )
    {
      return;
    }

    mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    // Loops through available GATT Services.
    for ( BluetoothGattService gattService : gattServices )
    {
      List<BluetoothGattCharacteristic> gattCharacteristics = gattService
          .getCharacteristics();

      // Loops through available Characteristics.
      for ( BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics )
      {
        // falls Heart Rate Characterristic
        if ( UUID_HEART_RATE_MEASUREMENT.equals( gattCharacteristic.getUuid() ) )
        {
          Log.d( TAG, "UUID_HEART_RATE_MEASUREMENT Avaliable" );
          mNotifyCharacteristic = gattCharacteristic;
          mBluetoothLeService.setCharacteristicNotification(
              gattCharacteristic, true );
        }
      }
    }
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
