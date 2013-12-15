package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.R;

import java.text.DecimalFormat;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class GraphDayActivity extends Activity implements OnTouchListener
{
  private static final int SERIES_SIZE = 200;
  private XYPlot aprHistoryPlot;
  private Button resetButton;
  private SimpleXYSeries[] series = null;
  private PointF minXY;
  private PointF maxXY;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.graph_day );
    resetButton = (Button) findViewById( R.id.resetButton );
    resetButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        minXY.x = series[ 0 ].getX( 0 ).floatValue();
        maxXY.x = series[ 0 ].getX( series[ 0 ].size() - 1 ).floatValue();
        aprHistoryPlot.setDomainBoundaries( minXY.x, maxXY.x,
            BoundaryMode.FIXED );

        aprHistoryPlot.redraw();
      }
    } );
    aprHistoryPlot = (XYPlot) findViewById( R.id.dayGraphPlot );
    aprHistoryPlot.setOnTouchListener( this );
    aprHistoryPlot.getGraphWidget().setTicksPerRangeLabel( 2 );
    aprHistoryPlot.getGraphWidget().setTicksPerDomainLabel( 2 );
    aprHistoryPlot.getGraphWidget().getBackgroundPaint()
        .setColor( Color.TRANSPARENT );
    aprHistoryPlot.getGraphWidget().setRangeValueFormat(
        new DecimalFormat( "#####" ) );
    aprHistoryPlot.getGraphWidget().setDomainValueFormat(
        new DecimalFormat( "#####.#" ) );
    aprHistoryPlot.getGraphWidget().setRangeLabelWidth( 25 );
    aprHistoryPlot.getGraphWidget().getRangeOriginLinePaint()
        .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getDomainOriginLinePaint()
        .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getRangeOriginLabelPaint()
        .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getDomainOriginLabelPaint()
        .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getRangeLabelPaint()
        .setColor( Color.rgb( 204, 0, 0 ) );
    aprHistoryPlot.getGraphWidget().getDomainLabelPaint()
        .setColor( Color.rgb( 204, 0, 0 ) );

    aprHistoryPlot.setDomainLabel( "Zeit" );
    aprHistoryPlot.getDomainLabelWidget().pack();
    aprHistoryPlot.setRangeLabel( "Puls" );
    aprHistoryPlot.getRangeLabelWidget().pack();
    aprHistoryPlot.getLayoutManager().remove( aprHistoryPlot.getLegendWidget() );

    aprHistoryPlot.setBorderStyle( Plot.BorderStyle.NONE, null, null );

    series = new SimpleXYSeries[ 1 ];
    int scale = 10;
    series[ 0 ] = new SimpleXYSeries( "S" );
    populateSeries( series[ 0 ], scale );
    aprHistoryPlot.addSeries(
        series[ 0 ],
        new LineAndPointFormatter( Color.rgb( 0, 0, 0 ), null, Color.rgb( 255,
            255, 255 ), null ) );
    aprHistoryPlot.redraw();
    aprHistoryPlot.calculateMinMaxVals();
    minXY = new PointF( aprHistoryPlot.getCalculatedMinX().floatValue(),
        aprHistoryPlot.getCalculatedMinY().floatValue() );
    maxXY = new PointF( aprHistoryPlot.getCalculatedMaxX().floatValue(),
        aprHistoryPlot.getCalculatedMaxY().floatValue() );
  }

  private void populateSeries( SimpleXYSeries series, int max )
  {
    Random r = new Random();
    for ( int i = 0; i < SERIES_SIZE; i++ )
    {
      series.addLast( i, r.nextInt( max ) );
    }
  }

  static final int NONE = 0;
  static final int ONE_FINGER_DRAG = 1;
  static final int TWO_FINGERS_DRAG = 2;
  int mode = NONE;

  PointF firstFinger;
  float distBetweenFingers;
  boolean stopThread = false;

  @Override
  public boolean onTouch( View arg0, MotionEvent event )
  {
    switch ( event.getAction() & MotionEvent.ACTION_MASK )
    {
      case MotionEvent.ACTION_DOWN:
        firstFinger = new PointF( event.getX(), event.getY() );
        mode = ONE_FINGER_DRAG;
        stopThread = true;
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
        mode = NONE;
        break;
      case MotionEvent.ACTION_POINTER_DOWN:
        distBetweenFingers = spacing( event );

        if ( distBetweenFingers > 5f )
        {
          mode = TWO_FINGERS_DRAG;
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if ( mode == ONE_FINGER_DRAG )
        {
          PointF oldFirstFinger = firstFinger;
          firstFinger = new PointF( event.getX(), event.getY() );
          scroll( oldFirstFinger.x - firstFinger.x );
          aprHistoryPlot.setDomainBoundaries( minXY.x, maxXY.x,
              BoundaryMode.FIXED );
          aprHistoryPlot.redraw();

        }
        else if ( mode == TWO_FINGERS_DRAG )
        {
          float oldDist = distBetweenFingers;
          distBetweenFingers = spacing( event );
          zoom( oldDist / distBetweenFingers );
          aprHistoryPlot.setDomainBoundaries( minXY.x, maxXY.x,
              BoundaryMode.FIXED );
          aprHistoryPlot.redraw();
        }
        break;
    }
    return true;
  }

  private void zoom( float scale )
  {
    float domainSpan = maxXY.x - minXY.x;
    float domainMidPoint = maxXY.x - domainSpan / 2.0f;
    float offset = domainSpan * scale / 2.0f;

    minXY.x = domainMidPoint - offset;
    maxXY.x = domainMidPoint + offset;

    minXY.x = Math.min( minXY.x, series[ 0 ].getX( series[ 0 ].size() - 3 )
        .floatValue() );
    maxXY.x = Math.max( maxXY.x, series[ 0 ].getX( 1 ).floatValue() );
    clampToDomainBounds( domainSpan );
  }

  private void scroll( float pan )
  {
    float domainSpan = maxXY.x - minXY.x;
    float step = domainSpan / aprHistoryPlot.getWidth();
    float offset = pan * step;
    minXY.x = minXY.x + offset;
    maxXY.x = maxXY.x + offset;
    clampToDomainBounds( domainSpan );
  }

  private void clampToDomainBounds( float domainSpan )
  {
    float leftBoundary = series[ 0 ].getX( 0 ).floatValue();
    float rightBoundary = series[ 0 ].getX( series[ 0 ].size() - 1 )
        .floatValue();

    if ( minXY.x < leftBoundary )
    {
      minXY.x = leftBoundary;
      maxXY.x = leftBoundary + domainSpan;
    }
    else if ( maxXY.x > series[ 0 ].getX( series[ 0 ].size() - 1 ).floatValue() )
    {
      maxXY.x = rightBoundary;
      minXY.x = rightBoundary - domainSpan;
    }
  }

  @SuppressLint( "FloatMath" )
  private float spacing( MotionEvent event )
  {
    float x = event.getX( 0 ) - event.getX( 1 );
    float y = event.getY( 0 ) - event.getY( 1 );
    return FloatMath.sqrt( x * x + y * y );
  }
}
