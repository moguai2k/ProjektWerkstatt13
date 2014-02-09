package hm.edu.pulsebuddy.misc;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.common.RunningState;
import hm.edu.pulsebuddy.data.DataInterface;

import java.util.Locale;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The user will receive during sports test instructions.
 */
public class SportModeStartFragment extends Fragment implements TextToSpeech.OnInitListener
{

  private DataInterface di;

  /**
   * The fragment argument representing the section number for this fragment.
   */
  public static final String ARG_SECTION_NUMBER = "section_number";
  private View rootView;
  private RunningState runningState;

  public SportModeStartFragment()
  {
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState )
  {
    rootView = inflater.inflate( R.layout.fragment_sport_test_start, container,
        false );
    Button buttonStop = (Button) rootView.findViewById( R.id.buttonStop );
    buttonStop.setOnClickListener( stopButtonClicked );
    buttonStop.setVisibility( View.INVISIBLE );
    
    Button changeButton = (Button) rootView.findViewById( R.id.buttonChange );
    changeButton.setOnClickListener( changeButtonClicked );
    changeButton.setVisibility( View.INVISIBLE );

    Button startSportTestButton = (Button) rootView
        .findViewById( R.id.buttonStartSportTest );
    startSportTestButton.setOnClickListener( startButtonClicked );
    
    tts = new TextToSpeech(getActivity(), this);

    // TODO @Team methode ersetzten mit echten Hinweisen (schneller, Tempo
    // halten, langsamer) f��r den L��ufer
    dummySetRunningState();

    return rootView;
  }

  private void dummySetRunningState()
  {
    // setRunningState( RunningState.keep );
    // changeRunningState( getRunningState() );

    //int pick = new Random().nextInt( RunningState.values().length );
    //setRunningState( RunningState.values()[ pick ] );
    //changeRunningState( getRunningState() );
	  
	  switch ( (int)(3*Math.random()) ) {
	     case 0:
    		walkFaster();
    	    setRunningState( RunningState.faster);
    	    changeRunningState( getRunningState() );
	        break;
	     case 1:
    		walkSlower();
    	    setRunningState( RunningState.slower);
    	    changeRunningState( getRunningState() );
	        break;
	     case 2:
    		holdSpeed();
    	    setRunningState( RunningState.keep);
    	    changeRunningState( getRunningState() );
	        break;
	  }
  }

