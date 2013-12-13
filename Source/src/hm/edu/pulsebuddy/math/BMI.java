package hm.edu.pulsebuddy.math;

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
	// check for http://en.wikipedia.org/wiki/Body_mass_index
	private static double getBMI(int weight, int height) {
		return (weight / (height * height));
	}

	private static int getDGEDescriptionForBMI(double bmiValue, Gender gender) {
		if (gender.equals(Gender.male))
			return getDGEDescriptionForBMIForM(bmiValue);
		else if (gender.equals(Gender.female))
			return getDGEDescriptionForBMIForF(bmiValue);
		else
			return 0;
	}

	private static int getColorForBMI(double bmiValue) {
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

	private static int getWHODescriptionForBMI(double bmiValue) {
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

	private static int getDGEDescriptionForBMIForM(double bmiValue) {
		if (bmiValue < 20) {
			return R.string.bmiUnder;
		} else if (bmiValue < 24.99) {
			return R.string.bmiNormal;
		} else {
			return getWHODescriptionForBMI(bmiValue);
		}
	}

	private static int getDGEDescriptionForBMIForF(double bmiValue) {
		if (bmiValue < 19) {
			return R.string.bmiUnder;
		} else if (bmiValue < 23.99) {
			return R.string.bmiNormal;
		} else {
			return getWHODescriptionForBMI(bmiValue);
		}
	}
}
