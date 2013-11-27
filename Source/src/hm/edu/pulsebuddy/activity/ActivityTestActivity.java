package hm.edu.pulsebuddy.activity;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.activity.ActivityUtils.REQUEST_TYPE;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class ActivityTestActivity extends Activity
{

  private static final int MAX_LOG_SIZE = 5000;

  // Instantiates a log file utility object, used to log status updates
  // private LogFile mLogFile;

  // Store the current request type (ADD or REMOVE)
  private REQUEST_TYPE mRequestType;

  // Holds the ListView object in the UI
  private ListView mStatusListView;

  /* Holds activity recognition data, in the form of strings that can contain
   * markup */
  private ArrayAdapter<Spanned> mStatusAdapter;

  /* Intent filter for incoming broadcasts from the IntentService. */
  IntentFilter mBroadcastFilter;

  // Instance of a local broadcast manager
  private LocalBroadcastManager mBroadcastManager;

  // The activity recognition update request object
  private DetectionRequester mDetectionRequester;

  // The activity recognition update removal object
  private DetectionRemover mDetectionRemover;

  /* Set main UI layout, get a handle to the ListView for logs, and create the
   * broadcast receiver. */
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    // Set the main layout
    setContentView( R.layout.activity_test );

    // Get a handle to the activity update list
    mStatusListView = (ListView) findViewById( R.id.log_listview );

    // Instantiate an adapter to store update data from the log
    mStatusAdapter = new ArrayAdapter<Spanned>( this, R.layout.item_layout,
        R.id.log_text );

    // Bind the adapter to the status list
    mStatusListView.setAdapter( mStatusAdapter );

    // Set the broadcast receiver intent filer
    mBroadcastManager = LocalBroadcastManager.getInstance( this );

    // Create a new Intent filter for the broadcast receiver
    mBroadcastFilter = new IntentFilter(
        ActivityUtils.ACTION_REFRESH_STATUS_LIST );
    mBroadcastFilter.addCategory( ActivityUtils.CATEGORY_LOCATION_SERVICES );

    /* Get detection requester and remover objects */
    mDetectionRequester = new DetectionRequester( this );
    mDetectionRemover = new DetectionRemover( this );

    // Create a new LogFile object
    // mLogFile = LogFile.getInstance( this );

  }

  /* Handle results returned to this Activity by other Activities started with
   * startActivityForResult(). In particular, the method onConnectionFailed() in
   * DetectionRemover and DetectionRequester may call startResolutionForResult()
   * to start an Activity that handles Google Play services problems. The result
   * of this call returns here, to onActivityResult. */
  @Override
  protected void onActivityResult( int requestCode, int resultCode,
      Intent intent )
  {

    // Choose what to do based on the request code
    switch ( requestCode )
    {

      // If the request code matches the code sent in onConnectionFailed
      case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:

        switch ( resultCode )
        {
        // If Google Play services resolved the problem
          case Activity.RESULT_OK:

            // If the request was to start activity recognition updates
            if ( ActivityUtils.REQUEST_TYPE.ADD == mRequestType )
            {

              // Restart the process of requesting activity recognition updates
              mDetectionRequester.requestUpdates();

              // If the request was to remove activity recognition updates
            }
            else if ( ActivityUtils.REQUEST_TYPE.REMOVE == mRequestType )
            {

              /* Restart the removal of all activity recognition updates for the
               * PendingIntent. */
              mDetectionRemover.removeUpdates( mDetectionRequester
                  .getRequestPendingIntent() );

            }
            break;

          // If any other result was returned by Google Play services
          default:

            // Report that Google Play services was unable to resolve the
            // problem.
            Log.d( ActivityUtils.ACTTAG, getString( R.string.no_resolution ) );
        }

        // If any other request code was received
      default:
        // Report that this Activity received an unknown requestCode
        Log.d( ActivityUtils.ACTTAG,
            getString( R.string.unknown_activity_request_code, requestCode ) );

        break;
    }
  }

  /* Register the broadcast receiver and update the log of activity updates */
  @Override
  protected void onResume()
  {
    super.onResume();

    // Register the broadcast receiver
    mBroadcastManager.registerReceiver( updateListReceiver, mBroadcastFilter );

    // Load updated activity history
    updateActivityHistory();
  }

  /* Create the menu */
  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // MenuInflater inflater = getMenuInflater();
    // inflater.inflate( R.menu.menu, menu );
    return true;

  }

  /* Unregister the receiver during a pause */
  @Override
  protected void onPause()
  {

    // Stop listening to broadcasts when the Activity isn't visible.
    mBroadcastManager.unregisterReceiver( updateListReceiver );

    super.onPause();
  }

  /**
   * Verify that Google Play services is available before making a request.
   * 
   * @return true if Google Play services is available, otherwise false
   */
  private boolean servicesConnected()
  {

    // Check that Google Play services is available
    int resultCode = GooglePlayServicesUtil
        .isGooglePlayServicesAvailable( this );

    // If Google Play services is available
    if ( ConnectionResult.SUCCESS == resultCode )
    {

      // In debug mode, log the status
      Log.d( ActivityUtils.ACTTAG, getString( R.string.play_services_available ) );

      // Continue
      return true;

      // Google Play services was not available for some reason
    }
    else
    {

      // Display an error dialog
      GooglePlayServicesUtil.getErrorDialog( resultCode, this, 0 ).show();
      return false;
    }
  }

  /**
   * Respond to "Start" button by requesting activity recognition updates.
   * 
   * @param view
   *          The view that triggered this method.
   */
  public void onStartUpdates( View view )
  {

    // Check for Google Play services
    if ( !servicesConnected() )
    {

      return;
    }

    /* Set the request type. If a connection error occurs, and Google Play
     * services can handle it, then onActivityResult will use the request type
     * to retry the request */
    mRequestType = ActivityUtils.REQUEST_TYPE.ADD;

    // Pass the update request to the requester object
    mDetectionRequester.requestUpdates();
  }

  /**
   * Respond to "Stop" button by canceling updates.
   * 
   * @param view
   *          The view that triggered this method.
   */
  public void onStopUpdates( View view )
  {

    // Check for Google Play services
    if ( !servicesConnected() )
    {

      return;
    }

    /* Set the request type. If a connection error occurs, and Google Play
     * services can handle it, then onActivityResult will use the request type
     * to retry the request */
    mRequestType = ActivityUtils.REQUEST_TYPE.REMOVE;

    // Pass the remove request to the remover object
    mDetectionRemover.removeUpdates( mDetectionRequester
        .getRequestPendingIntent() );

    /* Cancel the PendingIntent. Even if the removal request fails, canceling
     * the PendingIntent will stop the updates. */
    mDetectionRequester.getRequestPendingIntent().cancel();
  }

  /**
   * Display the activity detection history stored in the log file
   */
  private void updateActivityHistory()
  {

    // Load log file records into the List
    // List<Spanned> activityDetectionHistory = mLogFile.loadLogFile();

    // Clear the adapter of existing data
    mStatusAdapter.clear();

    // Add each element of the history to the adapter

    // If the number of loaded records is greater than the max log size
    if ( mStatusAdapter.getCount() > MAX_LOG_SIZE )
    {

    }

    // Trigger the adapter to update the display
    mStatusAdapter.notifyDataSetChanged();

    // If an error occurs while reading the history file

  }

  /**
   * Broadcast receiver that receives activity update intents It checks to see
   * if the ListView contains items. If it doesn't, it pulls in history. This
   * receiver is local only. It can't read broadcast Intents from other apps.
   */
  BroadcastReceiver updateListReceiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive( Context context, Intent intent )
    {

      /* When an Intent is received from the update listener IntentService,
       * update the displayed log. */
      updateActivityHistory();
    }
  };

}
