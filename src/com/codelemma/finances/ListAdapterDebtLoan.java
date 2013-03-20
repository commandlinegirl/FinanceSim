package com.codelemma.finances;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListAdapterDebtLoan extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ListGroupDebtLoan> groups;
	
	public ListAdapterDebtLoan(Context context, ArrayList<ListGroupDebtLoan> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ListChildDebtLoan item, ListGroupDebtLoan group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ListChildDebtLoan> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ListChildDebtLoan> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ListChildDebtLoan group = (ListChildDebtLoan) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, ListDebtLoan group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_debtloan, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.date);
		tv.setText(group.getDate().toString());
				
		tv = (TextView) view.findViewById(R.id.monthly_payment);
		tv.setText(group.getMonthlyPayment().toString());
										
		tv = (TextView) view.findViewById(R.id.interests_paid);
		tv.setText(group.getInterestsPaid().toString());
		
		tv = (TextView) view.findViewById(R.id.total_interests);
		tv.setText(group.getTotalInterests().toString());

		tv = (TextView) view.findViewById(R.id.principal_paid);
		tv.setText(group.getPrincipalPaid().toString());
		
		tv = (TextView) view.findViewById(R.id.remaining_amount);
		tv.setText(group.getRemainingAmount().toString());


		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ListChildDebtLoan> chList = groups.get(groupPosition).getItems();
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
		ListGroupDebtLoan group = (ListGroupDebtLoan) getGroup(groupPosition);
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
