package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.models.Pulse;

import org.garret.perst.TimeSeries;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class PulseBlock extends TimeSeries.Block {
	private Pulse[] pulses;

	static final int N_ELEMS_PER_BLOCK = 100;

	public TimeSeries.Tick[] getTicks() {
		if (pulses == null) {
			pulses = new Pulse[N_ELEMS_PER_BLOCK];
			for (int i = 0; i < N_ELEMS_PER_BLOCK; i++) {
				pulses[i] = new Pulse();
			}
		}
		return pulses;
	}
}
