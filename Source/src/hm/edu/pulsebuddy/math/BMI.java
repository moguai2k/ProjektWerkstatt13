package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.Gender;

public class BMI implements Math
{

  private int userWeight; // kg
  private int userHeight; // cm
  private Gender userGender;
  private int bmiWDescription;
  private int bmiDDescription;
  private int bmiWColor;
  private int bmiDColor;
  private double bmiValue;

  public void startBMICalculation( int userWeight, int userHeight,
      Gender userGender )
  {
    // WHO
    bmiValue = calculateBMI( userWeight, userHeight );
    bmiWColor = colorBMI( bmiValue );
    bmiWDescription = interpretWHOBMI( bmiValue );

    // DGE
    bmiDColor = colorBMI( bmiValue );
    bmiDDescription = interpretDGEBMI( bmiValue, userGender );
  }

  private int interpretWHOBMI( double bmiValue2 )
  {
    if ( bmiValue2 < 16 )
    {
      return R.string.bmiSUnder;
    }
    else if ( bmiValue2 < 18.5 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue2 < 25 )
    {
      return R.string.bmiNormal;
    }
    else if ( bmiValue2 < 30 )
    {
      return R.string.bmiOver;
    }
    else if ( bmiValue2 < 40 )
    {
      return R.string.bmiSOver;
    }
    else
    {
      return R.string.bmiObese;
    }
  }

  private int interpretDGEBMIForM( double bmiValue2 )
  {
    if ( bmiValue2 < 20 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue2 < 24.99 )
    {
      return R.string.bmiNormal;
    }
    else
    {
      return interpretWHOBMI( bmiValue2 );
    }
  }

  private int interpretDGEBMIForW( double bmiValue2 )
  {
    if ( bmiValue2 < 19 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue2 < 23.99 )
    {
      return R.string.bmiNormal;
    }
    else
    {
      return interpretWHOBMI( bmiValue2 );
    }
  }

  private int interpretDGEBMI( double bmiValue2, Gender gender )
  {
    if ( gender.equals( Gender.male ) )
      return interpretDGEBMIForM( bmiValue2 );
    else if ( gender.equals( Gender.female ) )
      return interpretDGEBMIForW( bmiValue2 );
    else
      return 0;
  }

  private int colorBMI( double bmiValue2 )
  {
    if ( bmiValue2 < 16 )
    {
      return R.color.red;
    }
    else if ( bmiValue2 < 18.5 )
    {
      return R.color.yellow;
    }
    else if ( bmiValue2 < 25 )
    {
      return R.color.green;
    }
    else if ( bmiValue2 < 30 )
    {
      return R.color.yellow;
    }
    else
    {
      return R.color.red;
    }
  }

  // check for http://en.wikipedia.org/wiki/Body_mass_index
  private float calculateBMI( int weight, int height )
  {
    return (float) ( weight / ( height * height ) );
  }

  private void setUserWeight( int userWeight )
  {
    this.userWeight = userWeight;
  }

  private void setUserHeight( int userHeight )
  {
    this.userHeight = userHeight;
  }

  public int getBmiWDescription()
  {
    return bmiWDescription;
  }

  public int getBmiDDescription()
  {
    return bmiDDescription;
  }

  public int getBmiWColor()
  {
    return bmiWColor;
  }

  public int getBmiDColor()
  {
    return bmiDColor;
  }

  public double getBmiValue()
  {
    return bmiValue;
  }
}
