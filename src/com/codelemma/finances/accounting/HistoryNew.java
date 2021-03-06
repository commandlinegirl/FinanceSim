package com.codelemma.finances.accounting;


public abstract class HistoryNew {

	public void add(int index, AccountingElement acctElement) {};
	public void add(int index, AccountingElement acctElement, HistoryCashflows cashflows) {};	
	public void add(int index, AccountingElement acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {};	
	public abstract void makeTable(TableVisitor visitor);
	public abstract void plot(PlotVisitor visitor);
	public abstract String getName();
	public abstract String toString();	
	public abstract boolean isNonEmpty();	
}
