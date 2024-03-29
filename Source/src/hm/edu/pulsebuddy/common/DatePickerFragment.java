package hm.edu.pulsebuddy.common;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	DateListener dateListener;

	public interface DateListener {
		public void returnDate(String tag, Calendar calendar);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the default date in the picker
		final Calendar defaultDate = Calendar.getInstance();
		defaultDate.set(1985, Calendar.JANUARY, 1);
		int year = defaultDate.get(Calendar.YEAR);
		int month = defaultDate.get(Calendar.MONTH);
		int day = defaultDate.get(Calendar.DAY_OF_MONTH);
		dateListener = (DateListener) getActivity();

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// set tags
		final String startDateTag = "startDateTag";
		final String endDateTag = "endDateTag";
		final String dateOfBirthdayTag = "dateOfBirthday";

		// set selected date
		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		// tag is set in show and separate each date picker
		String tag = getTag();

		if (tag.equals(startDateTag)) {
			dateListener.returnDate(startDateTag, calendar);
		}

		else if (tag.equals(endDateTag)) {
			dateListener.returnDate(endDateTag, calendar);
		}

		else if (tag.equals(dateOfBirthdayTag)) {
			dateListener.returnDate(dateOfBirthdayTag, calendar);
		}

	}
}