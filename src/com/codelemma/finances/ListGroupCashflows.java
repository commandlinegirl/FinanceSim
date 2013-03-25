package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupCashflows extends ListCashflows {
 
	private ArrayList<ListChildCashflows> items;
	
	
	public ArrayList<ListChildCashflows> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildCashflows> items) {
		this.items = items;
	}
	
	
}