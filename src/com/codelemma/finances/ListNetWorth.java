package com.codelemma.finances;

public abstract class ListNetWorth {

	private String date;
	private String savings;
	private String outstanding_debts;
	private String net_worth;
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void setSavings(String savings) {
		this.savings = savings;
	}
	
	public String getSavings() {
		return savings;
	}
	
	public void setOutstandingDebts(String outstanding_debts) {
		this.outstanding_debts = outstanding_debts;
	}	
	
	public String getOutstandingDebts() {
		return outstanding_debts;
	}

	public void setNetWorth(String net_worth) {
		this.net_worth = net_worth;
	}
	
	public String getNetWorth() {
		return net_worth;
	}	
}
