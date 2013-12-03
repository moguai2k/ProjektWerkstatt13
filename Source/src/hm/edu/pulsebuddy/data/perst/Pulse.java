package hm.edu.pulsebuddy.data.perst;

import java.io.Serializable;
import java.util.Date;

import org.garret.perst.TimeSeries;

public class Pulse implements TimeSeries.Tick, Serializable
{
  /**
   * UID
   */
  private static final long serialVersionUID = 2025472028539744879L;

  public long date;
  public int value;

  public long getTime()
  {
    return date;
  }

  @Override
  public String toString()
  {
    return "Pulse [date=" + new Date( date ).toString() + ", value=" + value
        + "]";
  }

}
