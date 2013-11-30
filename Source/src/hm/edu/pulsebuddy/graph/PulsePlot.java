package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.db.DataManager;
import hm.edu.pulsebuddy.db.DataStorage;
import hm.edu.pulsebuddy.db.PulseChangedListener;
import hm.edu.pulsebuddy.model.PulseModel;

import java.text.DecimalFormat;
import java.util.Arrays;

import android.graphics.Color;
import android.hardware.SensorManager;
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
  private static final int HISTORY_SIZE = 200;
  private XYPlot aprHistoryPlot = null;
  private SimpleXYSeries rollHistorySeries = null;
  private TextView tv = null;

  private DataStorage ds = null;

  public PulsePlot( XYPlot aprHistoryPlot, SensorManager sensorMgr,
      TextView tv, Redrawer redrawer )
  {
    this.aprHistoryPlot = aprHistoryPlot;
    this.tv = tv;
    this.redrawer = redrawer;

    ds = DataManager.getStorageInstance();
    if ( ds != null )
      ds.addPulseChangedListener( this );
    graphIt();
  }

  @SuppressWarnings( "deprecation" )
  public void graphIt()
  {
    rollHistorySeries = new SimpleXYSeries( "Puls" );
    rollHistorySeries.useImplicitXVals();

    aprHistoryPlot.setRangeBoundaries( 0, 240, BoundaryMode.FIXED );
    aprHistoryPlot.setDomainBoundaries( 0, HISTORY_SIZE, BoundaryMode.FIXED );
    aprHistoryPlot.addSeries( rollHistorySeries, new LineAndPointFormatter(
        Color.rgb( 200, 100, 100 ), null, null, null ) );
    aprHistoryPlot.setDomainStepMode( XYStepMode.INCREMENT_BY_VAL );
    aprHistoryPlot.setDomainStepValue( HISTORY_SIZE / 10 );
    aprHistoryPlot.setTicksPerRangeLabel( 3 );
    aprHistoryPlot.setDomainLabel( "Sample Index" );
    aprHistoryPlot.getDomainLabelWidget().pack();
    aprHistoryPlot.setRangeLabel( "Angle (Degs)" );
    aprHistoryPlot.getRangeLabelWidget().pack();

    aprHistoryPlot.setRangeValueFormat( new DecimalFormat( "#" ) );
    aprHistoryPlot.setDomainValueFormat( new DecimalFormat( "#" ) );

    redrawer = new Redrawer( Arrays.asList( new Plot[] { aprHistoryPlot } ),
        100, false );
    redrawer.start();
  }

  @Override
  public void handlePulseChangedEvent( PulseModel aPulse )
  {
    if ( rollHistorySeries.size() > HISTORY_SIZE )
      rollHistorySeries.removeFirst();

    rollHistorySeries.addLast( null, aPulse.getPulse() );
    tv.setText( "" + aPulse.getPulse() );
  }

}