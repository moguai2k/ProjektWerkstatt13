package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.common.Gender;

public class ConconiTest
{
  /**
   * 
   * @param age
   * @param gender
   * @return
   */
  public int getStandardHeartRate( int age, Gender gender )
  {
    if ( gender.equals( Gender.male ) )
      return 220 - age;
    else if ( gender.equals( Gender.female ) )
      return 226 - age;
    else
      return 0; // error
  }

}
