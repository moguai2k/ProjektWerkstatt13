package hm.edu.pulsebuddy.activity;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService
{
  @SuppressWarnings( "unused" )
  private static final String TAG = "activity.recognitionService";

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

      Intent i = new Intent(
          "hm.edu.pulsebuddy.activity.ACTIVITY_RECOGNITION_DATA" );
      i.putExtra( "Activity", getNameFromType( activityType ) );
      i.putExtra( "Confidence", confidence );
      sendBroadcast( i );
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
