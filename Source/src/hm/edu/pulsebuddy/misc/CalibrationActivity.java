package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.Gender;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.perst.UserModel;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The calibration collects user information like date of birthday, height,
 * weight and gender. This user information are needed for BMI, BMR, ...
 * 
 * @author josefbichlmeier
 * 
 */
public class CalibrationActivity extends Activity implements
    OnDateChangedListener
{
  private Date dateOfBirthday;
  private int userHeight;
  private int userWeight;
  private Gender userGender;

  private DataHandler dh;
  private UserModel user;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.calibration05 );

    dh = DataManager.getStorageInstance();
    user = dh.getUserInstance();

    getActionBar().setDisplayHomeAsUpEnabled( true );

    setValuesUserHeight();
    setValuesUserWeight();
    setValuesUserGender();
    setValuesUserBirthday();
    saveCalibrationValues();
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

  private void setValuesUserBirthday()
  {
    DatePicker datePicker = (DatePicker) findViewById( R.id.datePickerBirthday );

    Calendar c = Calendar.getInstance();
    c.setTimeInMillis( user.getBirthday().getTime() );

    int year = c.get( Calendar.YEAR );
    int month = c.get( Calendar.MONTH );
    int day = c.get( Calendar.DATE );
    datePicker.init( year, month, day, this );

    setDateOfBirthday( user.getBirthday() );
  }

  /**
   * Set the number picker for user height in calibration.
   */
  private void setValuesUserHeight()
  {
    NumberPicker numberPickerUserHeight = (NumberPicker) findViewById( R.id.numberPickerUserHeight );

    String[] userHeights = new String[ 161 ];
    int index = 0;
    for ( int height = 100; height <= 260; height++ )
    {
      userHeights[ index++ ] = String.format( "%3d", height );
    }

    int minUserHeight = 100;
    int maxUserHeight = 260;
    numberPickerUserHeight.setMinValue( minUserHeight );
    numberPickerUserHeight.setMaxValue( maxUserHeight );
    numberPickerUserHeight.setWrapSelectorWheel( false );
    numberPickerUserHeight.setDisplayedValues( userHeights );
    numberPickerUserHeight
        .setDescendantFocusability( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

    getSelectedUserHeight( numberPickerUserHeight );

    if ( user != null )
    {
      numberPickerUserHeight.setValue( user.getHeight() );
      setUserHeight( user.getHeight() );
    }
  }

  /**
   * Get selected user height value.
   * 
   * @param numberPickerUserHeight
   *          Selector for user height.
   */
  private void getSelectedUserHeight( NumberPicker numberPickerUserHeight )
  {
    numberPickerUserHeight
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker,
              int oldUserHeightValue, int newUserHeightValue )
          {
            setUserHeight( newUserHeightValue );
          }
        } );
  }

  /**
   * Set the number picker for user weight in calibration.
   */
  private void setValuesUserWeight()
  {
    NumberPicker numberPickerForWeight = (NumberPicker) findViewById( R.id.numberPickerWeight );

    String[] userweights = new String[ 271 ];
    int index = 0;
    for ( int weight = 30; weight <= 300; weight++ )
    {
      userweights[ index++ ] = String.format( "%3d", weight );
    }

    int minUserWeight = 30;
    int maxUserWeight = 300;
    numberPickerForWeight.setMinValue( minUserWeight );
    numberPickerForWeight.setMaxValue( maxUserWeight );
    numberPickerForWeight.setWrapSelectorWheel( false );
    numberPickerForWeight.setDisplayedValues( userweights );
    numberPickerForWeight
        .setDescendantFocusability( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

    getSelectedUserWeight( numberPickerForWeight );

    if ( user != null )
    {
      numberPickerForWeight.setValue( user.getWeight() );
      setUserWeight( user.getWeight() );
    }

  }

  /**
   * Get selected user weight value.
   * 
   * @param numberPickerUserWeight
   *          Selector for user weight.
   */
  private void getSelectedUserWeight( NumberPicker numberPickerUserWeight )
  {
    numberPickerUserWeight
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker,
              int oldUserWeightValue, int newUserWeightValue )
          {
            setUserWeight( newUserWeightValue );
          }
        } );
  }

  /**
   * Set the number picker for user gender in calibration.
   */
  private void setValuesUserGender()
  {
    NumberPicker numberPickerUserGender = (NumberPicker) findViewById( R.id.numberPickerUserGender );

    String[] genders = { "weiblich", "männlich" };

    numberPickerUserGender.setMinValue( 1 );
    numberPickerUserGender.setMaxValue( genders.length );
    numberPickerUserGender.setWrapSelectorWheel( false );
    numberPickerUserGender.setDisplayedValues( genders );
    numberPickerUserGender
        .setDescendantFocusability( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

    getSelectedUserGender( numberPickerUserGender );

    if ( user != null )
    {
      Gender g = user.getGender();
      if ( g == Gender.female )
        numberPickerUserGender.setValue( 1 );
      else if ( g == Gender.male )
        numberPickerUserGender.setValue( 2 );

      setUserGender( g );
    }
  }

  /**
   * Get selected user gender value.
   * 
   * @param numberPickerUserGender
   *          Selector for user gender.
   */
  private void getSelectedUserGender( NumberPicker numberPickerUserGender )
  {
    numberPickerUserGender
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker,
              int oldUserGenderValue, int newUserGenderValue )
          {
            setUserGender( calclulateUserGender( newUserGenderValue ) );

          }
        } );
  }

  /**
   * Convert the displayed value from string to enum.
   * 
   * @param userGender
   *          1 is female and 2 is male.
   * 
   * @return Calculated user gender value.
   */
  private Gender calclulateUserGender( int userGender )
  {

    if ( userGender == 1 )
    {
      return Gender.female;
    }
    else if ( userGender == 2 )
    {
      return Gender.male;
    }
    else
    {
      // TODO exceptoin
      // default value
      return Gender.male;
    }
  }

  /**
   * Triggers the BMI calculation based on the user values
   */
  private void saveCalibrationValues()
  {
    Button button = (Button) findViewById( R.id.buttonBmiCalculation );
    button.setOnClickListener( new View.OnClickListener()
    {
      public void onClick( View v )
      {
        user.setWeight( userWeight );
        user.setHeight( userHeight );
        user.setGender( userGender );
        user.setBirthday( dateOfBirthday );

        dh.savaUserInstance( user );

        customToast( "Saved user data." );
      }
    } );
  }

  private void setUserWeight( int userWeight )
  {
    Log.d( "pulsebuddy debug: ", "set weight: " + userWeight );
    this.userWeight = userWeight;
  }

  private void setUserHeight( int userHeight )
  {
    Log.d( "pulsebuddy debug: ", "set user height: " + userHeight );
    this.userHeight = userHeight;
  }

  private void setUserGender( Gender userGender )
  {
    Log.d( "pulsebuddy debug: ", "set gender: " + userGender );
    this.userGender = userGender;
  }

  private void setDateOfBirthday( Date dateOfBirthday )
  {
    Log.d( "pulsebuddy debug: ", "set date of birthday: " + dateOfBirthday );
    this.dateOfBirthday = dateOfBirthday;
  }

  private void customToast( String text )
  {
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate( R.layout.custom_toast,
        (ViewGroup) findViewById( R.id.toast_layout ) );

    TextView textV = (TextView) layout.findViewById( R.id.toastText );
    textV.setText( text );

    Toast toast = new Toast( getApplicationContext() );
    toast.setDuration( Toast.LENGTH_LONG );
    toast.setView( layout );
    toast.show();

  }

  @Override
  public void onDateChanged( DatePicker view, int year, int monthOfYear,
      int dayOfMonth )
  { 
    Calendar c = Calendar.getInstance();
    c.set( year, monthOfYear, dayOfMonth );
    setDateOfBirthday( c.getTime() );
  }
}
