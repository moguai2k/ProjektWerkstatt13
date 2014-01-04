package hm.edu.pulsebuddy.trainingsplan;

import hm.edu.pulsebuddy.R;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
  
  private Activity context;
  private Map<String, List<String>> planCollections;
  private List<String> trainingsplan;

  public ExpandableListAdapter(Activity context, List<String> trainingsplan,
          Map<String, List<String>> planCollections) {
      this.context = context;
      this.planCollections = planCollections;
      this.trainingsplan = trainingsplan;
  }

  @Override
  public Object getChild( int groupPosition, int childPosition )
  {
    return planCollections.get(trainingsplan.get(groupPosition)).get(childPosition);
  }

  @Override
  public long getChildId( int groupPosition, int childPosition )
  {
    return childPosition;
  }

  @Override
  public View getChildView( final int groupPosition, final int childPosition,
      boolean isLastChild, View convertView, ViewGroup parent)
  {
    final String plan = (String) getChild(groupPosition, childPosition);
    LayoutInflater inflater = context.getLayoutInflater();
    
    if (convertView == null) {
        convertView = inflater.inflate(R.layout.trainingsplan_child_item, null);
    }

    TextView item = (TextView) convertView.findViewById(R.id.trainingsplan);
    item.setText(plan);
    return convertView;
  }

  @Override
  public int getChildrenCount( int groupPosition )
  {
    return planCollections.get(trainingsplan.get(groupPosition)).size();
  }

  @Override
  public Object getGroup( int groupPosition )
  {
    return trainingsplan.get(groupPosition);
  }

  @Override
  public int getGroupCount()
  {
    return trainingsplan.size();
  }

  @Override
  public long getGroupId( int groupPosition )
  {
    return groupPosition;
  }

  @Override
  public View getGroupView( int groupPosition, boolean isExpanded,
      View convertView, ViewGroup parent )
  {
    String planName = (String) getGroup(groupPosition);
    if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.trainingsplan_group_item,
                null);
    }
    TextView item = (TextView) convertView.findViewById(R.id.trainingsplan);
    item.setTypeface(null, Typeface.BOLD);
    item.setText(planName);
    return convertView;
  }

  @Override
  public boolean hasStableIds()
  {
    return true;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition )
  {
    return true;
  }

}
