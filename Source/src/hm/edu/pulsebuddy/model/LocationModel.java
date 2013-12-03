package hm.edu.pulsebuddy.model;

import java.io.Serializable;
import java.util.Date;

public class LocationModel implements Serializable
{
  /**
   * UID
   */
  private static final long serialVersionUID = -3240980473307685029L;

  private Double latitude;
  private Double longitude;
  private Float speed;
  private Double elevation;
  private Date timestamp;

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

  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp( Date timestamp )
  {
    this.timestamp = timestamp;
  }

  @Override
  public String toString()
  {
    return "LocationModel [latitude=" + latitude + ", longitude=" + longitude
        + ", speed=" + speed + ", elevation=" + elevation + ", timestamp="
        + timestamp + "]";
  }

}
