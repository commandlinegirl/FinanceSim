package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentCheckAcct extends HistoryNew {

	private BigDecimal[] amountHistory;
	private BigDecimal[] interests_net;
	private BigDecimal[] contribution;
	private BigDecimal[] tax;
	private int listSize = 600; //TODO: take from
	private String name;
	
	public HistoryInvestmentCheckAcct(InvestmentCheckAcct investment_savacct) {		
		amountHistory = new BigDecimal[listSize];
		interests_net = new BigDecimal[listSize];
		contribution = new BigDecimal[listSize];
		tax = new BigDecimal[listSize];
		name = investment_savacct.getName();

	}
	
	@Override
	public void add(int index, NamedValue acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		InvestmentCheckAcct investment = (InvestmentCheckAcct) acctElement;
		try {		    
			amountHistory[index] = investment.getAmount();
			contribution[index] = investment.getContribution();
		    interests_net[index] = investment.getInterestsNet();
		    tax[index] = investment.getTax();
		    
		    cashflows.addInvestmentCheckAcct(index, investment);
		    net_worth.addInvestmentCheckAcct(index, investment);

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestmentCheckAcct(this);		
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
		visitor.plotInvestmentCheckAcct(this);								
		
	}
}
