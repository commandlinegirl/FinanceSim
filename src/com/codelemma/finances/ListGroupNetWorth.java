package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupNetWorth extends ListNetWorth {
 
	private ArrayList<ListChildNetWorth> items;
	
	
	public ArrayList<ListChildNetWorth> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildNetWorth> items) {
		this.items = items;
	}
	
	
}