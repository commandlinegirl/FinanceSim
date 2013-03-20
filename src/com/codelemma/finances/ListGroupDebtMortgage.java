package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupDebtMortgage extends ListDebtMortgage {
 
	private ArrayList<ListChildDebtMortgage> items;
	
	
	public ArrayList<ListChildDebtMortgage> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildDebtMortgage> items) {
		this.items = items;
	}
	
	
}