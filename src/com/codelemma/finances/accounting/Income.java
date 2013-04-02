package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class Income implements NamedValue {
   
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int month) {}
	public abstract BigDecimal getNetIncome();     	
	public abstract String toString();
	public abstract BigDecimal getInitAmount();
	public abstract BigDecimal getInitRiseRate();
	public abstract void setInvestment401k(Investment401k investment401k);
	public abstract Investment401k getInvestment401k();
}
