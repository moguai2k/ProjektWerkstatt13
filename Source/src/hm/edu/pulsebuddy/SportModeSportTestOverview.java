package hm.edu.pulsebuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SportModeSportTestOverview extends Fragment
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";

  public SportModeSportTestOverview()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    View view = inflater.inflate(
        R.layout.fragment_sport_mode_sport_test_overview, container, false );

    return view;
  }
}