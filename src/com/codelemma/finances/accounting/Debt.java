package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Debt implements AccountingElement {
	
	private int id = 0;
	
	public Debt() {
		id++;
	}
	
	public abstract BigDecimal getInitAmount();
	public abstract void initialize();     
	public abstract void setId(int id); 
	public abstract BigDecimal getMonthlyPayment();         
	public abstract void advance(int year, int month);
	public abstract void setValuesBeforeCalculation();
	
	@Override
	public void addToAccount(Account account) {
		account.justAddDebt(this);
	}
}
