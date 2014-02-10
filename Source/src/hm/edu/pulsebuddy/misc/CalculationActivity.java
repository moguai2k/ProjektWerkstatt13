package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.UserModel;
import hm.edu.pulsebuddy.math.BMI;
import hm.edu.pulsebuddy.math.BMR;
import hm.edu.pulsebuddy.math.CalorieCalc;
import hm.edu.pulsebuddy.math.MathUtilities;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CalculationActivity extends Activity {
	private DataInterface di;
	private UserModel user;
	// TODO Josef, brauchen in Kalibrierung eine Angabe an Aktivit�t, siehe
	// Technisches Konzept Grundumsatzenergie
	// TODO Tore muss diese Activit�t in der DB speichern
	final static double ENERGY_REQUIREMENTS_AS_STUDENT = 1.3;

	ImageButton imageButtonAppetizer;
	ImageButton imageButtonMainCourse;
	ImageButton imageButtonDessert;

	int currentCalorie = 0;
	int stillLeftCalorie = 0;

	double bmr = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculation);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addListenerOnButton();

		di = DataManager.getDataInterface();
		user = di.getUserInstance();

		double bmr = BMR.getBMR(user.getWeight(), user.getHeight(),
				MathUtilities.getAge(user.getBirthday()), user.getGender());
		this.bmr = bmr;

		// setBasalMetabolismCalorie( bmr );
		setCurrentCalorie(0); // TODO Tore: user.getCalories brauchen wir hier
								// TODO Tore: speichern pro klick muss dann auch
								// ren

		double bmi = BMI.getBMI(user.getWeight(), user.getHeight());
		int bmiColor = BMI.getColorForBMI(bmi);
		int bmiDescriptionLikeWHO = BMI.getWHODescriptionForBMI(bmi);
		int bmiDescriptionLikeDGE = BMI.getDGEDescriptionForBMI(bmi,
				user.getGender()); // TODO Josef noch nen zweiten BMI einbauen
									// (Txtview)
									// BMI nach DGE mit diesem wert hier. Ty.

		double bmre = BMR.getBMRE(bmr, ENERGY_REQUIREMENTS_AS_STUDENT);

		setBMIValue(bmi);
		setBMIDescription(bmiDescriptionLikeWHO);
		setBMIColor(bmiColor);

		setBMRValue(bmr);
		setBMREValue(bmre);
	}

	/**
	 * Set the bmi value based on calibration user information.
	 */
	public void setBMIValue(double bmiValue) {
		TextView textView = (TextView) findViewById(R.id.textViewBMIValue);
		textView.setText(bmiValue + "");
	}

	/**
	 * Change the color of the bmi value.
	 * 
	 * @param bmiValue
	 */
	public void setBMIColor(int bmiColor) {
		TextView textView = (TextView) findViewById(R.id.textViewBMIValue);
		textView.setTextColor(getResources().getColor(bmiColor));
	}

	/**
	 * Set the bmi description based calculated bmi value.
	 */
	public void setBMIDescription(int bmiDescription) {
		TextView textView = (TextView) findViewById(R.id.textViewBMIRecommendation);
		textView.setText(bmiDescription);
	}

	/**
	 * Set the bmr value based on calibration user information.
	 */
	public void setBMRValue(double bmrValue) {
		TextView textView = (TextView) findViewById(R.id.textViewBMRResult);
		textView.setText(bmrValue + " kcal");
	}

	/**
	 * Set the bmre value based on calibration user information.
	 */
	public void setBMREValue(double bmre) {
		TextView textView = (TextView) findViewById(R.id.textViewBMREResult);
		textView.setText(bmre + " kcal");
	}

	public void addListenerOnButton() {

		imageButtonAppetizer = (ImageButton) findViewById(R.id.imageButtonAppetizer);
		imageButtonMainCourse = (ImageButton) findViewById(R.id.imageButtonMainCourse);
		imageButtonDessert = (ImageButton) findViewById(R.id.imageButtonDessert);

		imageButtonAppetizer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int appetizerCalorie = 100;

				int curentCalc = calculateCurrentCalorie(appetizerCalorie);
				setCurrentCalorie(curentCalc);
			}

		});

		imageButtonMainCourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int appetizerCalorie = 1000;

				int curentCalc = calculateCurrentCalorie(appetizerCalorie);
				setCurrentCalorie(curentCalc);
			}

		});

		imageButtonDessert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int appetizerCalorie = 300;

				int curentCalc = calculateCurrentCalorie(appetizerCalorie);
				setCurrentCalorie(curentCalc);
			}

		});
	}

	/*
	 * private void setBasalMetabolismCalorie( double basalMetabolismCalorie ) {
	 * TextView basalMetabolism = (TextView) findViewById(
	 * R.id.textViewBasalMetabolism ); basalMetabolism.setText(
	 * basalMetabolismCalorie + " kcal" ); }
	 */

	private void setCurrentCalorie(int currentCalorie) {
		int caloriesColor = CalorieCalc
				.getColorForCalories(bmr, currentCalorie);
		TextView currentValue = (TextView) findViewById(R.id.textViewCurrentValue);
		currentValue.setTextColor(getResources().getColor(caloriesColor));
		currentValue.setText(currentCalorie + " kcal");
	}

	private int calculateCurrentCalorie(int addCalorie) {
		currentCalorie = currentCalorie + addCalorie;

		return currentCalorie;
	}
}
