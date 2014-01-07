package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.UserModel;
import hm.edu.pulsebuddy.math.BMR;
import hm.edu.pulsebuddy.math.CalorieCalc;
import hm.edu.pulsebuddy.math.MathUtilities;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class CalorieCalculatorActivity extends Activity
{

  ImageButton imageButtonAppetizer;
  ImageButton imageButtonMainCourse;
  ImageButton imageButtonDessert;
  
  private DataInterface di;
  private UserModel user;
  
  int currentCalorie = 0;
  int stillLeftCalorie = 0;
  
  double bmr = 0;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_calorie_calculator );
    getActionBar().setDisplayHomeAsUpEnabled( true );
    addListenerOnButton();
    
    di = DataManager.getDataInterface();
    user = di.getUserInstance();
    
    double bmr = BMR.getBMR( user.getWeight(), user.getHeight(),
        MathUtilities.getAge( user.getBirthday() ), user.getGender() );
    this.bmr = bmr;
    
    setBasalMetabolismCalorie( bmr );
    setCurrentCalorie( 0 ); //TODO Tore: user.getCalories brauchen wir hier
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
        int appetizerCalorie = 100;

        int curentCalc = calculateCurrentCalorie( appetizerCalorie );
        setCurrentCalorie( curentCalc );
      }

    } );

    imageButtonMainCourse.setOnClickListener( new OnClickListener()
    {

      @Override
      public void onClick( View view )
      {
        int appetizerCalorie = 1000;

        int curentCalc = calculateCurrentCalorie( appetizerCalorie );
        setCurrentCalorie( curentCalc );
      }

    } );

    imageButtonDessert.setOnClickListener( new OnClickListener()
    {

      @Override
      public void onClick( View view )
      {
        int appetizerCalorie = 300;

        int curentCalc = calculateCurrentCalorie( appetizerCalorie );
        setCurrentCalorie( curentCalc );
      }

    } );
  }

  private void setBasalMetabolismCalorie( double basalMetabolismCalorie )
  {
    TextView basalMetabolism = (TextView) findViewById( R.id.textViewBasalMetabolism );
    basalMetabolism.setText( basalMetabolismCalorie + "" );
  }

  private void setCurrentCalorie( int currentCalorie )
  {
    int caloriesColor = CalorieCalc.getColorForCalories(bmr,currentCalorie);
    TextView currentValue = (TextView) findViewById( R.id.textViewCurrentValue );
    currentValue.setTextColor( getResources().getColor( caloriesColor ) );
    currentValue.setText( currentCalorie + " kcal" );
  }

  private int calculateCurrentCalorie( int addCalorie )
  {
    currentCalorie = currentCalorie + addCalorie;

    return currentCalorie;
  }
}
