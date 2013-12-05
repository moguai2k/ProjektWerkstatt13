package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.ble.DeviceScanActivity;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.graph.PulsePlot;
import hm.edu.pulsebuddy.misc.About;
import hm.edu.pulsebuddy.misc.Help;
import hm.edu.pulsebuddy.misc.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.Redrawer;
import com.androidplot.xy.XYPlot;

public class MainActivity extends Activity
{
  private LayoutInflater inflater;
  private View layout;
  private TextView toastText;
  private Redrawer redrawer = null;

  private DataHandler ds = null;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    /* Important to be the first that is initiated. */
    ds = DataManager.getStorageInstance( this );

    XYPlot aprHistoryPlot = (XYPlot) findViewById( R.id.aprHistoryPlot );
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
      case R.id.about:
        startActivity( new Intent( this, About.class ) );
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

  public void onButtonClick( final View view )
  {
    switch ( view.getId() )
    {
      case R.id.hs_dm:
        // startActivity(new Intent(this, Daymode.class));
        customToast( "Noch kein Content vorhanden, lol..." );
        break;
      case R.id.hs_sm:
        // startActivity(new Intent(this, Sportmode.class));
        customToast( "Noch kein Content vorhanden, lol..." );
        break;
      default:
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