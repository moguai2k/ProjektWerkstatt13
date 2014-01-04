package hm.edu.pulsebuddy.sportmode;

import hm.edu.pulsebuddy.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;

/**
 * Represents the note text for the user.
 */
public class SportTestNoteFragment extends Fragment implements
    OnItemSelectedListener, View.OnClickListener
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    View view = inflater.inflate( R.layout.fragment_sport_test, container,
        false );

    Button startSportTestButton = (Button) view
        .findViewById( R.id.buttonStartSportTest );
    startSportTestButton.setOnClickListener( this );

    return view;
  }

  @Override
  public void onItemSelected( AdapterView<?> parent, View view, int pos, long id )
  {
    parent.getItemAtPosition( pos );
  }

  @Override
  public void onNothingSelected( AdapterView<?> parent )
  {
    // Another interface callback
  }

  @Override
  public void onClick( View v )
  {

    /* switch to second tab "Durchf��hren" */
    getActivity().getActionBar().setSelectedNavigationItem( 1 );
  }

}
