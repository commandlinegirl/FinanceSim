package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupInvestmentSavAcct extends ListInvestmentSavAcct {
 
	private ArrayList<ListChildInvestmentSavAcct> items;
	
	
	public ArrayList<ListChildInvestmentSavAcct> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildInvestmentSavAcct> items) {
		this.items = items;
	}
	
	
}