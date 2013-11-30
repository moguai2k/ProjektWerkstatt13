package hm.edu.pulsebuddy.db;

import android.content.Context;

public class DataManager
{
  private static DataStorage _instance;

  private DataManager()
  {
  }

  public static DataStorage getStorageInstance( Context context )
  {
    if ( _instance == null )
    {
      _instance = new DataStorage( context );
    }
    return _instance;
  }

  public static DataStorage getStorageInstance()
  {
    if ( _instance != null )
    {
      return _instance;
    }
    return null;
  }

}
