package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;

abstract class AccountingElement implements AccountingElementInterface {
	
	private static int next_id = 0;
	private int id = next_id;

	protected AccountingElement() {
		next_id++;		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public abstract BigDecimal getValue();
	public abstract String getName();
	public abstract BigDecimal getAmount();
	//public abstract int getId();
	//public abstract void setId(int id);
	public abstract HistoryNew getHistory();
	
	public abstract void updateInputListing(InputListingUpdater modifier);
	public abstract int getStartYear();
	public abstract int getStartMonth();
	public abstract void setValuesBeforeCalculation();
    public abstract void initialize();     

	public abstract void addToAccount(Account account); // Visitor pattern to provide double-dispatch on account as well as on accounting element.
}