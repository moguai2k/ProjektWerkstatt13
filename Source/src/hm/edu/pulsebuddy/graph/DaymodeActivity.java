package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class DaymodeActivity extends Activity implements OnClickListener
{

  private final static String TAG = "Daymode";

  private static final String TIME_FORMAT = "H:mm:ss";

  /* Called when the activity is first created. */
  private XYMultipleSeriesDataset mDataset;
  private XYMultipleSeriesRenderer mRenderer;
  List<double[]> values = new ArrayList<double[]>();
  private GraphicalView mChartView;
  private TimeSeries time_series;

  // chart container
  private LinearLayout layout;

  private DataInterface di;
  private DataHandler dh;
  private ArrayList<Pulse> mPulses;

  /* Zooming */
  private double mZoomLevel = 1;
  private static final int TEN_SEC = 10000;
  private static final int TWO_SEC = 2000;
  private double mYAxisMin = 250;
  private double mYAxisMax = 40;
  private int mYAxisPadding = 5;

  private final ZoomListener mZoomListener = new ZoomListener()
  {
    @Override
    public void zoomReset()
    {
      mZoomLevel = 1;
      scrollGraph( new Date().getTime() );
    }

    @Override
    public void zoomApplied( final ZoomEvent event )
    {
      if ( event.isZoomIn() )
      {
        mZoomLevel /= 2;
      }
      else
      {
        mZoomLevel *= 2;
      }
      scrollGraph( new Date().getTime() );
    }
  };

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.daymode_activity );

    dh = DataManager.getStorageInstance();
    di = DataManager.getDataInterface();
    mPulses = di.getAllPulses();

    layout = (LinearLayout) findViewById( R.id.chart );

    /* create dataset and renderer */
    mDataset = new XYMultipleSeriesDataset();
    mRenderer = new XYMultipleSeriesRenderer();
    
    mRenderer.setLabelsColor( Color.RED );
    mRenderer.setAxesColor( Color.RED );
    mRenderer.setGridColor( Color.rgb( 136, 136, 136 ) );
    mRenderer.setBackgroundColor( Color.WHITE );
    mRenderer.setApplyBackgroundColor( true );

    mRenderer.setMarginsColor( Color.WHITE );
    mRenderer.setXLabelsColor( Color.RED );

    mRenderer.setLegendTextSize( 20 );
    mRenderer.setLabelsTextSize( 20 );
    mRenderer.setPointSize( 8 );
    mRenderer.setMargins( new int[] { 20, 20, 20, 20 } );

    mRenderer.setFitLegend( true );
    mRenderer.setShowGrid( true );
    mRenderer.setZoomEnabled( true );
    mRenderer.setExternalZoomEnabled( true );
    mRenderer.setAntialiasing( true );
    mRenderer.setInScroll( true );
    
    
    
    
    
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor( Color.RED );
    r.setPointStyle( PointStyle.POINT );
    r.setFillPoints( true );
    mRenderer.addSeriesRenderer( r );
    mRenderer.setClickEnabled( true );
    mRenderer.setSelectableBuffer( 20 );
    mRenderer.setPanEnabled( true );
    
    /*
    mRenderer.setAxisTitleTextSize( 16 );
    mRenderer.setChartTitleTextSize( 20 );
    mRenderer.setLabelsTextSize( 15 );
    mRenderer.setLegendTextSize( 15 );
    mRenderer.setPointSize( 3f );

    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor( Color.RED );
    r.setPointStyle( PointStyle.CIRCLE );
    r.setFillPoints( true );
    mRenderer.addSeriesRenderer( r );
    mRenderer.setClickEnabled( true );
    mRenderer.setSelectableBuffer( 20 );
    mRenderer.setPanEnabled( true );
    */

    /* NEW */
    mRenderer.setLabelsColor( Color.RED );
    mRenderer.setAxesColor( Color.RED );
    mRenderer.setGridColor( Color.rgb( 136, 136, 136 ) );
    mRenderer.setBackgroundColor( Color.WHITE );
    mRenderer.setApplyBackgroundColor( true );

    mRenderer.setMarginsColor( Color.WHITE );
    mRenderer.setXLabelsColor( Color.RED );

    mRenderer.setLegendTextSize( 20 );
    mRenderer.setLabelsTextSize( 20 );
    mRenderer.setPointSize( 8 );
    mRenderer.setMargins( new int[] { 20, 20, 20, 20 } );

    mRenderer.setFitLegend( true );
    mRenderer.setShowGrid( true );
    mRenderer.setZoomEnabled( true );
    mRenderer.setExternalZoomEnabled( true );
    mRenderer.setAntialiasing( true );
    mRenderer.setInScroll( true );
    /* END - NEW */

    time_series = new TimeSeries( "Pulses" );
    mDataset.addSeries( time_series );

    mChartView = ChartFactory.getTimeChartView( this, mDataset, mRenderer,
        TIME_FORMAT );

    /* Add zoom listener ` */
    mChartView.addZoomListener( mZoomListener, true, false );

    fillData();

    layout.addView( mChartView );
  }

  private void fillData()
  {
    long value = new Date().getTime() - 3 * TimeChart.DAY;

    if ( mPulses.size() > 0 )
    {
      for ( Pulse p : mPulses )
      {
        time_series.add( new Date( p.getTime() ), p.getValue() );
      }
    }

    /* for ( int i = 0; i < 100; i++ ) { time_series.add( new Date( value + i *
     * TimeChart.DAY / 4 ), i ); } */
  }

  private void scrollGraph( final long time )
  {
    final double[] limits = new double[] { time - TEN_SEC * mZoomLevel,
        time + TWO_SEC * mZoomLevel, mYAxisMin - mYAxisPadding,
        mYAxisMax + mYAxisPadding };
    mRenderer.setRange( limits );
  }

  @Override
  public void onClick( View v )
  {
    Log.d( TAG, "on Click" );

    switch ( v.getId() )
    {
      case R.id.zoom_in:
        mChartView.zoomIn();
        break;

      case R.id.zoom_out:
        mChartView.zoomOut();
        break;

      case R.id.zoom_reset:
        mChartView.zoomReset();
        break;

      default:
        break;
    }
  }
}