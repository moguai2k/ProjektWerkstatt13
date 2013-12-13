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
  private float getBRM( int weight, int height, int age, Gender gender )
  {
    if ( gender.equals(Gender.male) )
      return 66 + ( 6.3F * weight ) + ( 12.9F * height ) - ( 6.8F * age );
    else if ( gender.equals(Gender.female) )
      return 655 + ( 4.3F * weight ) + ( 4.7F * height ) - ( 4.7F * age );
    else
      return 0;
  }
}
