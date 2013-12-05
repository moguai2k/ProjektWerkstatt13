package hm.edu.pulsebuddy.data.perst;

import java.io.Serializable;
import java.util.Date;

import org.garret.perst.TimeSeries;

public class LocationModel implements Serializable, TimeSeries.Tick
{
  /**
   * UID
   */
  private static final long serialVersionUID = -3240980473307685029L;

  private long timestamp;
  private Double latitude;
  private Double longitude;
  private Float speed;
  private Double elevation;

  public Double getLatitude()
  {
    return latitude;
  }

  public void setLatitude( Double latitude )
  {
    this.latitude = latitude;
  }

  public Double getLongitude()
  {
    return longitude;
  }

  public void setLongitude( Double longitude )
  {
    this.longitude = longitude;
  }

  public Float getSpeed()
  {
    return speed;
  }

  public void setSpeed( Float speed )
  {
    this.speed = speed;
  }

  public Double getElevation()
  {
    return elevation;
  }

  public void setElevation( Double elevation )
  {
    this.elevation = elevation;
  }
  
  public void setTime( Long aTime )
  {
    this.timestamp = aTime;
  }

  @Override
  public long getTime()
  {
    return timestamp;
  }

  @Override
  public String toString()
  {
    return "LocationModel [latitude=" + latitude + ", longitude=" + longitude
        + ", speed=" + speed + ", elevation=" + elevation + ", timestamp="
        + new Date( timestamp ).toString() + "]";
  }

}
