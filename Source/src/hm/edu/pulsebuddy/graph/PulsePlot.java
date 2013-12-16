package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.listeners.PulseChangedListener;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.text.DecimalFormat;
import java.util.Arrays;

import android.graphics.Color;
import android.graphics.Paint;
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
  private static final int SECONDS = 300;
  private static final int PULSE = 200;
  private XYPlot aprHistoryPlot = null;
  // private MultitouchPlot aprHistoryPlot = null;
  private SimpleXYSeries rollHistorySeries = null;
  private TextView tv = null;

  private DataHandler ds = null;

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
    aprHistoryPlot.getGraphWidget().setRangeOriginLinePaint( null );

    // aprHistoryPlot.getGraphWidget().setRangeLabelPaint(null);
    // aprHistoryPlot.getGraphWidget().setRangeGridLinePaint(null);

    aprHistoryPlot.setRangeBoundaries( 0, PULSE, BoundaryMode.FIXED );
    aprHistoryPlot.setDomainBoundaries( 0, SECONDS, BoundaryMode.FIXED );

    LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(
        Color.rgb( 204, 0, 0 ), null, null, null );
    Paint paint = lineAndPointFormatter.getLinePaint();
    paint.setStrokeWidth( 8 );
    lineAndPointFormatter.setLinePaint( paint );

    aprHistoryPlot.addSeries( rollHistorySeries, lineAndPointFormatter );
    aprHistoryPlot.setDomainStepMode( XYStepMode.INCREMENT_BY_VAL );
    aprHistoryPlot.setDomainStepValue( SECONDS / 6 );
    aprHistoryPlot.setRangeStepMode( XYStepMode.INCREMENT_BY_VAL );
    aprHistoryPlot.setRangeStepValue( 25.0d );
    aprHistoryPlot.setRangeValueFormat( new DecimalFormat( "#" ) );
    aprHistoryPlot.setDomainValueFormat( new DecimalFormat( "#" ) );

    // aprHistoryPlot.setTicksPerRangeLabel(3);
    aprHistoryPlot.setDomainLabel( "Zeit" );
    aprHistoryPlot.getDomainLabelWidget().pack();
    aprHistoryPlot.setRangeLabel( "Puls" );
    aprHistoryPlot.getRangeLabelWidget().pack();

    aprHistoryPlot.getLayoutManager().remove( aprHistoryPlot.getLegendWidget() );

    redrawer = new Redrawer( Arrays.asList( new Plot[] { aprHistoryPlot } ),
        100, false );
    redrawer.start();

    aprHistoryPlot.redraw();
  }

  @Override
  public void handlePulseChangedEvent( Pulse aPulse )
  {
    if ( rollHistorySeries.size() > SECONDS )
      rollHistorySeries.removeFirst();

    rollHistorySeries.addLast( null, aPulse.getValue() );
    tv.setText( "" + aPulse.getValue() );
  }
}