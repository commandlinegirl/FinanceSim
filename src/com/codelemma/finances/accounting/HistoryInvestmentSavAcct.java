package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentSavAcct extends HistoryNew {

	private BigDecimal[] amountHistory;
	private BigDecimal[] interests_net;
	private BigDecimal[] contribution;
	private BigDecimal[] tax;
	private int listSize = 600; //TODO: take from
	private String name;

	
	public HistoryInvestmentSavAcct(InvestmentSavAcct investment_savacct) {		
		amountHistory = new BigDecimal[listSize];
		interests_net = new BigDecimal[listSize];
		contribution = new BigDecimal[listSize];
		tax = new BigDecimal[listSize];
		name = investment_savacct.getName();

	}

	@Override
	public void add(int index, AccountingElement acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		InvestmentSavAcct investment = (InvestmentSavAcct) acctElement;
		try {		    
			amountHistory[index] = investment.getAmount();
			contribution[index] = investment.getContribution();
		    interests_net[index] = investment.getInterestsNet();
		    tax[index] = investment.getTax();
		    
		    cashflows.addInvestmentSavAcct(index, investment);
		    net_worth.addInvestmentSavAcct(index, investment);

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestmentSavAcct(this);		
	}    
	
	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}
	
	public BigDecimal[] getTaxHistory() {
		return tax;
	}
	
	public BigDecimal[] getContributionHistory() {
		return contribution;
	}

	public BigDecimal[] getNetInterestsHistory() {
		return interests_net;
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
		visitor.plotInvestmentSavAcct(this);								
		
	}
}
