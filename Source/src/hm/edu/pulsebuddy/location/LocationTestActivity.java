package hm.edu.pulsebuddy.location;

import java.util.Date;

import hm.edu.pulsebuddy.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTestActivity extends Activity implements LocationListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener
{
  
  private LocationRequester locR;

  /* A request to connect to Location Services */
  private LocationRequest locationRequest;

  /* Stores the current instantiation of the location client in this object */
  private LocationClient locationClient;

  SharedPreferences prefs;
  SharedPreferences.Editor editor;

  /* Note if updates have been turned on. Starts out as "false"; is set to
   * "true" in the method handleRequestSuccess of LocationUpdateReceiver. */
  boolean updatesRequested = false;

  /* UI widgets */
  private TextView uiLatLng;
  private TextView uiTimestamp;
  private TextView uiSpeed;
  private TextView uiAltitude;
  private TextView uiConnectionState;
  private TextView uiConnectionStatus;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.location_test );

    /* Add view part */
    uiLatLng = (TextView) findViewById( R.id.lat_lng );
    uiTimestamp = (TextView) findViewById( R.id.timestamp );
    uiSpeed = (TextView) findViewById( R.id.speed );
    uiAltitude = (TextView) findViewById( R.id.altitude );
    uiConnectionState = (TextView) findViewById( R.id.text_connection_state );
    uiConnectionStatus = (TextView) findViewById( R.id.text_connection_status );

    /* Create a new global location parameters object */
    locationRequest = LocationRequest.create();

    /* Update interval */
    locationRequest.setInterval( LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS );

    /* Use high accuracy */
    locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );

    /* Set the interval ceiling to one minute */
    locationRequest
        .setFastestInterval( LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS );

    /* Start with off. */
    updatesRequested = false;

    /* Open Shared Preferences */
    prefs = getSharedPreferences( LocationUtils.SHARED_PREFERENCES,
        Context.MODE_PRIVATE );

    /* Get an editor */
    editor = prefs.edit();

    /* Create a new location client, using this class to handle the callbacks. */
    locationClient = new LocationClient( this, this, this );
    
    locR = new LocationRequester( getApplicationContext() );
  }

  @Override
  public void onResume()
  {
    super.onResume();

    /* If the app already has a setting for getting location updates, get it */
    if ( prefs.contains( LocationUtils.KEY_UPDATES_REQUESTED ) )
    {
      updatesRequested = prefs.getBoolean( LocationUtils.KEY_UPDATES_REQUESTED,
          false );

      /* otherwise, turn off location updates until requested */
    }
    else
    {
      editor.putBoolean( LocationUtils.KEY_UPDATES_REQUESTED, false );
      editor.commit();
    }
  }

  /* Called when the activity is no longer visible at all. Stop updates and
   * disconnect. */
  @Override
  public void onStop()
  {
    if ( locationClient.isConnected() )
    {
      stopPeriodicUpdates();
    }

    /* After disconnect() is called, the client is considered "dead". */
    locationClient.disconnect();

    super.onStop();
  }

  /* Called when the activity is going into the background. Parts of the UI may
   * be visible, but the Activity is inactive. */
  @Override
  public void onPause()
  {

    /* Save the current setting for updates */
    editor.putBoolean( LocationUtils.KEY_UPDATES_REQUESTED, updatesRequested );
    editor.commit();

    super.onPause();
  }

  /* Called when the activity is restarted, even before it becomes visible. */
  @Override
  public void onStart()
  {

    super.onStart();

    /* Connect the client. Don't restart any requests here; instead, wait for
     * onResume() */
    locationClient.connect();
  }

  /* Handle results returned to this activity by other Activities started with
   * startActivityForResult(). In particular, the method onConnectionFailed() in
   * LocationUpdateRemover and LocationUpdateRequester may call
   * startResolutionForResult() to start an Activity that handles Google Play
   * services problems. The result of this call returns here, to
   * onActivityResult. */
  @Override
  protected void onActivityResult( int requestCode, int resultCode,
      Intent intent )
  {
    /* Choose what to do based on the request code */
    switch ( requestCode )
    {

    /* If the request code matches the code sent in onConnectionFailed */
      case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:

        switch ( resultCode )
        {
        /* If Google Play services resolved the problem */
          case Activity.RESULT_OK:

            /* Log the result */
            Log.d( LocationUtils.LOCTAG, getString( R.string.resolved ) );

            /* Display the result */
            uiConnectionState.setText( R.string.connected );
            uiConnectionStatus.setText( R.string.resolved );
            break;

          /* If any other result was returned by Google Play services */
          default:
            Log.d( LocationUtils.LOCTAG, getString( R.string.no_resolution ) );

            uiConnectionState.setText( R.string.disconnected );
            uiConnectionStatus.setText( R.string.no_resolution );

            break;
        }

        /* If any other request code was received */
      default:
        Log.d( LocationUtils.LOCTAG,
            getString( R.string.unknown_activity_request_code, requestCode ) );

        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate( R.menu.location_test, menu );
    return true;
  }

  @Override
  public void onConnectionFailed( ConnectionResult result )
  {
    /* Google Play services can resolve some errors it detects. If the error has
     * a resolution, try sending an Intent to start a Google Play services
     * activity that can resolve error. */
    if ( result.hasResolution() )
    {
      try
      {

        /* Start an activity that tries to resolve the error */
        result.startResolutionForResult( this,
            LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST );

        /* Thrown if Google Play services canceled the original PendingIntent */

      }
      catch ( IntentSender.SendIntentException e )
      {
        e.printStackTrace();
      }
    }
    else
    {

      /* If no resolution is available, display a dialog to the user with the
       * error. */
      // showErrorDialog( result.getErrorCode() );
    }

  }

  @Override
  public void onConnected( Bundle connectionHint )
  {
    uiConnectionStatus.setText( R.string.connected );

    if ( updatesRequested )
    {
      startPeriodicUpdates();
    }
  }

  @Override
  public void onDisconnected()
  {
    uiConnectionStatus.setText( R.string.disconnected );
  }

  @Override
  public void onLocationChanged( Location location )
  {
    /* Report to the UI that the location was updated */
    uiConnectionStatus.setText( R.string.location_updated );

    /* In the UI, set the latitude and longitude to the value received */
    uiLatLng.setText( LocationUtils.getLatLng( this, location ) );
  }

  /* BUTTONS */

  /**
   * Invoked by the "Get Location" button.
   * 
   * Calls getLastLocation() to get the current location
   * 
   * @param v
   *          The view object associated with this method, in this case a
   *          Button.
   */
  public void getLocation( View v )
  {
    locR.getCurrentLocation();
    
    /* If Google Play Services is available */
    if ( servicesConnected() )
    {

      /* Get the current location */
      Location currentLocation = locationClient.getLastLocation();
      
      String latLon = LocationUtils.getLatLng( this, currentLocation );

      /* TODO: Just test. */
      uiLatLng.setText( latLon );
      uiSpeed.setText( "Speed: " + currentLocation.getSpeed() );
      uiAltitude.setText( "Altitude: " + currentLocation.getAltitude() );
      uiTimestamp.setText( "Timestamp: " + "Timestamp: " + 
          new Date( currentLocation.getTime() ).toString() );
    }
  }

  /**
   * Invoked by the "Start Updates" button Sends a request to start location
   * updates
   * 
   * @param v
   *          The view object associated with this method, in this case a
   *          Button.
   */
  public void startUpdates( View v )
  {
    updatesRequested = true;

    if ( servicesConnected() )
    {
      startPeriodicUpdates();
    }
  }

  /**
   * Invoked by the "Stop Updates" button Sends a request to remove location
   * updates request them.
   * 
   * @param v
   *          The view object associated with this method, in this case a
   *          Button.
   */
  public void stopUpdates( View v )
  {
    updatesRequested = false;

    if ( servicesConnected() )
    {
      stopPeriodicUpdates();
    }
  }

  /* PRIVATE METHODS */

  /**
   * Verify that Google Play services is available before making a request.
   */
  private boolean servicesConnected()
  {

    /* Check that Google Play services is available */
    int resultCode = GooglePlayServicesUtil
        .isGooglePlayServicesAvailable( this );

    /* If Google Play services is available */
    if ( ConnectionResult.SUCCESS == resultCode )
    {
      Log.d( LocationUtils.LOCTAG, getString( R.string.play_services_available ) );

      return true;
    }
    else
    {
      /* Dialog dialog = GooglePlayServicesUtil.getErrorDialog( resultCode,
       * this, 0 ); if ( dialog != null ) { ErrorDialogFragment errorFragment =
       * new ErrorDialogFragment(); errorFragment.setDialog( dialog );
       * errorFragment.show( getSupportFragmentManager(), LocationUtils.APPTAG
       * ); } */
      return false;
    }
  }

  /**
   * In response to a request to start updates, send a request to Location
   * Services
   */
  private void startPeriodicUpdates()
  {

    locationClient.requestLocationUpdates( locationRequest, this );
    uiConnectionState.setText( R.string.location_requested );
  }

  /**
   * In response to a request to stop updates, send a request to Location
   * Services
   */
  private void stopPeriodicUpdates()
  {
    locationClient.removeLocationUpdates( this );
    uiConnectionState.setText( R.string.location_updates_stopped );
  }

}
