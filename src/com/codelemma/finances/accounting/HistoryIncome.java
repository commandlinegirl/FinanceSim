package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class HistoryIncome implements HistoryNew {

	public abstract String getName();
	public abstract BigDecimal[] getAmountHistory();
	public abstract void makeTable(TableVisitor visitory);
	

}
