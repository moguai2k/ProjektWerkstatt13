package hm.edu.pulsebuddy.data;

import java.util.ArrayList;
import java.util.List;

public class SportModeWorkoutPlanData
{

  public String string;
  public final List<String> children = new ArrayList<String>();

  public SportModeWorkoutPlanData( String string )
  {
    this.string = string;
  }
}
