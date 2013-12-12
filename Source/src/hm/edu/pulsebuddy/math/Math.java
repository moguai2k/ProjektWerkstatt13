package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.common.Gender;

public interface Math
{
  public void startBMICalculation( int userWeight, int userHeight,
      Gender userGender );

  public int getBmiWDescription();

  public int getBmiDDescription();

  public int getBmiWColor();

  public int getBmiDColor();

  public double getBmiValue();
}
