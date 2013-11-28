package hm.edu.pulsebuddy.db;

import java.util.Date;

public class PulseModel
{

  private long id;
  private int pulse;
  private Date dateTime;

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
