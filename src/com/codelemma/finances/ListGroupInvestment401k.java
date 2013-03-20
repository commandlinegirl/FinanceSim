package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupInvestment401k extends ListInvestment401k {
 
	private ArrayList<ListChildInvestment401k> items;
	
	
	public ArrayList<ListChildInvestment401k> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildInvestment401k> items) {
		this.items = items;
	}
	
	
}