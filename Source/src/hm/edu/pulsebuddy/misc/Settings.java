package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.MainActivity;
import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.ble.DeviceControlActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Settings extends Activity {

	  @Override
	  protected void onCreate( Bundle savedInstanceState )
	  {
	    super.onCreate( savedInstanceState );
	    setContentView( R.layout.activity_misc );
	}
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.home_menu, menu);
			return true;
		}
	  
	  @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.home:
					startActivity(new Intent(this, MainActivity.class));
					return true;
				case R.id.settings:
					startActivity(new Intent(this, Settings.class));
					return true;
				case R.id.about:
					startActivity(new Intent(this, About.class));
					return true;
				case R.id.help:
					startActivity(new Intent(this, Help.class));
					return true;
				case R.id.ble_scan:
					startActivity(new Intent(this, DeviceControlActivity.class));
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
	  }
	
}
