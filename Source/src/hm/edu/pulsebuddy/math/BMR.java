package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.common.Gender;

/**
 * How to use
 * 
 * getBRM( weight, height, age, Gender gender )
 * 
 */

public class BMR
{
  /**
   * 
   * @param weight
   * @param height
   * @param age
   * @param gender
   * @return
   */
  public static double getBMR( int weight, int height, int age, Gender gender )
  {
    if ( gender.equals( Gender.male ) )
      return 66 + ( 13.7 * weight ) + ( 5 * height ) - ( 6.8 * age );
    else if ( gender.equals( Gender.female ) )
      return 655 + ( 9.6 * weight ) + ( 1.8 * height ) - ( 4.7 * age );
    else
      return 0;
  }

  public static double getBMRE( double bmr, double energy )
  {
    return Math.round( energy * bmr * 10 ) / 10;
  }
}
