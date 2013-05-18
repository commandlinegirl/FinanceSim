package com.codelemma.finances;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListAdapterInvestmentSavAcct extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ListGroupInvestmentSavAcct> groups;
	
	public ListAdapterInvestmentSavAcct(Context context, ArrayList<ListGroupInvestmentSavAcct> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ListChildInvestmentSavAcct item, ListGroupInvestmentSavAcct group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ListChildInvestmentSavAcct> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ListChildInvestmentSavAcct> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListChildInvestmentSavAcct group = (ListChildInvestmentSavAcct) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, ListInvestmentSavAcct group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_investmentsavacct, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.date);
		tv.setText(group.getDate().toString());
		
		tv = (TextView) view.findViewById(R.id.contribution);
		tv.setText(group.getContribution().toString());
		
		tv = (TextView) view.findViewById(R.id.tax);
		tv.setText(group.getTax().toString());
		
		tv = (TextView) view.findViewById(R.id.net_interests);
		tv.setText(group.getNetInterests().toString());
		
		tv = (TextView) view.findViewById(R.id.amount);
		tv.setText(group.getAmount().toString());
						
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ListChildInvestmentSavAcct> chList = groups.get(groupPosition).getItems();
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
		ListGroupInvestmentSavAcct group = (ListGroupInvestmentSavAcct) getGroup(groupPosition);
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

