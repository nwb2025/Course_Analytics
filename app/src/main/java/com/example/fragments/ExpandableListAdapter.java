package com.example.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter  extends BaseExpandableListAdapter {

    private Context contex;
    private List<String> header_list;
    private static final String TAG = "ExpandableListAdapter";

    private HashMap<String, List<String>> list_data_child;


    public ExpandableListAdapter(Context contex, List<String> listheader, HashMap<String, List<String>> listChildData) {
        this.contex = contex;
        this.header_list = listheader;
        this.list_data_child = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.header_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.list_data_child.get(this.header_list.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.header_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.list_data_child.get(this.header_list.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        if(convertView == null){
            convertView = LayoutInflater.from(this.contex).inflate(R.layout.list_group,null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.header_tv);

        if(isExpanded){
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, contex.getResources().getDrawable(R.drawable.arrow_up),null);
        }else{
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, contex.getResources().getDrawable(R.drawable.arrow_down),null);
        }


        tv.setTypeface(null, Typeface.BOLD);
        tv.setText(header);
        return  convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textview_item);
        textView.setText(childText);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}


