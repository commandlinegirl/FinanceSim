package com.codelemma.finances;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListAdapterNetWorth extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ListGroupNetWorth> groups;
	
	public ListAdapterNetWorth(Context context, ArrayList<ListGroupNetWorth> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ListChildNetWorth item, ListGroupNetWorth group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ListChildNetWorth> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ListChildNetWorth> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListChildNetWorth group = (ListChildNetWorth) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, ListNetWorth group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_networth, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.date);
		tv.setText(group.getDate());
														
		tv = (TextView) view.findViewById(R.id.savings);
		tv.setText(group.getSavings());
		
		tv = (TextView) view.findViewById(R.id.outstanding_debts);
		tv.setText(group.getOutstandingDebts());

		tv = (TextView) view.findViewById(R.id.net_worth);
		tv.setText(group.getNetWorth());
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ListChildNetWorth> chList = groups.get(groupPosition).getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListGroupNetWorth group = (ListGroupNetWorth) getGroup(groupPosition);
		return populateView(view, group);
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
