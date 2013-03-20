package com.codelemma.finances;

public abstract class ListIncomeGeneric {

	private String date;
	private String gross_income;
    private String tax;
    private String net_income;   
		
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGrossIncome() {
		return gross_income;
	}
	
	public void setGrossIncome(String gross_income) {
		this.gross_income = gross_income;
	}	
	
	public String getTax() {
		return tax;
	}
	
	public void setTax(String tax) {
		this.tax = tax;
	}
	
	public String getNetIncome() {
		return net_income;
	}
	
	public void setNetIncome(String net_income) {
		this.net_income = net_income;
	}	
	
}
