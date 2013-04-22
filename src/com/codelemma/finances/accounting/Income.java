package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class Income implements NamedValue {
   
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {}
	public abstract BigDecimal getNetIncome();     	
	public abstract String toString();
	public abstract BigDecimal getInitRiseRate();
	public abstract BigDecimal getGrossIncome();
	public abstract BigDecimal getTax();
	public abstract void setInvestment401k(Investment401k investment401k);
	public abstract Investment401k getInvestment401k();
	public abstract void setValuesBeforeCalculation();
}
