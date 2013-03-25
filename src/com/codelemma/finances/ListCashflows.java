package com.codelemma.finances;

public abstract class ListCashflows {

	private String date;
	private String income;
	private String capital_gains;
	private String expenses;
	private String debt_rates;
	private String investment_rates;	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void setIncome(String income) {
		this.income = income;
	}
	
	public String getIncome() {
		return income;
	}
	
	public void setCapitalGains(String capital_gains) {
		this.capital_gains = capital_gains;
	}	
	
	public String getCapitalGains() {
		return capital_gains;
	}
	
	public void setExpenses(String expenses) {
		this.expenses = expenses;
	}	
	
	public String getExpenses() {
		return expenses;
	}
	
	public void setDebtRates(String debt_rates) {
		this.debt_rates = debt_rates;
	}	
	
	public String getDebtRates() {
		return debt_rates;
	}
	
	public void setInvestmentRates(String investment_rates) {
		this.investment_rates = investment_rates;
	}	
	
	public String getInvestmentRates() {
		return investment_rates;
	}
}
