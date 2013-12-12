package hm.edu.pulsebuddy.misc;

import java.util.Date;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.Gender;
import hm.edu.pulsebuddy.math.BMI;
import hm.edu.pulsebuddy.math.Math;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * The calibration collects user information like date of birthday, height,
 * weight and gender. This user information are needed for BMI, BMR, ...
 * 
 * @author josefbichlmeier
 * 
 */
public class CalibrationActivity extends Activity
{
  private Date dateOfBirthday;
  private int userHeight;
  private int userWeight;
  private Gender userGender;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.calibration05 );
    getActionBar().setDisplayHomeAsUpEnabled( true );
    setValuesUserHeight();
    setValuesUserWeight();
    setValuesUserGender();
    setSwitchToBMICalculation();
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
   * Set the number picker for user height in calibration.
   */
  private void setValuesUserHeight()
  {
    NumberPicker numberPickerUserHeight = (NumberPicker) findViewById( R.id.numberPickerUserHeight );
    // TODO mit for schleife lösen
    String[] userHeights = { "1,00 m", "1,01 m", "1,02 m", "1,03 m", "1,04 m",
        "1,05 m", "1,06 m", "1,07 m", "1,08 m", "1,09 m", "1,10 m", "1,11 m",
        "1,12 m", "1,13 m", "1,14 m", "1,15 m", "1,16 m", "1,17 m", "1,18 m",
        "1,19 m", "1,20 m", "1,21 m", "1,22 m", "1,23 m", "1,24 m", "1,25 m",
        "1,26 m", "1,27 m", "1,28 m", "1,29 m", "1,30 m", "1,31 m", "1,32 m",
        "1,33 m", "1,34 m", "1,35 m", "1,36 m", "1,37 m", "1,38 m", "1,39 m",
        "1,40 m", "1,41 m", "1,42 m", "1,43 m", "1,44 m", "1,45 m", "1,46 m",
        "1,47 m", "1,48 m", "1,49 m", "1,50 m", "1,51 m", "1,52 m", "1,53 m",
        "1,54 m", "1,55 m", "1,56 m", "1,57 m", "1,58 m", "1,59 m", "1,60 m",
        "1,61 m", "1,62 m", "1,63 m", "1,64 m", "1,65 m", "1,66 m", "1,67 m",
        "1,68 m", "1,69 m", "1,70 m", "1,71 m", "1,72 m", "1,73 m", "1,74 m",
        "1,75 m", "1,76 m", "1,77 m", "1,78 m", "1,79 m", "1,80 m", "1,81 m",
        "1,82 m", "1,83 m", "1,84 m", "1,85 m", "1,86 m", "1,87 m", "1,88 m",
        "1,89 m", "1,90 m", "1,91 m", "1,92 m", "1,93 m", "1,94 m", "1,95 m",
        "1,96 m", "1,97 m", "1,98 m", "1,99 m", "2,00 m", "2,01 m", "2,02 m",
        "2,03 m", "2,04 m", "2,05 m", "2,06 m", "2,07 m", "2,08 m", "2,09 m",
        "2,10 m", "2,11 m", "2,12 m", "2,13 m", "2,14 m", "2,15 m", "2,16 m",
        "2,17 m", "2,18 m", "2,19 m", "2,20 m", "2,21 m", "2,22 m", "2,23 m",
        "2,24 m", "2,25 m", "2,26 m", "2,27 m", "2,28 m", "2,29 m", "2,30 m",
        "2,31 m", "2,32 m", "2,33 m", "2,34 m", "2,35 m", "2,36 m", "2,37 m",
        "2,38 m", "2,39 m", "2,40 m", "2,41 m", "2,42 m", "2,43 m", "2,44 m",
        "2,45 m", "2,46 m", "2,47 m", "2,48 m", "2,49 m", "2,50 m", "2,51 m",
        "2,52 m", "2,53 m", "2,54 m", "2,55 m", "2,56 m", "2,57 m", "2,58 m",
        "2,59 m", "2,60 m" };

    int minUserHeight = 100;
    int maxUserHeight = 260;
    numberPickerUserHeight.setMinValue( minUserHeight );
    numberPickerUserHeight.setMaxValue( maxUserHeight );
    numberPickerUserHeight.setWrapSelectorWheel( false );
    numberPickerUserHeight.setDisplayedValues( userHeights );
    numberPickerUserHeight
        .setDescendantFocusability( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

    getSelectedUserHeight( numberPickerUserHeight );
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

    // TODO mit for schleife lösen
    String[] userWeights = { "30 kg", "31 kg", "32 kg", "33 kg", "34 kg",
        "35 kg", "36 kg", "37 kg", "38 kg", "39 kg", "40 kg", "41 kg", "42 kg",
        "43 kg", "44 kg", "45 kg", "46 kg", "47 kg", "48 kg", "49 kg", "50 kg",
        "51 kg", "52 kg", "53 kg", "54 kg", "55 kg", "56 kg", "57 kg", "58 kg",
        "59 kg", "60 kg", "61 kg", "62 kg", "63 kg", "64 kg", "65 kg", "66 kg",
        "67 kg", "68 kg", "69 kg", "70 kg", "71 kg", "72 kg", "73 kg", "74 kg",
        "75 kg", "76 kg", "77 kg", "78 kg", "79 kg", "80 kg", "81 kg", "82 kg",
        "83 kg", "84 kg", "85 kg", "86 kg", "87 kg", "88 kg", "89 kg", "90 kg",
        "91 kg", "92 kg", "93 kg", "94 kg", "95 kg", "96 kg", "97 kg", "98 kg",
        "99 kg", "100 kg", "101 kg", "102 kg", "103 kg", "104 kg", "105 kg",
        "106 kg", "107 kg", "108 kg", "109 kg", "110 kg", "111 kg", "112 kg",
        "113 kg", "114 kg", "115 kg", "116 kg", "117 kg", "118 kg", "119 kg",
        "120 kg", "121 kg", "122 kg", "123 kg", "124 kg", "125 kg", "126 kg",
        "127 kg", "128 kg", "129 kg", "130 kg", "131 kg", "132 kg", "133 kg",
        "134 kg", "135 kg", "136 kg", "137 kg", "138 kg", "139 kg", "140 kg",
        "141 kg", "142 kg", "143 kg", "144 kg", "145 kg", "146 kg", "147 kg",
        "148 kg", "149 kg", "150 kg", "151 kg", "152 kg", "153 kg", "154 kg",
        "155 kg", "156 kg", "157 kg", "158 kg", "159 kg", "160 kg", "161 kg",
        "162 kg", "163 kg", "164 kg", "165 kg", "166 kg", "167 kg", "168 kg",
        "169 kg", "170 kg", "171 kg", "172 kg", "173 kg", "174 kg", "175 kg",
        "176 kg", "177 kg", "178 kg", "179 kg", "180 kg", "181 kg", "182 kg",
        "183 kg", "184 kg", "185 kg", "186 kg", "187 kg", "188 kg", "189 kg",
        "190 kg", "191 kg", "192 kg", "193 kg", "194 kg", "195 kg", "196 kg",
        "197 kg", "198 kg", "199 kg", "200 kg" };

    int minUserWeight = 30;
    int maxUserWeight = 200;
    numberPickerForWeight.setMinValue( minUserWeight );
    numberPickerForWeight.setMaxValue( maxUserWeight );
    numberPickerForWeight.setWrapSelectorWheel( false );
    numberPickerForWeight.setDisplayedValues( userWeights );
    numberPickerForWeight
        .setDescendantFocusability( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

    getSelectedUserWeight( numberPickerForWeight );
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
  private void setSwitchToBMICalculation()
  {
    Button button = (Button) findViewById( R.id.buttonBmiCalculation );
    button.setOnClickListener( new View.OnClickListener()
    {
      public void onClick( View v )
      {
        Math math = new BMI();
        math.startBMICalculation( userWeight, userHeight, userGender );
        Intent intent = new Intent( getApplicationContext(), BMIActivity.class );
        startActivity( intent );
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

}
