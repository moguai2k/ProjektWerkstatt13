package hm.edu.pulsebuddy.data.models;

import java.io.Serializable;
import java.util.Date;

import org.garret.perst.TimeSeries;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class Pulse implements TimeSeries.Tick, Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 2025472028539744879L;

	private long timestamp;
	private int value;

	public Pulse() {
	}

	public Pulse(int aPulse) {
		this.timestamp = new Date().getTime();
		this.value = aPulse;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public long getTime() {
		return timestamp;
	}

	public void setTime(long aTime) {
		this.timestamp = aTime;
	}

	@Override
	public String toString() {
		return "Pulse [date=" + new Date(timestamp).toString() + ", value="
				+ value + "]";
	}

}
