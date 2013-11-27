package hm.edu.pulsebuddy.db;

public class StorageLogic
{
  private int numOfPulseValuesTillPersist = 10;
  private int pulseValueCounter;

  public StorageLogic()
  {
  }
  
  public Boolean pulseToBeSaved()
  {
    if ( this.pulseValueCounter < numOfPulseValuesTillPersist )
    {
      this.pulseValueCounter++;
      return false;
    }
    else
    {
      this.pulseValueCounter = 0;
      return true;
    }
      
  }

}
