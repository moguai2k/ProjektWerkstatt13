package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class About extends Activity
{

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_misc );
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.home:
        NavUtils.navigateUpFromSameTask( this );
        return true;
      default:
        return super.onOptionsItemSelected( item );
    }
  }

}
