package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity
{
  static final LatLng MUNICH = new LatLng( 48.133, 11.566 );
  private GoogleMap map;
  GoogleMapOptions options = new GoogleMapOptions();

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.map_fragment );
    getActionBar().setDisplayHomeAsUpEnabled( true );

    map = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) )
        .getMap();

    // TODO Tore, hier ein Beispiel, wie du die Activities einbringen kannst.
    // Quasi für alle Daten aus der DB hier die Location + Activity eintragen?!
    final LatLng TORE = new LatLng( 48.233, 11.566 );
    map.addMarker( new MarkerOptions()
        .position( TORE )
        .draggable( true )
        .title( "Activity" )
        .snippet( "50% Bewegung, 11:00, 01.01.2014, w/e" )
        .icon( BitmapDescriptorFactory.fromResource( R.drawable.pb ) ) );

    final LatLng CHRIS = new LatLng( 48.133, 11.566 );
    map.addMarker( new MarkerOptions()
        .position( CHRIS )
        .draggable( true )
        .title( "Activity" )
        .snippet( "20% Schwimmen, 11:00, 01.01.2014, w/e" )
        .icon( BitmapDescriptorFactory.fromResource( R.drawable.pb ) ) );
    // Beispiel Ende.

    map.moveCamera( CameraUpdateFactory.newLatLngZoom( MUNICH, 15 ) );
    map.animateCamera( CameraUpdateFactory.zoomTo( 10 ), 2000, null );
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
          map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
          map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        return true;
      case R.id.home:
        NavUtils.navigateUpFromSameTask( this );
        return true;
      default:
        return super.onOptionsItemSelected( item );
    }
  }

}