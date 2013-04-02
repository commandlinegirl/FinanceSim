package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupInvestmentCheckAcct extends ListInvestmentCheckAcct {
 
	private ArrayList<ListChildInvestmentCheckAcct> items;
	
	
	public ArrayList<ListChildInvestmentCheckAcct> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildInvestmentCheckAcct> items) {
		this.items = items;
	}
	
	
}