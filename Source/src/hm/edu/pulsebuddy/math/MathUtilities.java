package hm.edu.pulsebuddy.math;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MathUtilities {

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getAge(Date date) {
		GregorianCalendar birthd = new GregorianCalendar();
		birthd.setTime(date);

		GregorianCalendar today = new GregorianCalendar();

		int year = today.get(Calendar.YEAR) - birthd.get(Calendar.YEAR);

		if (today.get(Calendar.MONTH) <= birthd.get(Calendar.MONTH)) {
			if (today.get(Calendar.DATE) < birthd.get(Calendar.DATE)) {
				year -= 1;
			}
		}

		if (year < 0)
			throw new IllegalArgumentException("invalid age: " + year);

		return year;
	}

}
