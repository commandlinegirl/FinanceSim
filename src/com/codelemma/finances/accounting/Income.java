package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class Income extends AccountingElement {

    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {}
	public abstract BigDecimal getNetIncome();     	
	public abstract String toString();
	public abstract BigDecimal getInitRiseRate();
	public abstract BigDecimal getGrossIncome();
	public abstract BigDecimal getTax();
	public abstract void setInvestment401k(Investment401k investment401k);
	public abstract Investment401k getInvestment401k();
	public abstract void setInvestment401k(Investment investment);
    public abstract void setPreviousId(int id);
    public abstract int getPreviousId();

	@Override
	public void addToAccount(Account account) {
		account.addIncome(this);
	}
}