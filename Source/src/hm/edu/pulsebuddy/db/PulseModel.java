package hm.edu.pulsebuddy.db;

import java.io.Serializable;
import java.util.Date;

public class PulseModel implements Serializable
{
  
  /**
   * Unique UID
   */
  private static final long serialVersionUID = -4791072197178848364L;
  
  private long id;
  private int pulse;
  private Date dateTime;

  /**
   * Default constructor.
   */
  public PulseModel()
  {
  }

  public PulseModel( int aPulse )
  {
    this.pulse = aPulse;
    this.dateTime = new Date();
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public Date getDateTime()
  {
    return dateTime;
  }

  public void setDateTime( Date dateTime )
  {
    this.dateTime = dateTime;
  }

  public int getPulse()
  {
    return pulse;
  }

  public void setPulse( int pulse )
  {
    this.pulse = pulse;
  }
}
