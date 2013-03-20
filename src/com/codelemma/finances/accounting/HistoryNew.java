package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public interface HistoryNew {

	public void add(int index, NamedValue acctElement);
	public void makeTable(TableVisitor visitor);
	public void plot(PlotVisitor visitor);
	public BigDecimal[] getAmountHistory();
	public String getName();
	public String toString();	
	public boolean isNonEmpty();
}
