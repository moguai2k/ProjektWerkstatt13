package hm.edu.pulsebuddy.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRequester implements ConnectionCallbacks,
    OnConnectionFailedListener
{
  private static final String TAG = "activity.activityRequester";

  /* The Google Play Services Activity Client */
  private ActivityRecognitionClient activityClient;

  private PendingIntent pIntent;
  private BroadcastReceiver receiver;

  private int detectionIntervalMillis = 30000;

  private Context context;

  /**
   * Constructor
   * 
   * @param context
   *          the application context.
   */
  public ActivityRequester( Context context )
  {
    /* The application context. */
    this.context = context;

    /* Create a new activity client, using this class to handle the callbacks. */
    activityClient = new ActivityRecognitionClient( this.context, this, this );

    activityClient.connect();
  }

  /**
   * Verify that Google Play services is available before making a request.
   */
  private boolean servicesConnected()
  {

    /* Check that Google Play services is available */
    int resultCode = GooglePlayServicesUtil
        .isGooglePlayServicesAvailable( this.context );

    /* If Google Play services is available */
    if ( ConnectionResult.SUCCESS == resultCode )
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  @Override
  public void onConnectionFailed( ConnectionResult result )
  {
    Log.e( TAG, "Connection failed" );
  }

  @Override
  public void onConnected( Bundle connectionHint )
  {
    Intent intent = new Intent( context, ActivityRecognitionIntentService.class );

    PendingIntent callbackIntent = PendingIntent.getService( context, 0,
        intent, PendingIntent.FLAG_UPDATE_CURRENT );

    activityClient.requestActivityUpdates( 0, callbackIntent );

    // activityClient.disconnect();
  }

  @Override
  public void onDisconnected()
  {
    Log.e( TAG, "Disconnected" );
  }
}
