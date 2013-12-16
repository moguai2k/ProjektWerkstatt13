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
    double weight2 = weight;
    double height2 = height;
    double age2 = age;
    
    if ( gender.equals( Gender.male ) )
      return (double) Math.round(  ( 66 +  13.7 * weight2 + 5.0 * height2 - 6.8 * age2 ) *10 ) /10 ;
    else if ( gender.equals( Gender.female ) )
      return (double) Math.round(  ( 655 + 9.6 * weight2 +  1.8 * height2 - 4.7 * age ) *10 ) /10 ;
    else
      return 0;
  }

  public static double getBMRE( double bmr, double energy )
  {
    return (double) Math.round( energy * bmr * 10 ) / 10;
  }
}
