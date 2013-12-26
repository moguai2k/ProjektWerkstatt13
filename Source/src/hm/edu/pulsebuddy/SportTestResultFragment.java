package hm.edu.pulsebuddy;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class SportTestResultFragment extends Fragment
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";

  public SportTestResultFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    View rootView = inflater.inflate( R.layout.fragment_sport_test_result,
        container, false );
    // TODO add content
    return rootView;
  }
}
