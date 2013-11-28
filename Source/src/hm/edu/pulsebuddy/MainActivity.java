package hm.edu.pulsebuddy;

import java.util.Date;
import java.util.Random;

import hm.edu.pulsebuddy.db.DataStorage;
import hm.edu.pulsebuddy.location.LocationUtils;
import hm.edu.pulsebuddy.settings.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity
{

  DataStorage ds;
  
  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );
    
    ds = new DataStorage( this );
    ds.open();
  }
  
  public void generatePulse( View v )
  {
    Random generator = new Random();
    int pulse = generator.nextInt( 180 ) + 1;
    ds.savePulseValue( pulse );
  }
  

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    getMenuInflater().inflate( R.menu.main, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.action_settings:
        Intent intent = new Intent( this, SettingsActivity.class );
        startActivity( intent );
        return true;

      default:
        return super.onOptionsItemSelected( item );
    }
  }
}
