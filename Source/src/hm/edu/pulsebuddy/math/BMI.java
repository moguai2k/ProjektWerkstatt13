package hm.edu.pulsebuddy.math;

import hm.edu.pulsebuddy.R;

public class BMI {
	
	//weight = kg
	//height = cm
	public void whoBMI(float weight, float height) {
		
		//ini
		//TextView bmiValueText;
		//TextView bmiDescriptionText;
		//bmiValueText = (TextView)findViewById(R.id.textViewX);
		//bmiDescriptionText = (TextView)findViewById(R.id.textViewY);
		
		float bmiValue = calculateBMI(weight, height);

		int bmiInterpretationW = interpretWHOBMI(bmiValue);
		//bmiDescriptionText.setText(getResources().getString(bmiInterpretationW));
		
		int bmiColorW = colorBMI(bmiValue);
		//bmiValueText.setTextColor(getResources().getColor(bmiColorW));
		//bmiDescriptionText.setTextColor(getResources().getColor(bmiColorW));
	}
	
	public void dgeBMI(float weight, float height, String gender) {
		
		//ini
		//TextView bmiValueText;
		//TextView bmiDescriptionText;
		//bmiValueText = (TextView)findViewById(R.id.textViewX);
		//bmiDescriptionText = (TextView)findViewById(R.id.textViewY);
		
		float bmiValue = calculateBMI(weight, height);
		
		//DGE
		int bmiInterpretationD;
		if(gender=="m")
			bmiInterpretationD = interpretDGEBMIForM(bmiValue);
		else
			bmiInterpretationD = interpretDGEBMIForW(bmiValue);
		//bmiDescriptionText.setText(getResources().getString(bmiInterpretationD));
		
		int bmiColorD = colorBMI(bmiValue);
		//bmiValueText.setTextColor(getResources().getColor(bmiColorD));
		//bmiDescriptionText.setTextColor(getResources().getColor(bmiColorD));
	}	
    
    private int interpretWHOBMI(float bmiValue) {
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
    
    private int interpretDGEBMIForM(float bmiValue) {
    	if (bmiValue < 20) {
    		return R.string.bmiUnder;
    	} else if (bmiValue < 24.99) {
    		return R.string.bmiNormal;
		} else {
			return interpretWHOBMI(bmiValue);
		}
    }
    
    private int interpretDGEBMIForW(float bmiValue) {
    	if (bmiValue < 19) {
    		return R.string.bmiUnder;
    	} else if (bmiValue < 23.99) {
    		return R.string.bmiNormal;
		} else {
			return interpretWHOBMI(bmiValue);
		}
    }
		
    private int colorBMI(float bmiValue) {
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
        
    // check for http://en.wikipedia.org/wiki/Body_mass_index
    private float calculateBMI (float weight, float height) {
    	return (float) (weight / (height * height));
    }
}
