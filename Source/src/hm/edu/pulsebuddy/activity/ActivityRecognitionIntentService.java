package hm.edu.pulsebuddy.activity;

import java.util.Date;

import hm.edu.pulsebuddy.data.models.ActivityModel;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

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

      ActivityModel activity = new ActivityModel();
      activity.setConfidence( confidence );
      activity.setType( mapTypes( activityType ) );
      activity.setTime( new Date().getTime() );

      Intent i = new Intent(
          "hm.edu.pulsebuddy.activity.ACTIVITY_RECOGNITION_DATA" );
      i.putExtra( "hm.edu.pulsebuddy.model.ActivityModel", activity );
      LocalBroadcastManager.getInstance( this ).sendBroadcast( i );
    }
  }

  /* PRIVATE METHODS */

  /**
   * Map detected activity types to activity model types.
   * 
   * @param activityType
   *          The detected activity type
   * @return A activity model type
   */
  private ActivityModel.Type mapTypes( int activityType )
  {
    switch ( activityType )
    {
      case DetectedActivity.IN_VEHICLE:
        return ActivityModel.Type.IN_VEHICLE;
      case DetectedActivity.ON_BICYCLE:
        return ActivityModel.Type.ON_BICYCLE;
      case DetectedActivity.ON_FOOT:
        return ActivityModel.Type.ON_FOOT;
      case DetectedActivity.STILL:
        return ActivityModel.Type.STILL;
      case DetectedActivity.UNKNOWN:
        return ActivityModel.Type.UNKNOWN;
      case DetectedActivity.TILTING:
        return ActivityModel.Type.TILTING;
    }
    return null;
  }

}
