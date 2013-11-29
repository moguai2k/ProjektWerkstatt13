package hm.edu.pulsebuddy.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService
{
  private static final String TAG = "activity.recognitionService";
  
  /* Formats the timestamp in the log */
  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSZ";

  /* A date formatter */
  private SimpleDateFormat dateFormat;

  public ActivityRecognitionIntentService()
  {
    super( "ActivityRecognitionIntentService" );
  }

  /**
   * Called if a new activity recognition update is available.
   */
  @Override
  protected void onHandleIntent( Intent intent )
  {
    /* Get a date formatter, and catch errors in the returned timestamp */
    try
    {
      dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    }
    catch ( Exception e )
    {
      // Log.e( ActivityUtils.ACTTAG, getString( R.string.date_format_error ) );
    }

    /* Format the timestamp according to the pattern, then localize the pattern */
    dateFormat.applyPattern( DATE_FORMAT_PATTERN );
    dateFormat.applyLocalizedPattern( dateFormat.toLocalizedPattern() );

    /* If the intent contains an update */
    if ( ActivityRecognitionResult.hasResult( intent ) )
    {

      /* Get the update */
      ActivityRecognitionResult result = ActivityRecognitionResult
          .extractResult( intent );

      /* Get the most probable activity from the list of activities in the
       * update */
      DetectedActivity mostProbableActivity = result.getMostProbableActivity();

      /* Get the confidence percentage for the most probable activity */
      int confidence = mostProbableActivity.getConfidence();

      /* Get the type of activity */
      int activityType = mostProbableActivity.getType();
      
      Log.d( TAG, "Activity type: " + getNameFromType( activityType ) );
      Log.d( TAG, "Confidence: " + confidence );

    }
  }

  /* PRIVATE METHODS */

  /**
   * Post a notification to the user. The notification prompts the user to click
   * it to open the device's GPS settings
   */
  private void sendNotification()
  {

    // Create a notification builder that's compatible with platforms >= version
    // 4
    Notification.Builder builder = new Notification.Builder(
        getApplicationContext() );

    // Get the Intent that starts the Location settings panel
    builder.setContentIntent( getContentIntent() );

    // Get an instance of the Notification Manager
    NotificationManager notifyManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

    // Build the notification and post it
    notifyManager.notify( 0, builder.build() );
  }

  /**
   * Get a content Intent for the notification
   * 
   * @return A PendingIntent that starts the device's Location Settings panel.
   */
  private PendingIntent getContentIntent()
  {

    // Set the Intent action to open Location Settings
    Intent gpsIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );

    // Create a PendingIntent to start an Activity
    return PendingIntent.getActivity( getApplicationContext(), 0, gpsIntent,
        PendingIntent.FLAG_UPDATE_CURRENT );
  }

  /**
   * Determine if an activity means that the user is moving.
   * 
   * @param type
   *          The type of activity the user is doing
   * @return true if the user seems to be moving from one location to another,
   *         otherwise false
   */
  private boolean isMoving( int type )
  {
    switch ( type )
    {
      case DetectedActivity.STILL:
      case DetectedActivity.TILTING:
      case DetectedActivity.UNKNOWN:
        return false;
      default:
        return true;
    }
  }

  /**
   * Map detected activity types to strings
   * 
   * @param activityType
   *          The detected activity type
   * @return A user-readable name for the type
   */
  private String getNameFromType( int activityType )
  {
    switch ( activityType )
    {
      case DetectedActivity.IN_VEHICLE:
        return "in_vehicle";
      case DetectedActivity.ON_BICYCLE:
        return "on_bicycle";
      case DetectedActivity.ON_FOOT:
        return "on_foot";
      case DetectedActivity.STILL:
        return "still";
      case DetectedActivity.UNKNOWN:
        return "unknown";
      case DetectedActivity.TILTING:
        return "tilting";
    }
    return "unknown";
  }

}
