package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentBond extends HistoryNew {

	private BigDecimal[] amountHistory;
	private int listSize = 600; //TODO: take from
	private String name;

	public HistoryInvestmentBond(InvestmentBond investmentbond) {		
		amountHistory = new BigDecimal[listSize];	
		name = investmentbond.getName();

	}
	
	@Override
	public void add(int index, AccountingElement acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		// TODO Auto-generated method stub
		InvestmentBond investment = (InvestmentBond) acctElement;
		try {		    
			amountHistory[index] = investment.getAmount();
			
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestmentBond(this);		
	}

	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
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
		visitor.plotInvestmentBond(this);								
		
	}
}
