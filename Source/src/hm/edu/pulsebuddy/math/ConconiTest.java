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
  public int getStandardMaxHeartRate( int age, Gender gender )
  {
    if ( gender.equals( Gender.male ) )
      return 220 - age;
    else if ( gender.equals( Gender.female ) )
      return 226 - age;
    else
      return 0; // error
  }
  
  /**
   * 
   * @param pulse
   * @return
   */
  public int getDeflectionPoint( int[] pulse )
  {    
    for( int i=0; i < pulse.length; i++ ) {
      double rate = ( pulse[i+1] - pulse[i] ) *100 / pulse[i];
      int rateIndex = i;
      
      if(i>2 && rate < 1.0)
        return pulse[rateIndex];
    }
    return 0;
  }
  
  /**
   * 
   * @param pulse
   * @return
   */
  public int getDeflectionPointFinally( int[] pulse )
  {
    double rate = 0.0;
    int rateIndex = 0;
    
    for( int i=0; i < pulse.length; i++ ) {
      double newRate = ( pulse[i+1] - pulse[i] ) *100 / pulse[i];
      if(i==0)
        rate = newRate;
      else if(newRate < rate) {
        rate = newRate;
        rateIndex = i;
      }
    }
    return pulse[rateIndex];
  }

}
