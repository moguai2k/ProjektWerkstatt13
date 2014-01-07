package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class CalorieCalculatorActivity extends Activity
{

  ImageButton imageButtonAppetizer;
  ImageButton imageButtonMainCourse;
  ImageButton imageButtonDessert;
  private int basalMetabolismCalorie = 3000;
  int currentCalorie = 0;
  int stillLeftCalorie = 0;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_calorie_calculator );
    addListenerOnButton();
    setBasalMetabolismCalorie( basalMetabolismCalorie );

  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate( R.menu.calorie_calculator, menu );
    return true;
  }

  public void addListenerOnButton()
  {

    imageButtonAppetizer = (ImageButton) findViewById( R.id.imageButtonAppetizer );
    imageButtonMainCourse = (ImageButton) findViewById( R.id.imageButtonMainCourse );
    imageButtonDessert = (ImageButton) findViewById( R.id.imageButtonDessert );

    imageButtonAppetizer.setOnClickListener( new OnClickListener()
    {

      @Override
      public void onClick( View view )
      {
        int appetizerCalorie = 200;

        int curentCalc = calculateCurrentCalorie( appetizerCalorie );
        setCurrentCalorie( curentCalc );

        int stillLeftCal = calculateStillLeftCalorie( appetizerCalorie );
        setStillLeftCalorie( stillLeftCal );
      }

    } );

    imageButtonMainCourse.setOnClickListener( new OnClickListener()
    {

      @Override
      public void onClick( View view )
      {

      }

    } );

    imageButtonDessert.setOnClickListener( new OnClickListener()
    {

      @Override
      public void onClick( View view )
      {

      }

    } );
  }

  // @Tore, @Team: mit den setter Methoden kann der Grundumsatzt, Aktuelle
  // Kalorienstand und noch übrige Kalorien gesetzt werden.
  private void setBasalMetabolismCalorie( int basalMetabolismCalorie )
  {
    TextView basalMetabolism = (TextView) findViewById( R.id.textViewBasalMetabolism );
    basalMetabolism.setText( basalMetabolismCalorie + "" );

  }

  private void setCurrentCalorie( int currentCalorie )
  {
    TextView currentValue = (TextView) findViewById( R.id.textViewCurrentValue );
    currentValue.setText( currentCalorie + "" );

  }

  private void setStillLeftCalorie( int stillLeftCalorie )
  {
    TextView stillLeftValue = (TextView) findViewById( R.id.textViewStillLeft );
    stillLeftValue.setText( stillLeftCalorie + "" );

  }

  private int calculateCurrentCalorie( int addCalorie )
  {

    currentCalorie = currentCalorie + addCalorie;

    return currentCalorie;

  }

  private int calculateStillLeftCalorie( int reduceCalorie )
  {

    stillLeftCalorie = basalMetabolismCalorie - reduceCalorie;

    return stillLeftCalorie;

  }

  // private int calculateStillLeftCalorie( int basalMetabolismCalorie,
  // int reduceCalorie )
  // {
  // return basalMetabolismCalorie - reduceCalorie;
  //
  // }

}
