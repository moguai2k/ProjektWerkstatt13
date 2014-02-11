package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;

import java.util.Random;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class SportModeResultFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private View view;
	private View layout;

	private DataInterface di;

	public SportModeResultFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_sport_test_result, container,
				false);

		layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) view.findViewById(R.id.toast_layout));

		di = DataManager.getDataInterface();

		addData();

		return view;
	}

	// @Tore: Hier werte aus DB abfragen und in Tabelle eintragen
	private void addData() {
		TableLayout table = (TableLayout) view
				.findViewById(R.id.tableLayoutResult);

		// heading
		TableRow tableRowHeading = new TableRow(getActivity());

		TextView dateHeading = new TextView(getActivity());
		dateHeading.setText("Datum");
		dateHeading.setTypeface(null, Typeface.BOLD);
		dateHeading.setPadding(0, 0, 40, 20);
		dateHeading.setTextSize(18);

		TextView resultHeading = new TextView(getActivity());
		resultHeading.setText("Conconi-Ergebnis");
		resultHeading.setTypeface(null, Typeface.BOLD);
		resultHeading.setTextSize(18);

		tableRowHeading.addView(dateHeading);
		tableRowHeading.addView(resultHeading);

		table.addView(tableRowHeading);

		// values
		for (int i = 10; i <= 30; i++) {
			TableRow tableRowValue = new TableRow(getActivity());

			TextView dateValue = new TextView(getActivity());
			dateValue.setText(i + ".12.2013");
			dateValue.setPadding(0, 0, 40, 20);
			dateValue.setTextSize(16);

			// dummy Value
			Random r = new Random();
			int dummySportTestValue = 180 - r.nextInt(40);

			// result values
			TextView resultValue = new TextView(getActivity());
			resultValue.setText(dummySportTestValue + "");
			resultValue.setPadding(0, 0, 40, 0);
			resultValue.setTextSize(16);

			tableRowValue.addView(dateValue);
			tableRowValue.addView(resultValue);

			table.addView(tableRowValue);
		}
	}
}
