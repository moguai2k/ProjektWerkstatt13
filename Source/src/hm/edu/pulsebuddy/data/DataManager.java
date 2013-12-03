package hm.edu.pulsebuddy.data;

import android.content.Context;

public class DataManager
{
  private static DataHandler _instance;

  private DataManager()
  {
  }

  public static DataHandler getStorageInstance( Context context )
  {
    if ( _instance == null )
    {
      _instance = new DataHandler( context );
    }
    return _instance;
  }

  public static DataHandler getStorageInstance()
  {
    if ( _instance != null )
    {
      return _instance;
    }
    return null;
  }

}
