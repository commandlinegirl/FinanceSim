package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentSavAcct implements HistoryNew {

	private BigDecimal[] amount;
	private BigDecimal[] interests_net;
	private BigDecimal[] interests_gross;
	private BigDecimal[] tax;
	private int listSize = 360; //TODO: take from
	
	public HistoryInvestmentSavAcct(InvestmentSavAcct investment_savacct) {		
		amount = new BigDecimal[listSize];
		interests_net = new BigDecimal[listSize];
		interests_gross = new BigDecimal[listSize];
		tax = new BigDecimal[listSize];
	}

	@Override
	public void add(int i, Object acctElement) {
		InvestmentSavAcct investment = (InvestmentSavAcct) acctElement;
		try {		    
		    amount[i] = investment.getAmount();
		    interests_gross[i] = investment.getInterestsGross();
		    interests_net[i] = investment.getInterestsNet();
		    tax[i] = investment.getTax();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void plot(PlotVisitor plotter) {
		plotter.plotInvestment(this);		
	}    
}
