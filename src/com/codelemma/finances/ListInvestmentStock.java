package com.codelemma.finances;

public abstract class ListInvestmentStock {

	private String date;
	private String amount;
	private String dividends;
	private String tax;
	private String capital_gain_yield;
		
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
	
	public void setDividends(String dividends) {
		this.dividends = dividends;
	}
	
	public String getDividends() {
		return dividends;
	}
	
	public void setCapitalGainYield(String capital_gain_yield) {
		this.capital_gain_yield = capital_gain_yield;
	}
	
	public String getCapitalGainYield() {
		return capital_gain_yield;
	}
		
	public String getTax() {
		return tax;
	}
	
	public void setTax(String tax) {
		this.tax = tax;
	}	
}
