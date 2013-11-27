package hm.edu.pulsebuddy.location;

import hm.edu.pulsebuddy.R;
import android.content.Context;
import android.location.Location;

/**
 * Utility class for the location services. Contains specific settings and and
 * constants.
 */
public final class LocationUtils
{

  /* For debugging */
  public static final String LOCTAG = "PB.Location";

  /* Name of shared preferences repository that stores persistent state */
  public static final String SHARED_PREFERENCES = "hm.edu.pulsebuddy.location.SHARED_PREFERENCES";

  /* Key for storing the "updates requested" flag in shared preferences */
  public static final String KEY_UPDATES_REQUESTED = "hm.edu.pulsebuddy.location.KEY_UPDATES_REQUESTED";

  /* Define a request code to send to Google Play services This code is returned
   * in Activity.onActivityResult */
  public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  /* Constants for location update parameters */
  public static final int MILLISECONDS_PER_SECOND = 1000;
  public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
  public static final int FAST_CEILING_IN_SECONDS = 1;

  /* Update interval in milliseconds */
  public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
      * UPDATE_INTERVAL_IN_SECONDS;

  /* A fast ceiling of update intervals, used when the app is visible */
  public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
      * FAST_CEILING_IN_SECONDS;

  public static final String EMPTY_STRING = new String();

  /**
   * Get the latitude and longitude from the Location object returned by
   * Location Services.
   * 
   * @param currentLocation
   *          A Location object containing the current location
   * @return The latitude and longitude of the current location, or null if no
   *         location is available.
   */
  public static String getLatLng( Context context, Location currentLocation )
  {
    if ( currentLocation != null )
    {
      /* Returns the current location as a string. */
      return context.getString( R.string.latitude_longitude,
          currentLocation.getLatitude(), currentLocation.getLongitude() );
    }
    else
    {
      /* Otherwise return an empty string. */
      return EMPTY_STRING;
    }
  }
}