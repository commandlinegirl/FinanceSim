package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class HistoryInvestment implements HistoryNew {
	

	public abstract String getName();
	public abstract BigDecimal[] getAmountHistory();
	public abstract void plot(PlotVisitor plotter);
	
}
