package hm.edu.pulsebuddy.trainingsplan;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.R.layout;
import hm.edu.pulsebuddy.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class TrainingsplanActivity extends Activity
{
  
  List<String> groupList;
  List<String> childList;
  Map<String, List<String>> planCollection;
  ExpandableListView expListView;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_trainingsplan );
    getActionBar().setDisplayHomeAsUpEnabled( true );
    
    createGroupList();
    createCollection();
    
    expListView = (ExpandableListView) findViewById(R.id.trainingsplan_list);
    final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
            this, groupList, planCollection);
    expListView.setAdapter(expListAdapter);
    
    
//    expListView.setOnChildClickListener(new OnChildClickListener() {
//      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//      {
//          final String selected = (String) expListAdapter.getChild( groupPosition, childPosition);
//          Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();
//          return true;
//      }
//    });
  }

  
  public boolean onOptionsItemSelected( MenuItem item )
  {
    switch ( item.getItemId() )
    {
      case R.id.home:
        NavUtils.navigateUpFromSameTask( this );
        return true;
      default:
        return super.onOptionsItemSelected( item );
    }
  }
  
  private void createGroupList() {
    groupList = new ArrayList<String>();
    groupList.add("1. Woche");
    groupList.add("2. Woche");
    groupList.add("3. Woche");
    groupList.add("4. Woche");
    groupList.add("5. Woche");
  }
  
  private void createCollection() {
    // preparing weeks collection(child)
    String[] week1 = { "Tag 1: 130bpm", "Tag 2: 136bpm", "Tag 3: 140bpm", "Tag 4: Pause", "Tag 5: 130bpm", "Tag 6: 136bpm", "Tag 7: 140bpm" };
    String[] week2 = { "Tag 1", "Tag 2", "Tag 3", "Tag 4", "Tag 5", "Tag 6", "Tag 7" };


    planCollection = new LinkedHashMap<String, List<String>>();

    for (String plan : groupList) {
        if (plan.equals("1. Woche")) {
            loadChild(week1);
        } else if (plan.equals("2. Woche"))
            loadChild(week2);
        else
          loadChild(week2);
        
        planCollection.put(plan, childList);
    }
}
  
  private void loadChild(String[] children) {
    childList = new ArrayList<String>();
    for (String day : children)
        childList.add(day);
  }
  
  
  //Convert pixel to dip
  public int getDipsFromPixel(float pixels) {
      // Get the screen's density scale
      final float scale = getResources().getDisplayMetrics().density;
      // Convert the dps to pixels, based on density scale
      return (int) (pixels * scale + 0.5f);
  }

}
