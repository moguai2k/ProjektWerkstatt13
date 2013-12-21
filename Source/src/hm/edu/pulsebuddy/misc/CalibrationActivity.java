package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.DatePickerFragment;
import hm.edu.pulsebuddy.common.DatePickerFragment.DateListener;
import hm.edu.pulsebuddy.common.Gender;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class CalibrationActivity extends FragmentActivity implements
    DateListener
{

  private int userHeight;
  private int userWeight;
  private Gender userGender;
  private Calendar userDateOfBirthday;

  private DataInterface di;
  private UserModel user;

  final private String dateOfBirthdayTag = "dateOfBirthday";

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.calibration );

    di = DataManager.getDataInterface();
    user = di.getUserInstance();

    // Value's are existing values from database or default value's
    // weight
    userWeight = user.getWeight();

    // height
    userHeight = user.getHeight();

    // gender
    userGender = user.getGender();

    // dateOfBirthday
    userDateOfBirthday = Calendar.getInstance();
    userDateOfBirthday.setTime( user.getBirthday() );

    getActionBar().setDisplayHomeAsUpEnabled( true );
    saveCalibrationValues();
    setUserHeigt();
    setUserWeight();
    setUserGender();
    setUserDateOfBirthday();
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

  /**
   * Update the button with the stored value and save a new value if a new one
   * is selected.
   * 
   */
  private void setUserHeigt()
  {
    Button buttonUserHeight = (Button) findViewById( R.id.buttonUserHeight );

    buttonUserHeight.setText( userHeight + "" );

    buttonUserHeight.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserHeightPicker();
      }
    } );
  }

  /**
   * Update the button with the stored value and save a new value if a new one
   * is selected.
   * 
   */
  private void setUserWeight()
  {
    Button buttonUserWeight = (Button) findViewById( R.id.buttonUserWeight );

    buttonUserWeight.setText( userWeight + "" );

    buttonUserWeight.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserWeightPicker();
      }
    } );
  }

  /**
   * Update the button with the stored value and save a new value if a new one
   * is selected.
   * 
   */
  private void setUserGender()
  {
    Button buttonUserGender = (Button) findViewById( R.id.buttonUserGender );

    buttonUserGender.setText( calculateUserGender( userGender ) );

    buttonUserGender.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserGenderPicker();
      }
    } );
  }

  /**
   * Update the button with the stored value and save a new value if a new one
   * is selected.
   * 
   */
  private void setUserDateOfBirthday()
  {
    Button buttonUserDateOfBirthday = (Button) findViewById( R.id.buttonDateOfBirthday );

    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy",
        java.util.Locale.getDefault() );
    dateFormat.setCalendar( userDateOfBirthday );
    String selectedDate = dateFormat.format( userDateOfBirthday.getTime() );
    buttonUserDateOfBirthday.setText( selectedDate + "" );

    buttonUserDateOfBirthday.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showDatePickerDialogForDateOfBirthday();
      }
    } );
  }

  /**
   * Open the picker to select a new value.
   */
  private void showUserHeightPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Größe" );
    dialog.setContentView( R.layout.height_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerHeight );

    numberPicker.setMinValue( 110 );
    numberPicker.setMaxValue( 260 );
    numberPicker.setValue( userHeight );
    numberPicker.setWrapSelectorWheel( false );
    numberPicker
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker, int oldPickerValue,
              int newPickerValue )
          {
            setUserHeight( newPickerValue );
          }
        } );

    buttonFinished.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Button buttonUserHeight = (Button) findViewById( R.id.buttonUserHeight );
        buttonUserHeight.setText( String.valueOf( numberPicker.getValue() ) );
        dialog.dismiss();
      }
    } );
    dialog.show();

  }

  /**
   * Open the picker to select a new value.
   */
  private void showUserWeightPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Gewicht" );
    dialog.setContentView( R.layout.weight_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerGender );

    numberPicker.setMinValue( 30 );
    numberPicker.setMaxValue( 300 );
    numberPicker.setValue( userWeight );
    numberPicker.setWrapSelectorWheel( false );
    numberPicker
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker, int oldPickerValue,
              int newPickerValue )
          {
            setUserWeight( newPickerValue );
          }
        } );

    buttonFinished.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Button buttonUserWeight = (Button) findViewById( R.id.buttonUserWeight );
        userWeight = numberPicker.getValue();
        buttonUserWeight.setText( userWeight + "" );
        dialog.dismiss();
      }
    } );

    dialog.show();

  }

  /**
   * Open the picker to select a new value.
   */
  private void showUserGenderPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Geschlecht" );
    dialog.setContentView( R.layout.gender_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerGender );

    numberPicker.setMinValue( 1 );
    numberPicker.setMaxValue( 2 );

    if ( Gender.female.equals( userGender ) )
    {
      numberPicker.setValue( 1 );
    }
    else if ( Gender.female.equals( userGender ) )
    {
      numberPicker.setValue( 2 );
    }

    String[] genders = { "weiblich", "männlich" };
    numberPicker.setWrapSelectorWheel( false );
    numberPicker.setDisplayedValues( genders );
    numberPicker
        .setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
        {
          @Override
          public void onValueChange( NumberPicker picker, int oldPickerValue,
              int newPickerValue )
          {
            setUserGender( calclulateUserGender( newPickerValue ) );
          }
        } );

    buttonFinished.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Button buttonUserGender = (Button) findViewById( R.id.buttonUserGender );

        if ( numberPicker.getValue() == 1 )
        {
          buttonUserGender.setText( "weiblich" );
        }
        else if ( numberPicker.getValue() == 2 )
        {
          buttonUserGender.setText( "männlich" );
        }

        dialog.dismiss();

      }
    } );
    dialog.show();
  }

  /**
   * Open the picker to select a new value.
   */
  private void showDatePickerDialogForDateOfBirthday()
  {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show( getSupportFragmentManager(), dateOfBirthdayTag );
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
    return null;

  }

  /**
   * Convert the stored value from enum to string.
   * 
   * @param userGender
   * @return
   */
  private String calculateUserGender( Gender userGender )
  {
    if ( Gender.female.equals( userGender ) )
    {
      return "weiblich";
    }
    else if ( Gender.male.equals( userGender ) )
    {
      return "männlich";
    }
    return null;
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
        user.setBirthday( userDateOfBirthday.getTime() );
        di.savaUserInstance( user );

        customToast( getResources().getString(
            R.string.calibration_saved_values ) );
      }
    } );
  }

  /**
   * Get date of birthday from picker.
   * 
   * @param tag
   *          Required tag to distinguish different date picker.
   * @param calendar
   *          The current calendar from date picker.
   */
  @Override
  public void returnDate( String tag, Calendar calendar )
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy",
        java.util.Locale.getDefault() );
    dateFormat.setCalendar( calendar );
    String selectedDate = dateFormat.format( calendar.getTime() );

    setUserDateOfBirthday( calendar );

    if ( dateOfBirthdayTag.equals( tag ) )
    {
      Button startDateButton = (Button) findViewById( R.id.buttonDateOfBirthday );
      startDateButton.setText( selectedDate );
    }
  }

  /**
   * Notifies the user that the data has been stored in the database.
   * 
   * @param text
   *          The message.
   */
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

  // setters and getters
  private void setUserWeight( int userWeight )
  {
    this.userWeight = userWeight;
  }

  private void setUserHeight( int userHeight )
  {
    this.userHeight = userHeight;
  }

  private void setUserGender( Gender userGender )
  {
    this.userGender = userGender;
  }

  private void setUserDateOfBirthday( Calendar userDateOfBirthday )
  {
    this.userDateOfBirthday = userDateOfBirthday;
  }
}
