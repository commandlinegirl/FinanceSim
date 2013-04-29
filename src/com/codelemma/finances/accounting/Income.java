package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class Income implements AccountingElement {
	
	private static int next_id = 0;
	private final int id = next_id;
	
	public Income() {
		next_id++;		
	}
	
    public abstract  void initialize();     
    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {}
	public abstract BigDecimal getNetIncome();     	
	public abstract String toString();
	public abstract BigDecimal getInitRiseRate();
	public abstract BigDecimal getGrossIncome();
	public abstract BigDecimal getTax();
	public abstract void setInvestment401k(Investment401k investment401k);
	public abstract Investment401k getInvestment401k();
	public abstract void setValuesBeforeCalculation();
	
	@Override
	public void addToAccount(Account account) {
		account.justAddIncome(this);
	}
	
	@Override 
	public int getId() {
		return id;
	}	
}
