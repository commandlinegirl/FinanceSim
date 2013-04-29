package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Debt implements AccountingElement {
		
	private static int next_id = 0;
	private final int id = next_id;
	
	public Debt() {
		next_id++;		
	}
	
	public abstract BigDecimal getInitAmount();
	public abstract void initialize();     
	public abstract BigDecimal getMonthlyPayment();         
	public abstract void advance(int year, int month);
	public abstract void setValuesBeforeCalculation();
	
	@Override
	public void addToAccount(Account account) {
		account.justAddDebt(this);
	}
	
	@Override 
	public int getId() {
		return id;
	}	
}
