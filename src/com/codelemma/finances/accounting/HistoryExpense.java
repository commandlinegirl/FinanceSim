package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class HistoryExpense implements HistoryNew {

	public abstract String getName();
	public abstract BigDecimal[] getAmountHistory();
	public abstract void makeTable(TableVisitor visitor);
	

}
