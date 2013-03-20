package com.codelemma.finances;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListAdapterInvestment401k extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ListGroupInvestment401k> groups;
	
	public ListAdapterInvestment401k(Context context, ArrayList<ListGroupInvestment401k> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ListChildInvestment401k item, ListGroupInvestment401k group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ListChildInvestment401k> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ListChildInvestment401k> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListChildInvestment401k group = (ListChildInvestment401k) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, ListInvestment401k group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_investment401k, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.group_date);
		tv.setText(group.getDate().toString());
		
		tv = (TextView) view.findViewById(R.id.group_salary);
		tv.setText(group.getSalary().toString());
		
		tv = (TextView) view.findViewById(R.id.group_employee_contribution);
		tv.setText(group.getEmployeeContribution().toString());
		
		tv = (TextView) view.findViewById(R.id.group_employer_contribution);
		tv.setText(group.getEmployerContribution().toString());
		
		tv = (TextView) view.findViewById(R.id.group_amount);
		tv.setText(group.getAmount().toString());
						
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ListChildInvestment401k> chList = groups.get(groupPosition).getItems();
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
		ListGroupInvestment401k group = (ListGroupInvestment401k) getGroup(groupPosition);
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

