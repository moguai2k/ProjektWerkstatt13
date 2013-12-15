package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity
{
  static final LatLng MUNICH = new LatLng( 48.133, 11.566 );
  private GoogleMap map;

  // TODO Das ist ein Test-Array für die Activities. Wohmöglich sollte es
  // mehrdimensional werden, damit man dem Array "Activity" (Activity-Logo) +
  // "Text" (Ws, Zeit, etc) mitgeben kann. Vllt sollte das Listview auch auf
  // max. die letzten 3 Einträge beschränkt werden, damit nicht mehr
  // angezeigt werden.
  String[] newValues = new String[] { "<-ActivityLogo Laufen, 50% WS",
      "<-ActivityLogo Schwimmen, 50% WS", "<-ActivityLogo Idle, 50% WS" };

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.map_fragment );

    getActionBar().setDisplayHomeAsUpEnabled( true );

    MapsAdapter adapter = new MapsAdapter( MapsActivity.this,
        R.layout.map_fragment_element );
    adapter.setValues( newValues );

    ListView lv = (ListView) findViewById( R.id.list );
    lv.setAdapter( adapter );

    map = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) )
        .getMap();

    // TODO Tore, hier ein Beispiel für Maps, wie du die Activities einbringen
    // kannst. Quasi für alle Daten aus der DB hier die Location + Activity
    // eintragen?!
    final LatLng TORE = new LatLng( 48.233, 11.566 );
    map.addMarker( new MarkerOptions().position( TORE ).draggable( true )
        .title( "Activity" ).snippet( "50% Bewegung, 11:00, 01.01.2014, w/e" )
        .icon( BitmapDescriptorFactory.fromResource( R.drawable.pb ) ) );

    final LatLng CHRIS = new LatLng( 48.133, 11.566 );
    map.addMarker( new MarkerOptions().position( CHRIS ).draggable( true )
        .title( "Activity" ).snippet( "20% Schwimmen, 11:00, 01.01.2014, w/e" )
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