package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class DaymodeActivity extends FragmentActivity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daymode_activity);
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (null == savedInstanceState) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.chart, new DaymodeFragment()).commit();
		}
	}
}
