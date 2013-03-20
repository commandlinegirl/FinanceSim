package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupIncomeGeneric extends ListIncomeGeneric {
 
	private ArrayList<ListChildIncomeGeneric> items;	
	
	public ArrayList<ListChildIncomeGeneric> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildIncomeGeneric> items) {
		this.items = items;
	}	
}