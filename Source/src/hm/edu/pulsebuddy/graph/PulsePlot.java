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

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class PulsePlot implements PulseChangedListener {
	private Redrawer redrawer;
	private static final int SECONDS = 240;
	private static final int PULSE = 160;
	private XYPlot aprHistoryPlot = null;
	private SimpleXYSeries rollHistorySeries = null;
	private TextView tv = null;
	private PulseOptimizer dopt = null;

	private DataHandler ds = null;

	private Queue<Integer> pulseValues = new LinkedList<Integer>();

	private double lastPulse = 0;
	private double nextPulse = 0;

	public PulsePlot(XYPlot aprHistoryPlot, TextView tv, Redrawer redrawer) {
		this.aprHistoryPlot = aprHistoryPlot;
		this.tv = tv;
		this.redrawer = redrawer;

		ds = DataManager.getStorageInstance();
		if (ds != null)
			ds.addPulseChangedListener(this);

		graphIt();
	}

	public void graphIt() {
		rollHistorySeries = new SimpleXYSeries("Puls");
		rollHistorySeries.useImplicitXVals();

		// cosmetic
		aprHistoryPlot.setDrawDomainOriginEnabled(false);
		aprHistoryPlot.setBackgroundPaint(null);
		aprHistoryPlot.getGraphWidget().setBackgroundPaint(null);
		aprHistoryPlot.getGraphWidget().setGridBackgroundPaint(null);
		aprHistoryPlot.getGraphWidget().setDomainLabelPaint(null);
		aprHistoryPlot.getGraphWidget().setDomainOriginLabelPaint(null);
		aprHistoryPlot.getGraphWidget().setDomainGridLinePaint(null);
		aprHistoryPlot.getGraphWidget().setDomainOriginLinePaint(null);
		aprHistoryPlot.getGraphWidget().setRangeLabelWidth(50);
		aprHistoryPlot.getGraphWidget().getRangeOriginLinePaint() // lines
				.setColor(Color.rgb(192, 192, 192));
		aprHistoryPlot.getGraphWidget().getRangeOriginLabelPaint() // label/line
																	// zero
				.setColor(Color.rgb(192, 192, 192));
		aprHistoryPlot.getGraphWidget().getRangeLabelPaint() // labeles
				.setColor(Color.rgb(192, 192, 192));
		aprHistoryPlot.setRangeBoundaries(25, PULSE, BoundaryMode.FIXED);
		aprHistoryPlot.setDomainBoundaries(0, SECONDS, BoundaryMode.FIXED);
		LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(
				Color.rgb(204, 0, 0), null, null, null);
		Paint paint = lineAndPointFormatter.getLinePaint();
		paint.setStrokeWidth(12);
		lineAndPointFormatter.setLinePaint(paint);
		aprHistoryPlot.addSeries(rollHistorySeries, lineAndPointFormatter);
		aprHistoryPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
		aprHistoryPlot.setDomainStepValue(SECONDS / 6);
		aprHistoryPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
		aprHistoryPlot.setRangeStepValue(10.0d);
		aprHistoryPlot.setRangeValueFormat(new DecimalFormat("#"));
		aprHistoryPlot.setDomainValueFormat(new DecimalFormat("#"));
		aprHistoryPlot.getDomainLabelWidget().pack();
		aprHistoryPlot.setRangeLabel("Puls");
		aprHistoryPlot.getRangeLabelWidget().pack();
		aprHistoryPlot.getLayoutManager().remove(
				aprHistoryPlot.getLegendWidget());

		// start drawing
		redrawer = new Redrawer(Arrays.asList(new Plot[] { aprHistoryPlot }),
				100, false);
		redrawer.start();

		aprHistoryPlot.redraw();

		dopt = new PulseOptimizer();
		dopt.execute();
	}

	@Override
	public void handlePulseChangedEvent(Pulse aPulse) {
		if (rollHistorySeries.size() > SECONDS)
			rollHistorySeries.removeFirst();

		pulseValues.add(aPulse.getValue());
	}

	public void setResume(boolean resume) {
		dopt.setResume(resume);
	}

	/**
	 * pulse optimizer
	 */
	private class PulseOptimizer extends AsyncTask<Void, Integer, String> {
		public boolean resume = true;

		@Override
		protected String doInBackground(Void... params) {
			int steps = 10;
			int timeBetweenSteps = 1000 / steps;

			while (resume) {
				if (nextPulse == 0) {
					if (pulseValues.peek() != null)
						nextPulse = pulseValues.poll();
				} else {
					if (pulseValues.peek() != null) {
						double avgPulseSteps = 0.0;

						lastPulse = nextPulse;
						nextPulse = pulseValues.poll();
						double currentPulse = lastPulse;

						int minPulse = (int) ((int) (lastPulse / 10) * 10) - 25;
						int maxPulse = (int) ((int) (lastPulse / 10) * 10) + 25;
						aprHistoryPlot.setRangeBoundaries(minPulse, maxPulse,
								BoundaryMode.FIXED);

						if (nextPulse > lastPulse) { // +pulse
							avgPulseSteps = (nextPulse - lastPulse) / steps;
						} else { // -pulse
							avgPulseSteps = (lastPulse - nextPulse) / steps;
						}

						publishProgress((int) currentPulse);

						for (int i = 0; i < 9; i++) {
							if (nextPulse > lastPulse) { // +pulse
								currentPulse += avgPulseSteps;
							} else { // -pulse
								currentPulse -= avgPulseSteps;
							}

							publishProgress((int) ((double) Math
									.round(currentPulse * 10) / 10));

							try {
								Thread.sleep(timeBetweenSteps);
							} catch (InterruptedException e) {
								//
							}
						}
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onProgressUpdate(Integer... aPulse) {
			tv.setText("" + aPulse[0]);

			if (rollHistorySeries.size() > SECONDS)
				rollHistorySeries.removeFirst();

			rollHistorySeries.addLast(null,
					(int) ((double) Math.round(aPulse[0] * 10) / 10));
		}

		@Override
		protected void onPostExecute(String result) {
		}

		public void setResume(boolean resume) {
			this.resume = resume;
		}

	}
}