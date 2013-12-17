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
public class CalibrationActivity extends FragmentActivity implements
    OnDateChangedListener, DateListener
{
  private Calendar dateOfBirthday;
  private int userHeight;
  private int userWeight;
  private Gender userGender;

  public Gender getUserGender()
  {
    return userGender;
  }

  private DataInterface di;
  private UserModel user;
  final String dateOfBirthdayTag = "dateOfBirthday";

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.calibration7 );

    di = DataManager.getDataInterface();
    user = di.getUserInstance();

    getActionBar().setDisplayHomeAsUpEnabled( true );

    saveCalibrationValues();

    setUserHeigt();
    setUserWeight();
    setUserGender();

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

  private void setUserHeigt()
  {
    Button buttonUserHeight = (Button) findViewById( R.id.buttonUserHeight );
    buttonUserHeight.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserHeightPicker();
      }
    } );
  }

  private void setUserWeight()
  {
    Button buttonUserWeight = (Button) findViewById( R.id.buttonUserWeight );
    buttonUserWeight.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserWeightPicker();
      }
    } );
  }

  private void setUserGender()
  {
    Button buttonUserGender = (Button) findViewById( R.id.buttonUserGender );
    buttonUserGender.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        showUserGenderPicker();
      }
    } );
  }

  public void showUserHeightPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Größe" );
    dialog.setContentView( R.layout.height_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerHeight );

    numberPicker.setMinValue( 110 );
    numberPicker.setMaxValue( 260 );
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

  public void showUserWeightPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Gewicht" );
    dialog.setContentView( R.layout.weight_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerGender );

    numberPicker.setMinValue( 30 );
    numberPicker.setMaxValue( 300 );
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
        buttonUserWeight.setText( String.valueOf( numberPicker.getValue() ) );
        dialog.dismiss();
      }
    } );

    dialog.show();

  }

  public void showUserGenderPicker()
  {
    final Dialog dialog = new Dialog( CalibrationActivity.this );
    dialog.setTitle( "Wähle Dein Geschlecht" );
    dialog.setContentView( R.layout.gender_picker_dialog );
    Button buttonFinished = (Button) dialog.findViewById( R.id.buttonFinished );

    final NumberPicker numberPicker = (NumberPicker) dialog
        .findViewById( R.id.numberPickerGender );

    numberPicker.setMinValue( 1 );
    numberPicker.setMaxValue( 2 );
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

  // show date picker dialog if button is fired
  public void showDatePickerDialogForDateOfBirthday( View v )
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

  public void setDateOfBirthday( Calendar dateOfBirthday )
  {
    this.dateOfBirthday = dateOfBirthday;
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
        // TODO @Tore: ich musste den Typ auf Calendar ändern, bitte anpassen
        // user.setBirthday( dateOfBirthday );

        // TODO @Tore noch mal prüfen ob die Werte richtig in die DB übernomnen
        // werden

        di.savaUserInstance( user );

        customToast( R.string.calibration_saved_values + "" );
      }
    } );
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
  public void returnDate( String tag, Calendar calendar )
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy" );
    dateFormat.setCalendar( calendar );
    String selectedDate = dateFormat.format( calendar.getTime() );

    setDateOfBirthday( calendar );

    if ( dateOfBirthdayTag.equals( tag ) )
    {
      Button startDateButton = (Button) findViewById( R.id.buttonDateOfBirthday );
      startDateButton.setText( selectedDate );

      // TODO Hier Code einfügen, Startdatum für Graph
    }
  }

  @Override
  public void onDateChanged( DatePicker view, int year, int monthOfYear,
      int dayOfMonth )
  {
    // TODO Auto-generated method stub

  }
}
