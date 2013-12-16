package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.UserModel;
import hm.edu.pulsebuddy.math.BMI;
import hm.edu.pulsebuddy.math.BMR;
import hm.edu.pulsebuddy.math.MathUtilities;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CalculationActivity extends Activity
{
  private DataInterface di;
  private UserModel user;
  // TODO Josef, brauchen in Kalibrierung eine Angabe an Aktivität, siehe
  // Technisches Konzept Grundumsatzenergie
  // TODO Tore muss diese Activität in der DB speichern
  final static double ENERGY_REQUIREMENTS_AS_STUDENT = 1.3;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.calculation );
    getActionBar().setDisplayHomeAsUpEnabled( true );

    di = DataManager.getDataInterface();
    user = di.getUserInstance();

    double bmi = BMI.getBMI( user.getWeight(), user.getHeight() );
    int bmiColor = BMI.getColorForBMI( bmi );
    int bmiDescriptionLikeWHO = BMI.getWHODescriptionForBMI( bmi );
    int bmiDescriptionLikeDGE = BMI.getDGEDescriptionForBMI( bmi,
        user.getGender() );

    double bmr = BMR.getBMR( user.getWeight(), user.getHeight(),
        MathUtilities.getAge( user.getBirthday() ), user.getGender() );
    double bmre = BMR.getBMRE( bmr, ENERGY_REQUIREMENTS_AS_STUDENT );

    setBMIValue( bmi );
    setBMIDescription( bmiDescriptionLikeWHO );
    setBMIColor( bmiColor );

    setBMRValue( bmr );
    setBMREValue( bmre );
  }

  /**
   * Set the bmi value based on calibration user information.
   */
  public void setBMIValue( double bmiValue )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIValue );
    textView.setText( bmiValue + "" );
  }

  /**
   * Change the color of the bmi value.
   * 
   * @param bmiValue
   */
  public void setBMIColor( int bmiColor )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIValue );
    textView.setTextColor( getResources().getColor( bmiColor ) );
  }

  /**
   * Set the bmi description based calculated bmi value.
   */
  public void setBMIDescription( int bmiDescription )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMIRecommendation );
    textView.setText( bmiDescription );
  }

  /**
   * Set the bmr value based on calibration user information.
   */
  public void setBMRValue( double bmrValue )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMRResult );
    textView.setText( bmrValue + " kcal" );
  }

  /**
   * Set the bmre value based on calibration user information.
   */
  public void setBMREValue( double bmre )
  {
    TextView textView = (TextView) findViewById( R.id.textViewBMREResult );
    textView.setText( bmre + " kcal" );
  }
}
