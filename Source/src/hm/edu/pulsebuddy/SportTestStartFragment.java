package hm.edu.pulsebuddy;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hm.edu.pulsebuddy.common.RunningState;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

/**
 * The user will receive during sports test instructions.
 */
public class SportTestStartFragment extends Fragment implements
    View.OnClickListener
{
  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private View view;
  private RunningState runningState;

  public SportTestStartFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    view = inflater.inflate( R.layout.fragment_sport_test_start, container,
        false );
    Button buttonStop = (Button) view.findViewById( R.id.buttonStop );
    buttonStop.setOnClickListener( this );

    countdown( view );

    // TODO @Team methode ersetzten mit echten Hinweisen (schneller, Tempo
    // halten, langsamer) für den Läufer
    dummySetRunningState();

    return view;
  }

  private void dummySetRunningState()
  {
    // setRunningState( RunningState.keep );
    // changeRunningState( getRunningState() );

    int pick = new Random().nextInt( RunningState.values().length );
    setRunningState( RunningState.values()[ pick ] );
    changeRunningState( getRunningState() );
  }

  /**
   * Set running status depending on the speed of the runner.
   * 
   * @param runningState
   */
  private void changeRunningState( RunningState runningState )
  {
    TextView runningStateTextView = (TextView) view
        .findViewById( R.id.textViewRunningState );

    runningStateTextView.setText( calculateRunningState( runningState ) + "" );
  }

  /**
   * Convert the enum value to string.
   * 
   * @param runningState
   *          Running state is faster, keep speed or slower.
   * @return
   */
  private String calculateRunningState( RunningState runningState )
  {
    if ( RunningState.faster.equals( runningState ) )
    {
      return "schneller";
    }
    else if ( RunningState.keep.equals( runningState ) )
    {
      return "Tempo halten";
    }
    else if ( RunningState.slower.equals( runningState ) )
    {
      return "langsamer";
    }
    return null;
  }

  /**
   * Start running test after countdown.
   * 
   * @param view
   *          The tab view.
   */
  private void countdown( final View view )
  {
    CountDownTimer counter = new CountDownTimer( 5000, 1000 )
    {

      @Override
      public void onTick( long millisUntilFinished )
      {
        TextView countdownTimmer = (TextView) view
            .findViewById( R.id.textViewCountdown );

        AlphaAnimation fadeIn = new AlphaAnimation( 0.0f, 1.0f );
        countdownTimmer.setAnimation( fadeIn );
        fadeIn.setDuration( 1200 );
        fadeIn.setFillAfter( true );
        countdownTimmer.setText( "Achtung" );
      }

      @Override
      public void onFinish()
      {
        fadeInFadeOut();
        fadeOutCountdown();
        fadeInStopButton();

        // TODO @Team: hier methode einfügen um Lauftest zu starten bzw.
        // Aufzeichnung. Methode ofFinish wird
        // aufgerufen sobald der Countdown abgelaufen ist.

      }

      /**
       * Fade in the running state
       */
      private void fadeInFadeOut()
      {
        TextView textViewRunningState = (TextView) view
            .findViewById( R.id.textViewRunningState );
        AlphaAnimation fadeInRunningStageTextView = new AlphaAnimation( 0.0f,
            1.0f );
        textViewRunningState.setAnimation( fadeInRunningStageTextView );
        fadeInRunningStageTextView.setDuration( 5000 );
        fadeInRunningStageTextView.setFillAfter( true );

        textViewRunningState.setVisibility( View.VISIBLE );
      }

      /**
       * Fade in the stop button
       */
      private void fadeInStopButton()
      {
        Button buttonStop = (Button) view.findViewById( R.id.buttonStop );
        AlphaAnimation fadeInStopButton = new AlphaAnimation( 0.0f, 1.0f );
        buttonStop.setAnimation( fadeInStopButton );
        fadeInStopButton.setDuration( 1200 );
        fadeInStopButton.setFillAfter( true );
        buttonStop.setVisibility( View.VISIBLE );
      }

      /**
       * Fade out the countdown
       */
      private void fadeOutCountdown()
      {
        TextView countdownTimmer = (TextView) view
            .findViewById( R.id.textViewCountdown );

        AlphaAnimation fadeOutCountdown = new AlphaAnimation( 1.0f, 0.0f );
        countdownTimmer.startAnimation( fadeOutCountdown );
        fadeOutCountdown.setDuration( 800 );
        fadeOutCountdown.setFillAfter( true );

        countdownTimmer.setText( "Los!" );
      }
    };
    counter.start();
  }

  @Override
  public void onClick( View view )
  {
    // switch to second tab "Ergebnis"
    getActivity().getActionBar().setSelectedNavigationItem( 2 );

  }

  public RunningState getRunningState()
  {
    return runningState;
  }

  public void setRunningState( RunningState runningState )
  {
    this.runningState = runningState;
  }

}
