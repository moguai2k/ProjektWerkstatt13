package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.listeners.PulseChangedListener;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DaymodeFragment extends Fragment implements OnClickListener,
    PulseChangedListener
{
  private final static String TAG = "Daymode";

  // private static Random RAND = new Random();
  private static final String TIME = "H:mm:ss";

  private static final int TEN_SEC = 10000;
  private static final int TWO_SEC = 2000;
  private static final float RATIO = 0.618033988749895f;

  private View mViewZoomIn;
  private View mViewZoomOut;
  private View mViewZoomReset;
  private GraphicalView mChartView;
  private XYSeriesRenderer[] mThresholdRenderers;
  private XYMultipleSeriesRenderer mRenderer;
  private XYMultipleSeriesDataset mDataset;
  private HashMap<String, TimeSeries> mSeries;
  private TimeSeries[] mThresholds;

  private ArrayList<String> mItems;

  private double mYAxisMin = 250;
  private double mYAxisMax = 40;

  private double mZoomLevel = 1;
  private double mLastItemChange;
  private int mItemIndex;
  private int mYAxisPadding = 5;

  /* NEW */
  private DataInterface di;
  private DataHandler dh;
  private ArrayList<Pulse> mPulses;

  private XYSeriesRenderer mCurrentPulseRenderer;
  private TimeSeries mCurrentPulseSeries;

  /* private final CountDownTimer mTimer = new CountDownTimer( 15 * 60 * 1000,
   * 2000 ) {
   * 
   * @Override public void onTick( final long millisUntilFinished ) {
   * addValue(); }
   * 
   * @Override public void onFinish() { } }; */

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
  public void onCreate( final Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    Log.d( TAG, "onCreate" );

    mItems = new ArrayList<String>();
    mSeries = new HashMap<String, TimeSeries>();
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

    dh = DataManager.getStorageInstance();
    di = DataManager.getDataInterface();
    mPulses = di.getAllPulses();

    // mLastItemChange = new Date().getTime();
    // mItemIndex = Math.abs( RAND.nextInt( ITEMS.length ) );
  }

  @Override
  public View onCreateView( final LayoutInflater inflater,
      final ViewGroup container, final Bundle savedInstanceState )
  {
    Log.d( TAG, "onCreateView" );

    if ( Configuration.ORIENTATION_PORTRAIT == getResources()
        .getConfiguration().orientation )
    {
      mYAxisPadding = 9;
      // mRenderer.setYLabelsColor( 8, Color.RED );
      mRenderer.setYLabels( 15 );
    }

    final LinearLayout view = (LinearLayout) inflater.inflate(
        R.layout.fragment_daymode_chart, container, false );
    mChartView = ChartFactory.getTimeChartView( getActivity(), mDataset,
        mRenderer, TIME );
    mChartView.addZoomListener( mZoomListener, true, false );
    view.addView( mChartView, new LayoutParams( LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT ) );
    return view;
  }

  @Override
  public void onActivityCreated( final Bundle savedInstanceState )
  {
    super.onActivityCreated( savedInstanceState );
    Log.d( TAG, "onActivityCreated" );

    mViewZoomIn = getActivity().findViewById( R.id.zoom_in );
    mViewZoomOut = getActivity().findViewById( R.id.zoom_out );
    mViewZoomReset = getActivity().findViewById( R.id.zoom_reset );
    mViewZoomIn.setOnClickListener( this );
    mViewZoomOut.setOnClickListener( this );
    mViewZoomReset.setOnClickListener( this );

    TimeSeries pulseSeries = new TimeSeries( "Saved Pulses" );
    if ( mPulses.size() > 0 )
    {
      for ( Pulse p : mPulses )
      {
        pulseSeries.add( new Date( p.getTime() ), p.getValue() );
      }
    }
    mDataset.addSeries( pulseSeries );

    /* For the current pulses */
    mCurrentPulseRenderer = new XYSeriesRenderer();
    
    XYSeriesRenderer pulseRenderer = new XYSeriesRenderer();
    pulseRenderer.setDisplayChartValues( true );
    pulseRenderer.setChartValuesTextSize( 25 );
    pulseRenderer.setPointStyle( PointStyle.DIAMOND );
    pulseRenderer.setColor( Color.RED );
    pulseRenderer.setFillPoints( true );
    pulseRenderer.setLineWidth( 3 );
    
    mRenderer.addSeriesRenderer( pulseRenderer );
  }

  @Override
  public void onStop()
  {
    super.onStop();
    if ( dh != null )
    {
      Log.d( TAG, "removePulseChangedListener" );
      dh.removePulseChangedListener( this );
    }
  }

  private void scrollGraph( final long time )
  {
    final double[] limits = new double[] { time - TEN_SEC * mZoomLevel,
        time + TWO_SEC * mZoomLevel, mYAxisMin - mYAxisPadding,
        mYAxisMax + mYAxisPadding };
    mRenderer.setRange( limits );
  }

  @Override
  public void onClick( final View v )
  {
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

  /**
   * Pulse listener
   */
  @Override
  public void handlePulseChangedEvent( Pulse aPulse )
  {
    Log.d( TAG, "" + aPulse.getValue() );
    addPulse( aPulse );
  }
  
  private void addPulse( Pulse aPulse )
  {
    final Date now = new Date();
    final long time = now.getTime();

    mCurrentPulseSeries.add( new Date( aPulse.getTime() ), aPulse.getValue() );

    scrollGraph( time );
    mChartView.repaint();
  }
}
