package com.codelemma.finances;

public abstract class ListExpenseGeneric {

	private String date;
    private String net_expense;   
		
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNetExpense() {
		return net_expense;
	}
	
	public void setNetExpense(String net_expense) {
		this.net_expense = net_expense;
	}	
	
}
