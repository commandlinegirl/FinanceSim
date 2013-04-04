package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryIncomeGeneric extends HistoryNew {

	private BigDecimal[] amountHistory;
	private int listSize = 600; //TODO: take from
    private String name;
    private BigDecimal[] grossIncomeHistory;
    private BigDecimal[] taxHistory;
    private BigDecimal[] netIncomeHistory;
    private BigDecimal[] preTaxInvestmentHistory;
	private boolean incomeHas401k = false;
    
	public HistoryIncomeGeneric(IncomeGeneric income) {		
		amountHistory = new BigDecimal[listSize];
		grossIncomeHistory = new BigDecimal[listSize];
		taxHistory = new BigDecimal[listSize];
		netIncomeHistory = new BigDecimal[listSize];
		preTaxInvestmentHistory = new BigDecimal[listSize]; //eg. 401k
		name = income.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement, HistoryCashflows cashflows) {
		IncomeGeneric inc = (IncomeGeneric) acctElement;

		try {
		    amountHistory[index] = inc.getAmount();
		    grossIncomeHistory[index] = inc.getGrossIncome();
		    taxHistory[index] = inc.getTax();
		    netIncomeHistory[index] = inc.getNetIncome();
		    // TODO: add 401k payment history (if income contains 401k)
		    if (inc.getInvestment401k() != null) {
		    	incomeHas401k = true;
		        preTaxInvestmentHistory[index] = inc.getInvestment401k().getEmployeeContribution();
		    }
		    cashflows.addIncomeGeneric(index, inc);
             
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}	
	
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

	public BigDecimal[] getPreTaxInvestmentHistory() {
		return preTaxInvestmentHistory;
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
	
	@Override
	public void makeTable(TableVisitor visitor) {
		if (incomeHas401k) {
			visitor.makeTableIncomeWithPreTaxInv(this);	
		} else {
			visitor.makeTableIncome(this);
		}
				
	}	
}
