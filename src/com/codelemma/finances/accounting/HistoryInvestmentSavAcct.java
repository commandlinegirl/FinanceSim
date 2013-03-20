package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentSavAcct extends HistoryInvestment {

	private BigDecimal[] amountHistory;
	private BigDecimal[] interests_net;
	private BigDecimal[] contribution;
	private BigDecimal[] tax;
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryInvestmentSavAcct(InvestmentSavAcct investment_savacct) {		
		amountHistory = new BigDecimal[listSize];
		interests_net = new BigDecimal[listSize];
		contribution = new BigDecimal[listSize];
		tax = new BigDecimal[listSize];
		name = investment_savacct.getName();
	}

	@Override
	public void add(int i, NamedValue acctElement) {
		InvestmentSavAcct investment = (InvestmentSavAcct) acctElement;
		try {		    
			amountHistory[i] = investment.getAmount();
			contribution[i] = investment.getContribution();
		    interests_net[i] = investment.getInterestsNet();
		    tax[i] = investment.getTax();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestmentSavAcct(this);		
	}    
	
	@Override
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
