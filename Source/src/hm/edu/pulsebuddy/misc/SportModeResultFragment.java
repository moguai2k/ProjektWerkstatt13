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
 */
public class SportModeResultFragment extends Fragment
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private View view;
  private View layout;

  private DataInterface di;

  public SportModeResultFragment()
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

    di = DataManager.getDataInterface();

    addData();

    return view;
  }

  // @Tore: Hier werte aus DB abfragen und in Tabelle eintragen
  private void addData()
  {
    TableLayout table = (TableLayout) view
        .findViewById( R.id.tableLayoutResult );

    // heading
    TableRow tableRowHeading = new TableRow( getActivity() );

    TextView dateHeading = new TextView( getActivity() );
    dateHeading.setText( "Datum" );
    dateHeading.setTypeface( null, Typeface.BOLD );
    dateHeading.setPadding( 0, 0, 40, 20 );
    dateHeading.setTextSize( 18 );

    TextView resultHeading = new TextView( getActivity() );
    resultHeading.setText( "Conconi-Ergebnis" );
    resultHeading.setTypeface( null, Typeface.BOLD );
    resultHeading.setTextSize( 18 );

    tableRowHeading.addView( dateHeading );
    tableRowHeading.addView( resultHeading );

    table.addView( tableRowHeading );

    // values
    for ( int i = 10; i <= 30; i++ )
    {
      TableRow tableRowValue = new TableRow( getActivity() );

      TextView dateValue = new TextView( getActivity() );
      dateValue.setText( i + ".12.2013" );
      dateValue.setPadding( 0, 0, 40, 20 );
      dateValue.setTextSize( 16 );

      // dummy Value
      Random r = new Random();
      int dummySportTestValue = 180 - r.nextInt( 40 );

      // result values
      TextView resultValue = new TextView( getActivity() );
      resultValue.setText( dummySportTestValue + "" );
      resultValue.setPadding( 0, 0, 40, 0 );
      resultValue.setTextSize( 16 );

      tableRowValue.addView( dateValue );
      tableRowValue.addView( resultValue );

      table.addView( tableRowValue );

    }

    // TableRow tableRowValues = new TableRow( getActivity() );
    //
    // // date values
    // TextView dateValues = new TextView( getActivity() );
    // dateValues.setText( 01 + ".12.2013" );
    // dateValues.setPadding( 0, 0, 40, 0 );
    // dateValues.setTextSize( 16 );
    //
    // Random r = new Random();
    // int dummySportTestValue = r.nextInt( 30 - 5 ) + 5;
    //
    // // result values
    // TextView resultValues = new TextView( getActivity() );
    // resultValues.setText( dummySportTestValue + "" );
    // resultValues.setTextSize( 16 );
    //
    // tableRowValues.addView( dateHeading, 1 );
    // tableRowValues.addView( resultHeading, 2 );
    //
    // table.addView( tableRowValues, 1 );

    // // TableRow tableRow = (TableRow) getActivity().findViewById(
    // // R.id.tableRowResult );
    //
    // // add heading
    // TableRow row = new TableRow( getActivity() );
    // // TableRow.LayoutParams lp = new TableRow.LayoutParams(
    // // TableRow.LayoutParams.WRAP_CONTENT );
    // // row.setLayoutParams( lp );
    //
    // TextView dateValue = new TextView( getActivity() );
    // dateValue.setText( "Datum 2" );
    // row.addView( dateValue );
    //
    // // TextView headingResult = new TextView( getActivity() );
    // // headingDate.setText( "Ergebnis" );
    // // row.addView( headingResult );
    //
    // table.addView( row );
    //
    // // for ( int i = 1; i < 3; i++ )
    // // {
    // //
    // // TableRow rowValue = new TableRow( getActivity() );
    // // TableRow.LayoutParams lpValue = new TableRow.LayoutParams(
    // // TableRow.LayoutParams.WRAP_CONTENT );
    // // rowValue.setLayoutParams( lpValue );
    // //
    // // TextView qty = new TextView( getActivity() );
    // // qty.setText( "10" );
    // // rowValue.addView( qty );
    // //
    // // table.addView( rowValue, i );
    // // }

  }
}
