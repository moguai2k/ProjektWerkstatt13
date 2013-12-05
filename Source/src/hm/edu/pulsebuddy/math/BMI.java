package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.R;

public class BMI
{

  private int userWeight; // kg
  private int userHeight; // cm
  private int bmiWDescription;
  private int bmiDDescription;
  private int bmiWColor;
  private int bmiDColor;
  private float bmiValue;
  private String gender;

  public BMI()
  {
    userWeight = 1; // PERST.gimmeUserWeight();
    userWeight = 1; // PERST.gimmeUserHeight();
    gender = "m";

    // WHO
    bmiValue = calculateBMI( userWeight, userHeight );
    bmiWColor = colorBMI( bmiValue );
    bmiWDescription = interpretWHOBMI( bmiValue );

    // DGE
    bmiDColor = colorBMI( bmiValue );
    bmiDDescription = interpretDGEBMI( bmiValue );
  }

  private int interpretWHOBMI( float bmiValue )
  {
    if ( bmiValue < 16 )
    {
      return R.string.bmiSUnder;
    }
    else if ( bmiValue < 18.5 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue < 25 )
    {
      return R.string.bmiNormal;
    }
    else if ( bmiValue < 30 )
    {
      return R.string.bmiOver;
    }
    else if ( bmiValue < 40 )
    {
      return R.string.bmiSOver;
    }
    else
    {
      return R.string.bmiObese;
    }
  }

  private int interpretDGEBMIForM( float bmiValue )
  {
    if ( bmiValue < 20 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue < 24.99 )
    {
      return R.string.bmiNormal;
    }
    else
    {
      return interpretWHOBMI( bmiValue );
    }
  }

  private int interpretDGEBMIForW( float bmiValue )
  {
    if ( bmiValue < 19 )
    {
      return R.string.bmiUnder;
    }
    else if ( bmiValue < 23.99 )
    {
      return R.string.bmiNormal;
    }
    else
    {
      return interpretWHOBMI( bmiValue );
    }
  }

  private int interpretDGEBMI( float bmiValue )
  {
    if ( gender == "m" )
      return interpretDGEBMIForM( bmiValue );
    else if ( gender == "w" )
      return interpretDGEBMIForW( bmiValue );
    else
      return 0;
  }

  private int colorBMI( float bmiValue )
  {
    if ( bmiValue < 16 )
    {
      return R.color.red;
    }
    else if ( bmiValue < 18.5 )
    {
      return R.color.yellow;
    }
    else if ( bmiValue < 25 )
    {
      return R.color.green;
    }
    else if ( bmiValue < 30 )
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

  public float getBmiValue()
  {
    return bmiValue;
  }
}
