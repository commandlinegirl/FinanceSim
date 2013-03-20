package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class HistoryDebt implements HistoryNew {

	public abstract String getName();
	public abstract BigDecimal[] getAmountHistory();
	public abstract void makeTable(TableVisitor visitor);
}