  /**
   * Set running status depending on the speed of the runner.
   * 
   * @param runningState
   */
  private void changeRunningState( RunningState runningState )
  {
    TextView runningStateTextView = (TextView) rootView
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
      return "Schneller!";
    }
    else if ( RunningState.keep.equals( runningState ) )
    {
      return "Tempo halten!";
    }
    else if ( RunningState.slower.equals( runningState ) )
    {
      return "Langsamer!";
    }
    return null;
  }

  /**
   * Start running test after countdown.
   * 
   * @param rootView
   *          The tab rootView.
   */
  private void countdown()
  {
    CountDownTimer counter = new CountDownTimer( 5000, 1000 )
    {

      @Override
      public void onTick( long millisUntilFinished )
      {
        TextView countdownTimmer = (TextView) rootView
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
        fadeInRuningState();
        fadeOutCountdown();
        fadeInStopButton();
        fadeInChangeButton();

        // TODO @Team: hier methode einf��gen um Lauftest zu starten bzw.
        // Aufzeichnung. Methode ofFinish wird
        // aufgerufen sobald der Countdown abgelaufen ist.

      }

      /**
       * Fade in the running state
       */
      private void fadeInRuningState()
      {
        TextView textViewRunningState = (TextView) rootView
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
        Button buttonStop = (Button) rootView.findViewById( R.id.buttonStop );
        AlphaAnimation fadeInStopButton = new AlphaAnimation( 0.0f, 1.0f );
        buttonStop.setAnimation( fadeInStopButton );
        fadeInStopButton.setDuration( 1200 );
        fadeInStopButton.setFillAfter( true );
        buttonStop.setVisibility( View.VISIBLE );
      }
      private void fadeInChangeButton()
      {
        Button changeButton = (Button) rootView.findViewById( R.id.buttonChange );
        AlphaAnimation fadeInChangeButton = new AlphaAnimation( 0.0f, 1.0f );
        changeButton.setAnimation( fadeInChangeButton );
        fadeInChangeButton.setDuration( 1200 );
        fadeInChangeButton.setFillAfter( true );
        changeButton.setVisibility( View.VISIBLE );
      }

      /**
       * Fade out the countdown
       */
      private void fadeOutCountdown()
      {
        TextView countdownTimmer = (TextView) rootView
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

  //
  /**
   * Fade out the note text
   */
  private void fadeOutNoteText()
  {
    LinearLayout noteLayout = (LinearLayout) rootView
        .findViewById( R.id.linearLayoutNote );

    AlphaAnimation fadeOutCountdown = new AlphaAnimation( 1.0f, 0.0f );
    noteLayout.startAnimation( fadeOutCountdown );
    fadeOutCountdown.setDuration( 800 );
    fadeOutCountdown.setFillAfter( true );

  }

  /**
   * Fade out the start button
   */
  private void fadeOutStartButton()
  {
    Button startSportTest = (Button) rootView
        .findViewById( R.id.buttonStartSportTest );

    AlphaAnimation fadeOutCountdown = new AlphaAnimation( 1.0f, 0.0f );
    startSportTest.startAnimation( fadeOutCountdown );
    fadeOutCountdown.setDuration( 800 );
    fadeOutCountdown.setFillAfter( true );
    startSportTest.setClickable( false );
  }

  public RunningState getRunningState()
  {
    return runningState;
  }

  public void setRunningState( RunningState runningState )
  {
    this.runningState = runningState;
  }

  View.OnClickListener startButtonClicked = new View.OnClickListener()
  {
    public void onClick( View v )
    {
      fadeOutStartButton();
      fadeOutNoteText();
      countdown();
      testStart();
    }
  };

  View.OnClickListener stopButtonClicked = new View.OnClickListener()
  {
    public void onClick( View v )
    {
      // TODO @Tore ergebnis speicheren wenn "Lauf stoppen" geklickt wird. Den
      // Code von aus SportModeResultFragment hab ich hierher verschoben da in
      // den Historie den speicher Button gelöscht habe

      // Random r = new Random();
      // CoconiResultModel c = new CoconiResultModel( r.nextInt( 200 ), new
      // Date() );
      // di.addCoconiResult( c );

      /* ArrayList<CoconiResultModel> all = di.getCoconiTestResults(); for ( int
       * i = 0; i < all.size(); i++ ) { Log.d( TAG, "Coconi result pulse: " +
       * all.get( i )._id ); } */

      // switch to second tab "Trainingsplan"
      getActivity().getActionBar().setSelectedNavigationItem( 1 );
    }
  };
  
  View.OnClickListener changeButtonClicked = new View.OnClickListener()
  {
    public void onClick( View v )
    {
    	dummySetRunningState();
    }
  };

  
  
  
  
  //TTS
  private TextToSpeech tts = null;
  private static final String testStart = "Der Test beginnt! Laufe bitte los!";
  private static final String testEnd = "Das wars, der Test ist vorbei!";
  private static final String walkFaster = "Laufe etwas schneller!";
  private static final String walkSlower = "Laufe etwas langsamer!";
  private static final String holdSpeed = "Halte dein Tempo!";
  
  public void onInit(int status) {
	  
      if (status == TextToSpeech.SUCCESS) {

          int result = tts.setLanguage(Locale.GERMAN);

          if (result == TextToSpeech.LANG_MISSING_DATA
                  || result == TextToSpeech.LANG_NOT_SUPPORTED) {
              Log.e("TTS", "This Language is not supported");
          }
      } else {
          Log.e("TTS", "Initilization Failed!");
      }

  }
  
  public void walkFaster(){
	    sayText( walkFaster );
	  }
	  
	  public void walkSlower(){
	    sayText( walkSlower );
	  }
	  
	  public void holdSpeed(){
	    sayText( holdSpeed );
	  }
	  
	  public void testStart(){
	    sayText( testStart );
	  }
	  
	  public void testEnd(){
	    sayText( testEnd );
	  }
	  
	  public void sayText(String text) {
	      tts.speak(text, TextToSpeech.QUEUE_ADD, null);
	  }
	  
	    @Override
	    public void onDestroy() {
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }

}
