package com.codelemma.finances.accounting;

import java.math.BigDecimal;


public class HistoryExpenseGeneric extends HistoryExpense {

	private BigDecimal[] amountHistory;
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryExpenseGeneric(ExpenseGeneric expense) {		
		amountHistory = new BigDecimal[listSize];	
		name = expense.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement) {
		// TODO Auto-generated method stub
		ExpenseGeneric expense = (ExpenseGeneric) acctElement;
		try {		    
		    amountHistory[index] = expense.getAmount();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableExpense(this);		
	}

	@Override
	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}

	public String getName() {
		return name;
	}
	
	@Override
    public String toString() {
		return name;
	}
	
	@Override
	public boolean isNonEmpty() {
		return amountHistory.length > 0;
	}

	@Override
	public void plot(PlotVisitor visitor) {
		visitor.plotExpenseGeneric(this);						
	}

}