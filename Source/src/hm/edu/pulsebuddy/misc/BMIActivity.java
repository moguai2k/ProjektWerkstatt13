package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.NumberPicker;

public class BMIActivity extends Activity
{

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.bmi );
    getActionBar().setDisplayHomeAsUpEnabled( true );
  }

}
