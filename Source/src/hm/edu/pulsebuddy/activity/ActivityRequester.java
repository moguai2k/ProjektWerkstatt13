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

  private Context context;

  /* The Google Play Services Activity Client */
  private ActivityRecognitionClient activityClient;

  /* The current pending intent */
  private PendingIntent pIntent;

  private BroadcastReceiver receiver;

  private int detectionIntervalMillis = 30000;
  
  private Boolean isConnected = false;

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

    receiver = new BroadcastReceiver()
    {
      @Override
      public void onReceive( Context context, Intent intent )
      {
        String v = "Activity :" + intent.getStringExtra( "Activity" ) + " "
            + "Confidence : " + intent.getExtras().getInt( "Confidence" ) + "n";
        Log.d( TAG, v );
      }
    };

    IntentFilter filter = new IntentFilter();
    filter.addAction( "hm.edu.pulsebuddy.activity.ACTIVITY_RECOGNITION_DATA" );
    context.registerReceiver( receiver, filter );
  }

  public void startUpdates()
  {
    if ( servicesConnected() && isConnected )
    {
      Intent intent = new Intent( context, ActivityRecognitionIntentService.class );

      pIntent = PendingIntent.getService( context, 0, intent,
          PendingIntent.FLAG_UPDATE_CURRENT );

      activityClient.requestActivityUpdates( detectionIntervalMillis, pIntent );
    }   
  }
  
  public void stopUpdates()
  {
    if ( isConnected )
    {
      activityClient.removeActivityUpdates( pIntent );
    }
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
    isConnected = true;
  }

  @Override
  public void onDisconnected()
  {
    Log.e( TAG, "Disconnected" );
    isConnected = false;
  }
}
