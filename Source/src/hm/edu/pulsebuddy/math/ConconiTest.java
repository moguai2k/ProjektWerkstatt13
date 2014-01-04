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
   * @param pulse (all measured pulse data)
   * @return (deflection point)
   */
  public int getDeflectionPoint( int[] pulse )
  {
    int maxPulse = 0;
    
    for( int i=0; i < pulse.length; i++ ) {
      double rate = ( pulse[i+1] - pulse[i] ) *100 / pulse[i];
      int rateIndex = i;
      
      if(pulse[i] > maxPulse)
        maxPulse = pulse[i];
      
      if(i>2 && rate < 1.0)
        return pulse[rateIndex];
    }
    if(maxPulse > 179) //if pulse 180 or higher - better catch in activity?
      return getDeflectionPointFinally( pulse );
    else 
      return 0;
  }
  
  /**
   * 
   * @param pulse (all measured pulse data)
   * @return (deflection point finally)
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
  
  /**
   * 
   * @param pulse (last 3 pulse [190m 195m, 200m])
   * @return (correct pulse, average)
   */
  public int getCorrectPulesToStore( int[] pulse )
  {
    int correctPulse = 0;
    
    for( int i=0; i < pulse.length; i++ ) {
      correctPulse += pulse[i];
    }
    
    return correctPulse / pulse.length;
  }

}
