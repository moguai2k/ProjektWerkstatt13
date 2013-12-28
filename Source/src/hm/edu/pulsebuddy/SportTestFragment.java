package hm.edu.pulsebuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Represents the note text for the user.
 */
public class SportTestFragment extends Fragment implements
    OnItemSelectedListener, View.OnClickListener
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private Spinner kindOfSportsSpinner;

  public SportTestFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    View view = inflater.inflate( R.layout.fragment_sport_test, container,
        false );

    kindOfSportsSpinner = (Spinner) view
        .findViewById( R.id.spinnerKindOfSports );

    setKindOfSports();
    Button startSportTestButton = (Button) view
        .findViewById( R.id.buttonStartSportTest );
    startSportTestButton.setOnClickListener( this );

    return view;
  }

  private void setKindOfSports()
  {
    // set value if kind of sports exists in db

    // TODO @Tore: aus kommentieren und zuvor Prüfen ob Wert in DB schon besteht
    // falls ja dann diesen im Spinner setzten
    // if ( usermodel.getkindOfSportsDB() != null )
    // {
    // kindOfSportsSpinner.setSelection( usermodel.getkindOfSportsDB() );
    // }

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        getActivity().getBaseContext(), R.array.kind_of_sports_array,
        android.R.layout.simple_spinner_item );

    adapter
        .setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    kindOfSportsSpinner.setAdapter( adapter );
    kindOfSportsSpinner.setOnItemSelectedListener( this );

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
    // TODO @Tore: Trainingsart in DB speichern
    // 0: Kraft
    // 1: Ausdauer
    // 2: Abnehmen
    kindOfSportsSpinner.getSelectedItemPosition();

    // switch to second tab "Durchführen"
    getActivity().getActionBar().setSelectedNavigationItem( 1 );
  }

}
