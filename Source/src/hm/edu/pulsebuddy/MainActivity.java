package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.ble.DeviceScanActivity;
import hm.edu.pulsebuddy.misc.*;
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

public class MainActivity extends Activity
{
    // Inflater for custom-toasts
    private LayoutInflater inflater;
    private View layout;
    private TextView toastText;
	
  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );
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
				startActivity(new Intent(this, DeviceScanActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
  }
		
    public void onButtonClick(final View view) {
        switch (view.getId()) {
            case R.id.hs_dm:
                //startActivity(new Intent(this, Daymode.class));
            	customToast("Noch kein Content vorhanden, lol...");
                break;
            case R.id.hs_sm:
                //startActivity(new Intent(this, Sportmode.class));
            	customToast("Noch kein Content vorhanden, lol...");
                break;
            default:
        }
  }
    
    // Custom toast aka alert box
    private void customToast(String text) {
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
        toastText = (TextView) layout.findViewById(R.id.toastText);
        toastText.setText(text);
        Toast t = new Toast(getApplicationContext());
        t.setDuration(Toast.LENGTH_LONG);
        t.setView(layout);
        t.show();
    }
}