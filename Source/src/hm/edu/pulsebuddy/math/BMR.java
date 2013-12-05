package hm.edu.pulsebuddy.math;

public class BMR
{

  private float BMR;

  private int weight; // kg
  private int height; // cm
  private int age;
  private String gender;

  public BMR()
  {
    weight = 1; // PERST.gimmeUserWeight();
    height = 1; // PERST.gimmeUserHeight();
    age = 20; // PERST.gimmeUserAge();
    calculateBRM( weight, height, age );
  }

  private float calculateBRM( int weight, int height, int age )
  {
    if ( gender == "m" )
      return 66 + ( 6.3F * weight ) + ( 12.9F * height ) - ( 6.8F * age );
    else if ( gender == "w" )
      return 655 + ( 4.3F * weight ) + ( 4.7F * height ) - ( 4.7F * age );
    else
      return 0;
  }

  public float getBMR()
  {
    return BMR;
  }
}
