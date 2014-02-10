package hm.edu.pulsebuddy.graph;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.data.DataHandler;
import hm.edu.pulsebuddy.data.DataInterface;
import hm.edu.pulsebuddy.data.DataManager;
import hm.edu.pulsebuddy.data.models.Pulse;

import java.util.ArrayList;
import java.util.Date;

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

public class DaymodeFragment extends Fragment implements OnClickListener {
	private final static String TAG = "Daymode";

	// private static Random RAND = new Random();
	private static final String TIME = "H:mm:ss";

	private View mViewZoomIn;
	private View mViewZoomOut;
	private View mViewZoomReset;
	private GraphicalView mChartView;
	private XYMultipleSeriesRenderer mRenderer;
	private XYMultipleSeriesDataset mDataset;
	private TimeSeries pulseSeries;
	private double minXRange;
	private double maxXRange;

	private DataInterface di;
	private DataHandler dh;
	private ArrayList<Pulse> mPulses;

	private final ZoomListener mZoomListener = new ZoomListener() {
		@Override
		public void zoomReset() {
			double maxX = pulseSeries.getMaxX();
			double minX = pulseSeries.getMinX();
			double minY = pulseSeries.getMinY();
			double maxY = pulseSeries.getMaxY();

			maxXRange = pulseSeries.getMaxX();
			minXRange = pulseSeries.getMinX();

			final double[] limits = new double[] { minX, maxX, minY, maxY };
			mRenderer.setRange(limits); // minX, maxX, minY, maxY
		}

		@Override
		public void zoomApplied(final ZoomEvent event) {
			double diffX = (maxXRange - minXRange) / 4;

			if (event.isZoomIn()) {
				minXRange += diffX;
				maxXRange -= diffX;
				scrollGraph();
			} else // zoomOut
			{
				minXRange -= diffX;
				maxXRange += diffX;
				scrollGraph();
			}
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setLabelsColor(Color.RED);
		mRenderer.setAxesColor(Color.RED);
		mRenderer.setGridColor(Color.rgb(136, 136, 136));
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setApplyBackgroundColor(true);

		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setXLabelsColor(Color.RED);

		mRenderer.setLegendTextSize(20);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setPointSize(8);
		mRenderer.setMargins(new int[] { 20, 20, 20, 20 });

		mRenderer.setFitLegend(true);
		mRenderer.setShowGrid(true);
		mRenderer.setZoomEnabled(true);
		mRenderer.setExternalZoomEnabled(true);
		mRenderer.setAntialiasing(true);
		mRenderer.setInScroll(true);

		dh = DataManager.getStorageInstance();
		di = DataManager.getDataInterface();
		mPulses = di.getAllPulses();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");

		if (Configuration.ORIENTATION_PORTRAIT == getResources()
				.getConfiguration().orientation) {
			mRenderer.setYLabels(15);
		}

		final LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.fragment_daymode_chart, container, false);
		mChartView = ChartFactory.getTimeChartView(getActivity(), mDataset,
				mRenderer, TIME);
		mChartView.addZoomListener(mZoomListener, true, false);
		view.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");

		mViewZoomIn = getActivity().findViewById(R.id.zoom_in);
		mViewZoomOut = getActivity().findViewById(R.id.zoom_out);
		mViewZoomReset = getActivity().findViewById(R.id.zoom_reset);
		mViewZoomIn.setOnClickListener(this);
		mViewZoomOut.setOnClickListener(this);
		mViewZoomReset.setOnClickListener(this);

		pulseSeries = new TimeSeries("Saved Pulses");
		if (mPulses.size() > 0) {
			for (Pulse p : mPulses) {
				pulseSeries.add(new Date(p.getTime()), p.getValue());
			}
		}
		mDataset.addSeries(pulseSeries);

		XYSeriesRenderer pulseRenderer = new XYSeriesRenderer();
		pulseRenderer.setDisplayChartValues(true);
		pulseRenderer.setChartValuesTextSize(25);
		pulseRenderer.setPointStyle(PointStyle.DIAMOND);
		pulseRenderer.setColor(Color.RED);
		pulseRenderer.setFillPoints(true);
		pulseRenderer.setLineWidth(3);

		mRenderer.addSeriesRenderer(pulseRenderer);

		maxXRange = pulseSeries.getMaxX();
		minXRange = pulseSeries.getMinX();
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
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

	private void scrollGraph() {
		double minY = pulseSeries.getMinY();
		double maxY = pulseSeries.getMaxY();

		final double[] limits = new double[] { minXRange, maxXRange, minY, maxY };
		mRenderer.setRange(limits);
	}
}
