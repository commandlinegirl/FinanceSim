package com.codelemma.finances;

import java.util.ArrayList;

public class ListGroupDebtLoan extends ListDebtLoan {
 
	private ArrayList<ListChildDebtLoan> items;
	
	
	public ArrayList<ListChildDebtLoan> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ListChildDebtLoan> items) {
		this.items = items;
	}
	
	
}