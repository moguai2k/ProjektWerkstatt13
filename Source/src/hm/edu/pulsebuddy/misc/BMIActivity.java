package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.R.layout;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;

public class BMIActivity extends Activity
{

  private int bmiValue = 50; // @Chris dummy Wert ersetzten. siehe Methode
                             // setBMIValue
  private String bmiDescription = "Du bist zu fett"; // @Chris dummy Wert
                                                     // ersetzten. Beschreibung
                                                     // mit Methode
                                                     // setBMIDescription
                                                     // setzten.

  private int bmiColor = Color.rgb( 205, 92, 92 ); // @Chris dummy Wert
                                                   // ersetzten. Beschreibung
                                                   // mit Methode
                                                   // setBMIDescription setzten.

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.bmi );
    getActionBar().setDisplayHomeAsUpEnabled( true );
    setBMIValue( bmiValue );
    setBMIDescription( bmiDescription );
    setBMIColor( bmiColor );
  }

  /**
   * Set the bmi value based on calibration user information.
   */
  public void setBMIValue( Integer bmiValue )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIValue );
    textView.setText( bmiValue.toString() );

  }

  /**
   * Change the color of the bmi value.
   * 
   * @param bmiValue
   */
  public void setBMIColor( int bmiColor )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIValue );
    textView.setTextColor( bmiColor );
  }

  /**
   * Set the bmi description based calculated bmi value.
   */
  public void setBMIDescription( String bmiDescription )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIRecommendation );
    textView.setText( bmiDescription );
  }

}
