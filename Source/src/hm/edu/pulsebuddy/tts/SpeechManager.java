package hm.edu.pulsebuddy.tts;

import android.content.Context;

public class SpeechManager {
	private static SpeechOutput spInstance;

	public SpeechOutput getSpeechInstance(Context context) {
		if (spInstance == null) {
			spInstance = new SpeechOutput(context);
		}
		return spInstance;
	}

}
