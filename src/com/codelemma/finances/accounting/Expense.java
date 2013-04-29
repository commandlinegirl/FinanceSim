package com.codelemma.finances.accounting;

public abstract class Expense implements AccountingElement {
	
	private static int next_id = 0;
	private final int id = next_id;
	
	public Expense() {
		next_id++;		
	}
	
    public abstract  void initialize();     
    public void advance(int year, int month) {}
	public abstract void setValuesBeforeCalculation();
	
	@Override
	public void addToAccount(Account account) {
		account.justAddExpense(this);
	}
	
	@Override 
	public int getId() {
		return id;
	}	
}