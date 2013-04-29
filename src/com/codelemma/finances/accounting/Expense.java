package com.codelemma.finances.accounting;

public abstract class Expense implements AccountingElement {
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int year, int month) {}
	public abstract void setValuesBeforeCalculation();
	
	@Override
	public void addToAccount(Account account) {
		account.justAddExpense(this);
	}
}