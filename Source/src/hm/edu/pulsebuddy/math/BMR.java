package hm.edu.pulsebuddy.math;

public class BMR {

    public void getBMR(double weight, double height, double BMRw, double BMRm, int age)
    { 
        weight = 1.0; 
        height = 1.0; 
        age = 20; 

        BMRw = 655 + (4.3 * weight) + (4.7 * height) - (4.7 * age); 
        BMRm = 66 + (6.3 * weight) + (12.9 * height) - (6.8 * age); 
   } 
	
}
