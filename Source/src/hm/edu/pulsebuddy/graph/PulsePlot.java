package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.listeners.PulseChangedListener;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

public class PulsePlot implements PulseChangedListener
{
  private Redrawer redrawer;
  private static final int SECONDS = 240;
  private static final int PULSE = 160;
  private XYPlot aprHistoryPlot = null;
  // private MultitouchPlot aprHistoryPlot = null;
  private SimpleXYSeries rollHistorySeries = null;
  private TextView tv = null;

  private DataHandler ds = null;
  
  private Queue<Integer> pulseValues = new LinkedList<Integer>();
  
  private double lastPulse = 0;
  private double nextPulse = 0;

  public PulsePlot( XYPlot aprHistoryPlot, TextView tv, Redrawer redrawer )
  {
    // public PulsePlot(MultitouchPlot aprHistoryPlot, TextView tv, Redrawer
    // redrawer) {
    this.aprHistoryPlot = aprHistoryPlot;
    this.tv = tv;
    this.redrawer = redrawer;

    ds = DataManager.getStorageInstance();
    if ( ds != null )
      ds.addPulseChangedListener( this );

    graphIt();
  }

  public void graphIt()
  {
    rollHistorySeries = new SimpleXYSeries( "Puls" );
    rollHistorySeries.useImplicitXVals();
    
    aprHistoryPlot.setDrawDomainOriginEnabled( false );
    aprHistoryPlot.setBackgroundPaint( null );
    aprHistoryPlot.getGraphWidget().setBackgroundPaint( null );
    aprHistoryPlot.getGraphWidget().setGridBackgroundPaint( null );
    aprHistoryPlot.getGraphWidget().setDomainLabelPaint( null );
    aprHistoryPlot.getGraphWidget().setDomainOriginLabelPaint( null );
    aprHistoryPlot.getGraphWidget().setDomainGridLinePaint( null );
    aprHistoryPlot.getGraphWidget().setDomainOriginLinePaint( null );
    //aprHistoryPlot.getGraphWidget().setRangeOriginLinePaint( null );
    
    //aprHistoryPlot.getGraphWidget().setTicksPerRangeLabel( 2 ); 1 ... 3 ... 5
    aprHistoryPlot.getGraphWidget().setRangeLabelWidth( 50 );

    // aprHistoryPlot.getGraphWidget().setRangeLabelPaint(null);
    // aprHistoryPlot.getGraphWidget().setRangeGridLinePaint(null);
    
    aprHistoryPlot.getGraphWidget().getRangeOriginLinePaint()
    .setColor( Color.rgb( 192, 192, 192 ) );
    aprHistoryPlot.getGraphWidget().getRangeOriginLabelPaint()
    .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getRangeLabelPaint()
    .setColor( Color.rgb( 204, 0, 0 ) );

    aprHistoryPlot.setRangeBoundaries( 25, PULSE, BoundaryMode.FIXED );
    aprHistoryPlot.setDomainBoundaries( 0, SECONDS, BoundaryMode.FIXED );

    LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(
        Color.rgb( 204, 0, 0 ), null, null, null );
    Paint paint = lineAndPointFormatter.getLinePaint();
    paint.setStrokeWidth( 12 );
    lineAndPointFormatter.setLinePaint( paint );

    aprHistoryPlot.addSeries( rollHistorySeries, lineAndPointFormatter );
    aprHistoryPlot.setDomainStepMode( XYStepMode.INCREMENT_BY_VAL );
    aprHistoryPlot.setDomainStepValue( SECONDS / 6 );
    aprHistoryPlot.setRangeStepMode( XYStepMode.INCREMENT_BY_VAL );
    aprHistoryPlot.setRangeStepValue( 10.0d );
    aprHistoryPlot.setRangeValueFormat( new DecimalFormat( "#" ) );
    aprHistoryPlot.setDomainValueFormat( new DecimalFormat( "#" ) );

    // aprHistoryPlot.setTicksPerRangeLabel(3);
    //aprHistoryPlot.setDomainLabel( "Zeit" );
    aprHistoryPlot.getDomainLabelWidget().pack();
    aprHistoryPlot.setRangeLabel( "Puls" );
    aprHistoryPlot.getRangeLabelWidget().pack();

    aprHistoryPlot.getLayoutManager().remove( aprHistoryPlot.getLegendWidget() );

    redrawer = new Redrawer( Arrays.asList( new Plot[] { aprHistoryPlot } ),
        100, false );
    redrawer.start();

    aprHistoryPlot.redraw();
    
    new DemoPulseGenerator().execute();
  }

  @Override
  public void handlePulseChangedEvent( Pulse aPulse )
  {
    if ( rollHistorySeries.size() > SECONDS )
      rollHistorySeries.removeFirst();
      
    pulseValues.add( aPulse.getValue() );
  }
  
  /**
   * Demo pulse generator.
   */
  private class DemoPulseGenerator extends AsyncTask<Void, Integer, String>
  {
    @Override
    protected String doInBackground( Void... params )
    { 
      int steps = 10;
      int timeBetweenSteps = 1000 / steps;
      
      while ( true )
      {
        if ( nextPulse == 0 )
        {
          if ( pulseValues.peek() != null )
            nextPulse = pulseValues.poll();
        }
        else
        {
          if ( pulseValues.peek() != null )
          {
            double avgPulseSteps = 0.0;
            
            lastPulse = nextPulse;
            nextPulse = pulseValues.poll();
            double currentPulse = lastPulse;
            
            int minPulse = (int) ( (int) (lastPulse / 10) * 10 ) -25;
            int maxPulse = (int) ( (int) (lastPulse / 10) * 10 ) +25; 
            aprHistoryPlot.setRangeBoundaries( minPulse, maxPulse, BoundaryMode.FIXED );
                 
            if ( nextPulse > lastPulse )
            { // +pulse
              avgPulseSteps = ( nextPulse - lastPulse ) / steps;
            }
            else
            { // -pulse
              avgPulseSteps = ( lastPulse - nextPulse ) / steps;
            }
            
            publishProgress((int)currentPulse);

            for ( int i = 0; i < 9; i++ )
            {
              if ( nextPulse > lastPulse )
              { // +pulse
                currentPulse += avgPulseSteps;
              }
              else
              { // -pulse
                currentPulse -= avgPulseSteps;
              }
              
              publishProgress( (int) ( (double) Math.round( currentPulse * 10 ) / 10 ) );
              
              try
              {
                Thread.sleep( timeBetweenSteps );
              }
              catch ( InterruptedException e )
              {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }     
            }
          }
        }
        try
        {
          Thread.sleep( 100 );
        }
        catch ( InterruptedException e )
        {
          e.printStackTrace();
        }
      }
    }

    protected void onProgressUpdate( Integer... aPulse )
    {
      tv.setText( "" + aPulse[ 0 ] );
      
      if ( rollHistorySeries.size() > SECONDS )
        rollHistorySeries.removeFirst();
      
      rollHistorySeries.addLast( null, (int) ( (double) Math.round( aPulse[ 0 ] * 10 ) / 10 ) );
    }

    @Override
    protected void onPostExecute( String result )
    {
      
    }
  }
}