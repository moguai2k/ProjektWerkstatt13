package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.util.SportModeWorkoutPlanData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class SportModeWorkoutPlanFragment extends Fragment
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  SparseArray<SportModeWorkoutPlanData> groups = new SparseArray<SportModeWorkoutPlanData>();

  public SportModeWorkoutPlanFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    View view = inflater.inflate( R.layout.fragment_sport_mode_workout_plan,
        container, false );

    createData();
    ExpandableListView listView = (ExpandableListView) view
        .findViewById( R.id.expandableListViewWorkoutPlan );
    SportModeWorkoutPlanAdapter adapter = new SportModeWorkoutPlanAdapter(
        this, groups );
    listView.setAdapter( adapter );

    return view;
  }

  public void createData()
  {
    for ( int j = 0; j < 6; j++ )
    {
      SportModeWorkoutPlanData group = new SportModeWorkoutPlanData( "Test "
          + j );
      for ( int i = 0; i < 5; i++ )
      {
        group.children.add( "Sub Item" + i );
      }
      groups.append( j, group );
    }
  }

}