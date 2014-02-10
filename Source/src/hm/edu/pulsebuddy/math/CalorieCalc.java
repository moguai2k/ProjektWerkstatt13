package hm.edu.pulsebuddy.math;

import android.util.Log;
import hm.edu.pulsebuddy.R;

public class CalorieCalc {

	public void addCalories(int calories) {
		// db put calories to today + timestamp
	}

	public void getCalories() {
		// db gimme calories all day long
	}

	public void caloriesTrafficLight(int food) {
		switch (food) {
		case 1:
			addCalories(100);
			break;
		case 2:
			addCalories(250);
			break;
		case 3:
			addCalories(500);
			break;
		case 4:
			addCalories(750);
			break;
		case 5:
			addCalories(1000);
			break;
		}
	}

	public static int getColorForCalories(double bmr, double current) {
		if (bmr + 750 < current) {
			return R.color.red;
		} else if (bmr > (double) current) {
			return R.color.green;
		} else {
			return R.color.yellow;
		}
	}
}
