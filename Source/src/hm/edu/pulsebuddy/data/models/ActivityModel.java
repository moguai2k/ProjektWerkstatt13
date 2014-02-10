package hm.edu.pulsebuddy.data.models;

import java.io.Serializable;
import java.util.Date;

import org.garret.perst.TimeSeries;

public class ActivityModel implements Serializable, TimeSeries.Tick {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 6459427843116191898L;

	public enum Type {
		IN_VEHICLE, ON_BICYCLE, ON_FOOT, STILL, TILTING, UNKNOWN
	}

	private int type;
	private int confidence;
	private long timestamp;

	public Type getType() {
		return IntToType(this.type);
	}

	public void setType(Type aType) {
		this.type = TypeToInt(aType);
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public void setTime(long aTime) {
		this.timestamp = aTime;
	}

	@Override
	public String toString() {
		return "ActivityModel [type=" + IntToType(type) + ", confidence="
				+ confidence + ", timestamp=" + new Date(timestamp).toString()
				+ "]";
	}

	@Override
	public long getTime() {
		return timestamp;
	}

	private int TypeToInt(Type aType) {
		int type;
		switch (aType) {
		case IN_VEHICLE:
			type = 1;
			break;

		case ON_BICYCLE:
			type = 2;
			break;

		case ON_FOOT:
			type = 3;
			break;

		case STILL:
			type = 4;
			break;

		case TILTING:
			type = 5;
			break;

		case UNKNOWN:
			type = 0;
			break;

		default:
			type = 0;
			break;
		}
		return type;
	}

	private Type IntToType(int aInt) {
		Type type = Type.UNKNOWN;
		switch (aInt) {
		case 0:
			break;

		case 1:
			type = Type.IN_VEHICLE;
			break;

		case 2:
			type = Type.ON_BICYCLE;
			break;

		case 3:
			type = Type.ON_FOOT;
			break;

		case 4:
			type = Type.STILL;
			break;

		case 5:
			type = Type.TILTING;
			break;

		default:
			break;
		}
		return type;
	}
}
