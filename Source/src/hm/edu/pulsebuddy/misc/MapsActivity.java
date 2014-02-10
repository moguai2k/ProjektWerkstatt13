package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.listeners.ActivityListener;
import hm.edu.pulsebuddy.data.models.ActivityModel;
import hm.edu.pulsebuddy.data.models.LocationModel;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity implements ActivityListener
{
  static final LatLng MUNICH = new LatLng( 48.133, 11.566 );
  private GoogleMap map;

  private final int NUMBER_ACTITIY_ENTRIES = 5;
  private final int DISTANCE_BETWEEN_LOCATIONS = 100;

  private MapsAdapter adapter;
  private DataInterface di;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.map_fragment );

    getActionBar().setDisplayHomeAsUpEnabled( true );

    di = DataManager.getDataInterface();

    adapter = new MapsAdapter( MapsActivity.this, R.layout.map_fragment_element );
    adapter.setValues( di.getLastActivities( NUMBER_ACTITIY_ENTRIES ) );

    ListView lv = (ListView) findViewById( R.id.list );
    lv.setAdapter( adapter );

    map = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) )
        .getMap();

    /*
    ArrayList<LocationModel> locations = di
        .getAllLocations( DISTANCE_BETWEEN_LOCATIONS );
    for ( int i = 0; i < locations.size(); i++ )
    {
      LocationModel l = locations.get( i );
      if ( l != null )
        setLocationOnMap( l, "Location " + i + " " );
    }
    */

    /* LocationModel l = di.getLastLocation( 0 ); if ( l != null )
     * setLocationOnMap( l, null ); else { map.moveCamera(
     * CameraUpdateFactory.newLatLngZoom( MUNICH, 15 ) ); map.animateCamera(
     * CameraUpdateFactory.zoomTo( 10 ), 2000, null ); } */
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    di.addActivityListener( this );
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    di.removeActivityListener( this );
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    getMenuInflater().inflate( R.menu.maps_menu, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.maptype:
        if ( map.getMapType() == GoogleMap.MAP_TYPE_HYBRID )
          map.setMapType( GoogleMap.MAP_TYPE_NORMAL );
        else
          map.setMapType( GoogleMap.MAP_TYPE_HYBRID );
        return true;
      case R.id.home:
        NavUtils.navigateUpFromSameTask( this );
        return true;
      default:
        return super.onOptionsItemSelected( item );
    }
  }

  private void setLocationOnMap( LocationModel aLocation, String aTitle )
  {
    if ( aLocation == null )
      return;

    LatLng loc = new LatLng( aLocation.getLatitude(), aLocation.getLongitude() );
    Marker marker = map.addMarker( new MarkerOptions().position( loc ).icon(
        BitmapDescriptorFactory.fromResource( R.drawable.ic_pb ) ) );

    if ( aTitle != null )
      marker.setTitle( aTitle );

    marker.showInfoWindow();

    map.moveCamera( CameraUpdateFactory.newLatLngZoom( loc, 17 ) );
  }

  /**
   * Currently only indicates redrawing the last activities.
   */
  @Override
  public void handleRelevantActivity( ActivityModel aActivity )
  {
    String[] values = di.getLastActivities( NUMBER_ACTITIY_ENTRIES );

    ArrayList<Pulse> pulses = di.getAllPulses();
    Pulse last = pulses.get( pulses.size() - 1 );

    adapter.setValues( values );
    setLocationOnMap( di.getLastLocation( 0 ),
        values[ 0 ] + ", Pulse: " + last.getValue() );
  }

  private class MapsAdapter extends BaseAdapter
  {

    String[] values = null;
    Context context;

    public MapsAdapter( Context context, int textViewResourceId )
    {
      this.values = getValues();
      this.context = context;
    }

    public View getView( int position, View convertView, ViewGroup parent )
    {
      View v = convertView;
      if ( v == null )
      {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        v = inflater.inflate( R.layout.map_fragment_element, null );

      }
      TextView post = (TextView) v.findViewById( R.id.label );
      post.setText( values[ position ] );
      return v;
    }

    public int getCount()
    {
      return values.length;
    }

    public Object getItem( int position )
    {
      return position;
    }

    public long getItemId( int position )
    {
      return position;
    }

    public void setValues( String[] newValues )
    {
      this.values = newValues;
      notifyDataSetChanged();
    }

    private String[] getValues()
    {

      notifyDataSetChanged();
      return values;
    }
  }
}