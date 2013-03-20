package com.codelemma.finances;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListAdapterInvestmentStock extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ListGroupInvestmentStock> groups;
	
	public ListAdapterInvestmentStock(Context context, ArrayList<ListGroupInvestmentStock> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ListChildInvestmentStock item, ListGroupInvestmentStock group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ListChildInvestmentStock> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ListChildInvestmentStock> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListChildInvestmentStock group = (ListChildInvestmentStock) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, ListInvestmentStock group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_investmentstock, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.date);
		tv.setText(group.getDate().toString());
				
		tv = (TextView) view.findViewById(R.id.dividends);
		tv.setText(group.getDividends().toString());
		
		tv = (TextView) view.findViewById(R.id.capital_gain_yield);
		tv.setText(group.getCapitalGainYield().toString());
		
		tv = (TextView) view.findViewById(R.id.tax);
		tv.setText(group.getTax().toString());
	
		tv = (TextView) view.findViewById(R.id.amount);
		tv.setText(group.getAmount().toString());
						
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ListChildInvestmentStock> chList = groups.get(groupPosition).getItems();
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
		ListGroupInvestmentStock group = (ListGroupInvestmentStock) getGroup(groupPosition);
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

