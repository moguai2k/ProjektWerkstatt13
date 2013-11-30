package hm.edu.pulsebuddy.model;

import java.io.Serializable;
import java.util.Date;

public class ActivityModel implements Serializable
{
  /**
   * UID
   */
  private static final long serialVersionUID = 6459427843116191898L;

  public enum Type
  {
    IN_VEHICLE, ON_BICYCLE, ON_FOOT, STILL, TILTING, UNKNOWN
  }

  private Type type;
  private int confidence;
  private Date timestamp;

  public ActivityModel( Type aType, int aConfidence )
  {
    type = aType;
    confidence = aConfidence;
    timestamp = new Date();
  }

  public Type getType()
  {
    return type;
  }

  public void setType( Type type )
  {
    this.type = type;
  }

  public int getConfidence()
  {
    return confidence;
  }

  public void setConfidence( int confidence )
  {
    this.confidence = confidence;
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
    return "ActivityModel [type=" + type + ", confidence=" + confidence
        + ", timestamp=" + timestamp + "]";
  }

}
