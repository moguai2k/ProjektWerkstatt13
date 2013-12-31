package hm.edu.pulsebuddy;

import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.UserModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class SportTestNoteFragment extends Fragment implements
    OnItemSelectedListener, View.OnClickListener
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private Spinner kindOfSportsSpinner;
 
  private final static String TAG = "SportTestNoteFragment";
  
  private DataInterface di;

  public SportTestNoteFragment()
  {
    di = DataManager.getDataInterface();
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
    UserModel user = di.getUserInstance(); 

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        getActivity().getBaseContext(), R.array.kind_of_sports_array,
        android.R.layout.simple_spinner_item );

    adapter
        .setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    kindOfSportsSpinner.setAdapter( adapter );
    kindOfSportsSpinner.setOnItemSelectedListener( this );
    
    if ( user.getTrainingType() != null )
      kindOfSportsSpinner.setSelection( user.getTrainingTypeAsInt() ); 

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
    int trainingType = kindOfSportsSpinner.getSelectedItemPosition();
    UserModel user = di.getUserInstance();
    if ( user != null )
    {
      user.setTrainingType( trainingType );
      Log.d( TAG, "Saved training type" );
      di.savaUserInstance( user );
    }
    /* switch to second tab "Durchführen" */
    getActivity().getActionBar().setSelectedNavigationItem( 1 );
  }
  
}
