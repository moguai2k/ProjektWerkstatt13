package hm.edu.pulsebuddy.graph;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;


public class TestGraph implements SensorEventListener
{
    private SensorManager sensorMgr = null;
	private Sensor orSensor = null;
	private Redrawer redrawer;
    private static final int HISTORY_SIZE = 300;
    private XYPlot aprHistoryPlot = null;
    private SimpleXYSeries rollHistorySeries = null;

	@SuppressWarnings("deprecation")
	public void graphIt() {
        rollHistorySeries = new SimpleXYSeries("Puls");
        rollHistorySeries.useImplicitXVals();

        aprHistoryPlot.setRangeBoundaries(-75, 250, BoundaryMode.FIXED);
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        aprHistoryPlot.addSeries(rollHistorySeries,
                new LineAndPointFormatter(
                        Color.rgb(200, 100, 100), null, null, null));
        aprHistoryPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        aprHistoryPlot.setDomainStepValue(HISTORY_SIZE/10);
        aprHistoryPlot.setTicksPerRangeLabel(3);
        aprHistoryPlot.setDomainLabel("Sample Index");
        aprHistoryPlot.getDomainLabelWidget().pack();
        aprHistoryPlot.setRangeLabel("Angle (Degs)");
        aprHistoryPlot.getRangeLabelWidget().pack();

        aprHistoryPlot.setRangeValueFormat(new DecimalFormat("#"));
        aprHistoryPlot.setDomainValueFormat(new DecimalFormat("#"));

        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ORIENTATION)) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                orSensor = sensor;
            }
        }

        if (orSensor == null) {
            System.out.println("Failed to attach to orSensor.");
          sensorMgr.unregisterListener(this);
        }

        sensorMgr.registerListener(this, orSensor, SensorManager.SENSOR_DELAY_UI);
        
        redrawer = new Redrawer(Arrays.asList(new Plot[]{aprHistoryPlot}),100, false);
        redrawer.start();
	}
    
    @Override
    public synchronized void onSensorChanged(SensorEvent sensorEvent) {
        if (rollHistorySeries.size() > HISTORY_SIZE) {
            rollHistorySeries.removeFirst();
        }

		Random random = new Random();
        rollHistorySeries.addLast(null, random.nextInt(200));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    
    public SensorManager getSensorMgr() {
		return sensorMgr;
	}

	public void setSensorMgr(SensorManager sensorMgr) {
		this.sensorMgr = sensorMgr;
	}
	
    public XYPlot getAprHistoryPlot() {
		return aprHistoryPlot;
	}

	public void setAprHistoryPlot(XYPlot aprHistoryPlot) {
		this.aprHistoryPlot = aprHistoryPlot;
	}
	
    public Redrawer getRedrawer() {
		return redrawer;
	}

	public void setRedrawer(Redrawer redrawer) {
		this.redrawer = redrawer;
	}
}