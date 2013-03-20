package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupExpenseGeneric extends ListExpenseGeneric {
 
	private ArrayList<ListChildExpenseGeneric> items;	
	
	public ArrayList<ListChildExpenseGeneric> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildExpenseGeneric> items) {
		this.items = items;
	}	
}