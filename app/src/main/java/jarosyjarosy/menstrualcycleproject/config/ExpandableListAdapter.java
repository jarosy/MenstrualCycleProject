package jarosyjarosy.menstrualcycleproject.config;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.activities.DayFormActivity;
import jarosyjarosy.menstrualcycleproject.activities.ListActivity;
import jarosyjarosy.menstrualcycleproject.activities.MainActivity;
import jarosyjarosy.menstrualcycleproject.activities.TableActivity;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    private DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private List<Cycle> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Cycle, List<Day>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Cycle> listDataHeader,
                                 HashMap<Cycle, List<Day>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Day day = (Day) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.listDay);

        txtListChild.setText(day.getDayOfCycle() + " dzień | " + day.getTemperature() + "℃");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final Cycle cycle = (Cycle) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.listCycle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(appDateFormat.print(cycle.getStartDate()) + " - " +  ((cycle.getEndDate() != null ) ? appDateFormat.print(cycle.getEndDate()) : "obecnie"));

        Button previewBtn = (Button)convertView.findViewById(R.id.previewButton);
        previewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(), TableActivity.class);
                Bundle b = new Bundle();
                b.putLong("cycleId", cycle.getCycleId());
                intent.putExtras(b);
                parent.getContext().startActivity(intent);
            }
        });

        Button addDayButton = (Button)convertView.findViewById(R.id.addDayButton);
        addDayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(), DayFormActivity.class);
                Bundle b = new Bundle();
                b.putLong("cycleId", cycle.getCycleId());
                intent.putExtras(b);
                parent.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}