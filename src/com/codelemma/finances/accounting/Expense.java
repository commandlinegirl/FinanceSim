package com.codelemma.finances.accounting;

public abstract class Expense extends AccountingElement {
	
    public void advance(int year, int month) {}
	
	@Override
	public void addToAccount(Account account) {
		account.addExpense(this);
	}
}