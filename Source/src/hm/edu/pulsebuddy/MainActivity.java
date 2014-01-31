package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.ble.BluetoothLeService;
import hm.edu.pulsebuddy.ble.DeviceControl;
import hm.edu.pulsebuddy.ble.DeviceManager;
import hm.edu.pulsebuddy.ble.DeviceScanActivity;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.graph.GraphDayActivity;
import hm.edu.pulsebuddy.graph.PulsePlot;
import hm.edu.pulsebuddy.misc.CalculationActivity;
import hm.edu.pulsebuddy.misc.CalibrationActivity;
import hm.edu.pulsebuddy.misc.CalorieCalculatorActivity;
import hm.edu.pulsebuddy.misc.Help;
import hm.edu.pulsebuddy.misc.MapsActivity;
import hm.edu.pulsebuddy.misc.SettingsActivity;
import hm.edu.pulsebuddy.sportmode.SportModeActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.Redrawer;
import com.androidplot.xy.XYPlot;

public class MainActivity extends Activity
{

  private String[] mPlanetTitles;

  // TODO @Josef ggf. für swipe menu button benötigt, sonst löschen
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;

  private final static String TAG = BluetoothLeService.class.getSimpleName();

  private LayoutInflater inflater;
  private View layout;
  private TextView toastText;
  private Redrawer redrawer = null;

  private DataHandler ds = null;
  private DataInterface di = null;

  @SuppressWarnings( "unused" )
  private DeviceControl dc = null;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    setContentView( R.layout.activity_main );
    // setContentView( R.layout.activity_main );

    mPlanetTitles = getResources().getStringArray( R.array.planets_array );

    mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );

    mDrawerList = (ListView) findViewById( R.id.left_drawer );

    // Set the adapter for the list view
    mDrawerList.setAdapter( new ArrayAdapter<String>( this,
        R.layout.drawer_list_item, mPlanetTitles ) );

    // The click listner for ListView in the navigation drawer
    class DrawerItemClickListener implements ListView.OnItemClickListener
    {
      @Override
      public void onItemClick( AdapterView<?> parent, View view, int position,
          long id )
      {
        selectItem( position );
      }

      private void selectItem( int position )
      {

        switch ( position )
        {
          case 0:
            Intent dayMode = new Intent( MainActivity.this,
                GraphDayActivity.class );
            startActivity( dayMode );
            break;
          case 1:
            Intent sportMode = new Intent( MainActivity.this,
                SportModeActivity.class );

            // TODO @Team: Fall unterscheidung notwendig: Falls sport test
            // bereits durchgefühgt wurde dann zum Trainingsplan Tab springen
            // falls nicht dann zum Sport Test Tab (erster Tab) springen

            startActivity( sportMode );
            break;
          case 2:
            Intent map = new Intent( MainActivity.this, MapsActivity.class );
            startActivity( map );
            break;
          case 3:
            Intent calculation = new Intent( MainActivity.this,
                CalculationActivity.class );
            startActivity( calculation );
            break;
          case 4:
            Intent calorie = new Intent( MainActivity.this,
                CalorieCalculatorActivity.class );
            startActivity( calorie );
            break;

          default:
        }

      }
    }

    // Set the list's click listener
    mDrawerList.setOnItemClickListener( new DrawerItemClickListener() );

    /* The click listner for ListView in the navigation drawer */

    /* Important to be the first that is initiated. */
    ds = DataManager.getStorageInstance( this );

    /* Device control for Bluetooth related settings. */
    dc = DeviceManager.getDeviceControlInstance( this );

    /* Data interface. */
    di = ds.getDataInterface();

    XYPlot aprHistoryPlot = (XYPlot) findViewById( R.id.aprHistoryPlot );
    // MultitouchPlot aprHistoryPlot = (MultitouchPlot)
    // findViewById(R.id.aprHistoryPlot);
    TextView tv = (TextView) findViewById( R.id.currentPulse );

    new PulsePlot( aprHistoryPlot, tv, redrawer );
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate( R.menu.home_menu, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.settings:
        startActivity( new Intent( this, SettingsActivity.class ) );
        return true;
      case R.id.calibration:
        startActivity( new Intent( this, CalibrationActivity.class ) );
        return true;
      case R.id.help:
        startActivity( new Intent( this, Help.class ) );
        return true;
      case R.id.ble_scan:
        startActivity( new Intent( this, DeviceScanActivity.class ) );
        return true;
      default:
        return super.onOptionsItemSelected( item );
    }
  }

  @Override
  public void onResume()
  {
    super.onResume();
    if ( redrawer != null )
      redrawer.start();
  }

  @Override
  public void onPause()
  {
    if ( redrawer != null )
      redrawer.pause();
    super.onPause();
  }

  @Override
  public void onDestroy()
  {
    Log.d( TAG, "onDestroy" );
    if ( redrawer != null )
      redrawer.finish();

    super.onDestroy();
  }

  // Custom toast aka alert box
  private void customToast( String text )
  {
    inflater = getLayoutInflater();
    layout = inflater.inflate( R.layout.custom_toast,
        (ViewGroup) findViewById( R.id.toast_layout ) );
    toastText = (TextView) layout.findViewById( R.id.toastText );
    toastText.setText( text );
    Toast t = new Toast( getApplicationContext() );
    t.setDuration( Toast.LENGTH_LONG );
    t.setView( layout );
    t.show();
  }
}