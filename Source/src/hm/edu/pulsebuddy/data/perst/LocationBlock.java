package hm.edu.pulsebuddy.data.perst;

import hm.edu.pulsebuddy.data.models.LocationModel;

import org.garret.perst.TimeSeries;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class LocationBlock extends TimeSeries.Block {
	private LocationModel[] locations;

	static final int N_ELEMS_PER_BLOCK = 100;

	public TimeSeries.Tick[] getTicks() {
		if (locations == null) {
			locations = new LocationModel[N_ELEMS_PER_BLOCK];
			for (int i = 0; i < N_ELEMS_PER_BLOCK; i++) {
				locations[i] = new LocationModel();
			}
		}
		return locations;
	}
}
