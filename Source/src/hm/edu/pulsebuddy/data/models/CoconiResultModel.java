package hm.edu.pulsebuddy.data.models;

import java.util.Date;

import org.garret.perst.Persistent;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class CoconiResultModel extends Persistent {
	public int _id;

	private int pulse;
	private long date;

	public CoconiResultModel() {
	}

	public CoconiResultModel(int aPulse, Date aDate) {
		this.pulse = aPulse;
		this.date = aDate.getTime();
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CoconiResult [_id=" + _id + ", pulse=" + pulse + ", date="
				+ date + "]";
	}
}
