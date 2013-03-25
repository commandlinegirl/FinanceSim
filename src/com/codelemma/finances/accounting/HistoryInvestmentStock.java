package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentStock extends HistoryNew {

	private BigDecimal[] amountHistory;
	private BigDecimal[] capitalGainYieldHistory;
	private BigDecimal[] dividendsHistory;
	private BigDecimal[] taxHistory;
	private int listSize = 360; //TODO: take from
	private String name;

	public HistoryInvestmentStock(InvestmentStock investmentstock) {		
		amountHistory = new BigDecimal[listSize];
		taxHistory = new BigDecimal[listSize];
		dividendsHistory = new BigDecimal[listSize];
		capitalGainYieldHistory = new BigDecimal[listSize];
		name = investmentstock.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		InvestmentStock investment = (InvestmentStock) acctElement;
		try {		    
			amountHistory[index] = investment.getAmount();
			taxHistory[index] = investment.getTax();
			dividendsHistory[index] = investment.getDividends();
			capitalGainYieldHistory[index] = investment.getCapitalGainYield();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}


	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}

	public BigDecimal[] getTaxHistory() {
		return taxHistory;
	}
	
	public BigDecimal[] getDividendsHistory() {
		return dividendsHistory;
	}
	
	public BigDecimal[] getCapitalGainYieldHistory() {
		return capitalGainYieldHistory;
	}
	
	@Override
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
		visitor.plotInvestmentStock(this);										
	}

	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestmentStock(this);		
	}
}
