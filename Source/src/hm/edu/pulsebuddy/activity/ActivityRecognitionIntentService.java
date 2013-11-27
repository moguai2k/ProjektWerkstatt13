package hm.edu.pulsebuddy.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import hm.edu.pulsebuddy.activity.ActivityUtils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ActivityRecognitionIntentService extends IntentService
{
  /* Formats the timestamp in the log */
  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSZ";

  /* Delimits the timestamp from the log info */
  private static final String LOG_DELIMITER = ";;";

  /* A date formatter */
  private SimpleDateFormat dateFormat;

  /* Store the app's shared preferences repository */
  private SharedPreferences prefs;

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
    prefs = getApplicationContext().getSharedPreferences(
        ActivityUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE );

    /* Get a date formatter, and catch errors in the returned timestamp */
    try
    {
      dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    }
    catch ( Exception e )
    {
      Log.e( ActivityUtils.ACTTAG, getString( R.string.date_format_error ) );
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

      /* Log the update */
      logActivityRecognitionResult( result );

      /* Get the most probable activity from the list of activities in the
       * update */
      DetectedActivity mostProbableActivity = result.getMostProbableActivity();

      /* Get the confidence percentage for the most probable activity */
      int confidence = mostProbableActivity.getConfidence();

      /* Get the type of activity */
      int activityType = mostProbableActivity.getType();

      /* Check to see if the repository contains a previous activity */
      if ( !prefs.contains( ActivityUtils.KEY_PREVIOUS_ACTIVITY_TYPE ) )
      {

        /* This is the first type an activity has been detected. Store the type */
        Editor editor = prefs.edit();
        editor.putInt( ActivityUtils.KEY_PREVIOUS_ACTIVITY_TYPE, activityType );
        editor.commit();

        // If the repository contains a type
      }
      else if (
      // If the current type is "moving"
      isMoving( activityType )

      &&

      // The activity has changed from the previous activity
          activityChanged( activityType )

          // The confidence level for the current activity is > 50%
          && ( confidence >= 50 ) )
      {

        // Notify the user
        sendNotification();
      }
    }
  }

  /* PRIVATE METHODS */

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

}
