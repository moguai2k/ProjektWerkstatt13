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
import hm.edu.pulsebuddy.misc.Help;
import hm.edu.pulsebuddy.misc.MapsActivity;
import hm.edu.pulsebuddy.misc.SettingsActivity;
import hm.edu.pulsebuddy.sportmode.SportModeActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
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

  private final static String TAG = BluetoothLeService.class.getSimpleName();

  private String[] mMenuTitles;

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;

  private LayoutInflater inflater;
  private View layout;
  private TextView toastText;
  private Redrawer redrawer = null;
  private PulsePlot mainPlot = null;

  private DataHandler ds = null;
  private DataInterface di = null;

  @SuppressWarnings( "unused" )
  private DeviceControl dc = null;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    setContentView( R.layout.activity_main );
    //ThreadControl tControl = new ThreadControl();

    mMenuTitles = getResources().getStringArray( R.array.menu_array );
    mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
    mDrawerList = (ListView) findViewById( R.id.left_drawer );

    // Set the adapter for the list view
    mDrawerList.setAdapter( new ArrayAdapter<String>( this,
        R.layout.drawer_list_item, mMenuTitles ) );

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
            mDrawerLayout.closeDrawers();
            break;
          case 1:
            Intent sportMode = new Intent( MainActivity.this,
                SportModeActivity.class );

            // TODO @Team: Fall unterscheidung notwendig: Falls sport test
            // bereits durchgefühgt wurde dann zum Trainingsplan Tab springen
            // falls nicht dann zum Sport Test Tab (erster Tab) springen

            startActivity( sportMode );
            mDrawerLayout.closeDrawers();
            break;
          case 2:
            Intent map = new Intent( MainActivity.this, MapsActivity.class );
            startActivity( map );
            mDrawerLayout.closeDrawers();
            break;
          case 3:
            Intent calculation = new Intent( MainActivity.this,
                CalculationActivity.class );
            startActivity( calculation );
            mDrawerLayout.closeDrawers();
            break;

          default:
        }

      }
    }

    // Set the list's click listener
    mDrawerList.setOnItemClickListener( new DrawerItemClickListener() );

    mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,
        R.drawable.ic_drawer, R.string.drawer_open, R.string.app_name )
    {

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed( View view )
      {
        super.onDrawerClosed( view );
        getActionBar().setTitle( R.string.app_name );
      }

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened( View drawerView )
      {
        super.onDrawerOpened( drawerView );
        getActionBar().setTitle( R.string.drawer_open );
      }
    };

    // Set the drawer toggle as the DrawerListener
    mDrawerLayout.setDrawerListener( mDrawerToggle );

    getActionBar().setDisplayHomeAsUpEnabled( true );
    getActionBar().setHomeButtonEnabled( true );

    /* Important to be the first that is initiated. */
    ds = DataManager.getStorageInstance( this );

    /* Device control for Bluetooth related settings. */
    dc = DeviceManager.getDeviceControlInstance( this );

    /* Data interface. */
    di = ds.getDataInterface();

    //XYPlot aprHistoryPlot = (XYPlot) findViewById( R.id.aprHistoryPlot );
    // MultitouchPlot aprHistoryPlot = (MultitouchPlot)
    // findViewById(R.id.aprHistoryPlot);
    //TextView tv = (TextView) findViewById( R.id.currentPulse );
    //mainPlot = new PulsePlot( aprHistoryPlot, tv, redrawer );
  }

  @Override
  protected void onPostCreate( Bundle savedInstanceState )
  {
    super.onPostCreate( savedInstanceState );
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged( Configuration newConfig )
  {
    super.onConfigurationChanged( newConfig );
    mDrawerToggle.onConfigurationChanged( newConfig );
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
    // Pass the event to ActionBarDrawerToggle, if it returns
    // true, then it has handled the app icon touch event
    if ( mDrawerToggle.onOptionsItemSelected( item ) )
    {
      return true;
    }
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
    XYPlot aprHistoryPlot = (XYPlot) findViewById( R.id.aprHistoryPlot );
    TextView tv = (TextView) findViewById( R.id.currentPulse );
    mainPlot = new PulsePlot( aprHistoryPlot, tv, redrawer );
    if ( redrawer != null )
      redrawer.start();
  }

  @Override
  public void onPause()
  {
    if ( redrawer != null )
      redrawer.pause();
    mainPlot.setResume(false);
    super.onPause();
  }

  @Override
  public void onDestroy()
  {
    Log.d( TAG, "onDestroy" );
    mainPlot.setResume(false);
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