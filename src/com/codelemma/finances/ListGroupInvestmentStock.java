package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupInvestmentStock extends ListInvestmentStock {
 
	private ArrayList<ListChildInvestmentStock> items;
	
	
	public ArrayList<ListChildInvestmentStock> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildInvestmentStock> items) {
		this.items = items;
	}
	
	
}