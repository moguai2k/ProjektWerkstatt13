package hm.edu.pulsebuddy;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class SportTestResultFragment extends Fragment implements
    View.OnClickListener
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private View view;
  private View layout;

  public SportTestResultFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    view = inflater.inflate( R.layout.fragment_sport_test_result, container,
        false );

    layout = inflater.inflate( R.layout.custom_toast,
        (ViewGroup) view.findViewById( R.id.toast_layout ) );

    Button buttonAgain = (Button) view.findViewById( R.id.buttonAgain );
    buttonAgain.setOnClickListener( this );
    Button buttonSave = (Button) view.findViewById( R.id.buttonSave );
    buttonSave.setOnClickListener( this );

    // TODO @Team: Methode um Sport Test abzubrechen wenn Läufer nicht mehr
    // kann. Anschließend wert in DB schrieben

    return view;
  }

  @Override
  public void onClick( View view )
  {
    switch ( view.getId() )
    {
      case R.id.buttonAgain:
        Intent intent = new Intent( getActivity(), SportTestActivity.class );
        startActivity( intent );
        break;
      case R.id.buttonSave:
        saveSportTestResult();
        break;
      default:
    }

  }

  private void saveSportTestResult()
  {
    // TODO @Tore save value in DB

    customToast( getResources().getString( R.string.common_saved_values ) );

  }

  /**
   * Notifies the user that the data has been stored in the database.
   * 
   * @param text
   *          The message.
   */
  private void customToast( String text )
  {
    TextView textV = (TextView) layout.findViewById( R.id.toastText );
    textV.setText( text );

    Toast toast = new Toast( getActivity() );
    toast.setDuration( Toast.LENGTH_LONG );
    toast.setView( layout );
    toast.show();
  }

}
