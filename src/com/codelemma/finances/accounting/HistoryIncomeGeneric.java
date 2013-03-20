package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryIncomeGeneric extends HistoryIncome {

	private BigDecimal[] amountHistory;
	private int listSize = 360; //TODO: take from
    private String name;
    private BigDecimal[] grossIncomeHistory;
    private BigDecimal[] taxHistory;
    private BigDecimal[] netIncomeHistory;
	
	public HistoryIncomeGeneric(IncomeGeneric income) {		
		amountHistory = new BigDecimal[listSize];
		grossIncomeHistory = new BigDecimal[listSize];
		taxHistory = new BigDecimal[listSize];
		netIncomeHistory = new BigDecimal[listSize];
		name = income.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement) {
		IncomeGeneric inc = (IncomeGeneric) acctElement;
		try {		    
		    amountHistory[index] = inc.getAmount();
		    grossIncomeHistory[index] = inc.getGrossIncome();
		    taxHistory[index] = inc.getTax();
		    netIncomeHistory[index] = inc.getNetIncome();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableIncome(this);		
	}
	
	@Override
	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}
	
	public BigDecimal[] getGrossIncomeHistory() {
		return grossIncomeHistory;
	}
	
	public BigDecimal[] getTaxHistory() {
		return taxHistory;
	}
	
	public BigDecimal[] getNetIncomeHistory() {
		return netIncomeHistory;
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
		visitor.plotIncomeGeneric(this);								
	}
	
}
