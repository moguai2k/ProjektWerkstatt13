package hm.edu.pulsebuddy.math;

import android.util.Log;
import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.Gender;

/**
 * How to use
 * 
 * Common 
 * ------
 * getBMI( userWeight, userHeight ) 
 * getColorForBMI( bmiValue )
 * 
 * WHO-BMI 
 * ------
 * getWHODescriptionForBMI( bmiValue )
 * 
 * DGI-BMI
 * ------ 
 * getDGEDescriptionForBMI( bmiValue, userGender )
 */

public class BMI {
	/**
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public static double getBMI(int weight, int height) {
		//height usually in cm, BMI needs meter, so /100 /100 => /10000
		// check for http://en.wikipedia.org/wiki/Body_mass_index
		return (double) Math.round( (double) ( weight / (double) ( ( height * height) / 10000 ) ) *10 ) / 10;
	}

	public static int getDGEDescriptionForBMI(double bmiValue, Gender gender) {
		if (gender.equals(Gender.male))
			return getDGEDescriptionForBMIForM(bmiValue);
		else if (gender.equals(Gender.female))
			return getDGEDescriptionForBMIForF(bmiValue);
		else
			return 0;
	}

	/**
	 * 
	 * @param bmiValue
	 * @return
	 */
	public static int getColorForBMI(double bmiValue) {
		if (bmiValue < 16) {
			return R.color.red;
		} else if (bmiValue < 18.5) {
			return R.color.yellow;
		} else if (bmiValue < 25) {
			return R.color.green;
		} else if (bmiValue < 30) {
			return R.color.yellow;
		} else {
			return R.color.red;
		}
	}

	/**
	 * 
	 * @param bmiValue
	 * @return
	 */
	public static int getWHODescriptionForBMI(double bmiValue) {
		if (bmiValue < 16) {
			return R.string.bmiSUnder;
		} else if (bmiValue < 18.5) {
			return R.string.bmiUnder;
		} else if (bmiValue < 25) {
			return R.string.bmiNormal;
		} else if (bmiValue < 30) {
			return R.string.bmiOver;
		} else if (bmiValue < 40) {
			return R.string.bmiSOver;
		} else {
			return R.string.bmiObese;
		}
	}

	/**
	 * 
	 * @param bmiValue
	 * @return
	 */
	public static int getDGEDescriptionForBMIForM(double bmiValue) {
		if (bmiValue < 20) {
			return R.string.bmiUnder;
		} else if (bmiValue < 24.99) {
			return R.string.bmiNormal;
		} else {
			return getWHODescriptionForBMI(bmiValue);
		}
	}

	/**
	 * 
	 * @param bmiValue
	 * @return
	 */
	public static int getDGEDescriptionForBMIForF(double bmiValue) {
		if (bmiValue < 19) {
			return R.string.bmiUnder;
		} else if (bmiValue < 23.99) {
			return R.string.bmiNormal;
		} else {
			return getWHODescriptionForBMI(bmiValue);
		}
	}
}
