package com.codelemma.finances;

public abstract class ListInvestmentSavAcct {

	private String tag;
	private String date;
	private String amount;
	private String contribution;
	private String tax;
	private String net_interests;
		
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public void setContribution(String contribution) {
		this.contribution = contribution;
	}
	
	public String getContribution() {
		return contribution;
	}
	
	public void setNetInterests(String net_interests) {
		this.net_interests = net_interests;
	}
	
	public String getNetInterests() {
		return net_interests;
	}
		
	public String getTax() {
		return tax;
	}
	
	public void setTax(String tax) {
		this.tax = tax;
	}	
}
