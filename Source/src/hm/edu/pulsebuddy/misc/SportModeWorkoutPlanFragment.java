package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.SportModeWorkoutPlanData;
import hm.edu.pulsebuddy.sportmode.SportModeWorkoutPlanAdapter;

import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class SportModeWorkoutPlanFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	SparseArray<SportModeWorkoutPlanData> groups = new SparseArray<SportModeWorkoutPlanData>();

	public SportModeWorkoutPlanFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sport_mode_workout_plan,
				container, false);

		createData();
		ExpandableListView listView = (ExpandableListView) view
				.findViewById(R.id.expandableListViewWorkoutPlan);
		SportModeWorkoutPlanAdapter adapter = new SportModeWorkoutPlanAdapter(
				this, groups);
		listView.setAdapter(adapter);

		return view;
	}

	public void createData() {

		for (int j = 0; j < 6; j++) {

			int weekCount = j;
			weekCount++;

			SportModeWorkoutPlanData group = new SportModeWorkoutPlanData(
					weekCount + ". Woche");
			for (int i = 0; i < 7; i++) {
				int dayCount = i;
				dayCount++;

				Random r = new Random();
				int dummyPulseValue = r.nextInt(170 - 30) + 65;

				group.children.add(dayCount + ". Tag: " + dummyPulseValue
						+ " bpm");
			}
			groups.append(j, group);
		}
	}

}