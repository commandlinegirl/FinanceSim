package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryDebtMortgage extends HistoryDebt {

	private BigDecimal[] monthlyPaymentHistory;
	private BigDecimal[] interestsPaidHistory;
	private BigDecimal[] totalInterestsHistory;
	private BigDecimal[] principalPaidHistory;
	private BigDecimal[] additionalCostHistory;	
	private BigDecimal[] remainingAmountHistory;	
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryDebtMortgage(DebtMortgage debt) {		
		monthlyPaymentHistory = new BigDecimal[listSize];
		interestsPaidHistory = new BigDecimal[listSize];
		totalInterestsHistory = new BigDecimal[listSize];
		principalPaidHistory = new BigDecimal[listSize];
		additionalCostHistory = new BigDecimal[listSize];		
		remainingAmountHistory = new BigDecimal[listSize];
		name = debt.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement) {
		// TODO Auto-generated method stub
		DebtMortgage debt = (DebtMortgage) acctElement;
		try {		    
		    monthlyPaymentHistory[index] = debt.getMonthlyPayment();
		    interestsPaidHistory[index] = debt.getInterestPaid();
		    totalInterestsHistory[index] = debt.getTotalInterestPaid();
		    principalPaidHistory[index] = debt.getPrincipalPaid();
		    additionalCostHistory[index] = debt.getAdditionalCost();
		    remainingAmountHistory[index] = debt.getRemainingAmount();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableDebtMortgage(this);		
	}

	public BigDecimal[] getMonthlyPaymentHistory() {
		return monthlyPaymentHistory;
	}

	public BigDecimal[] getInterestsPaidHistory() {
		return interestsPaidHistory;
	}

	public BigDecimal[] getTotalInterestsHistory() {
		return totalInterestsHistory;
	}
	
	public BigDecimal[] getPrincipalPaidHistory() {
		return principalPaidHistory;
	}
	
	public BigDecimal[] getAdditionalCostHistory() {
		return additionalCostHistory;
	}
	
	public BigDecimal[] getRemainingAmountHistory() {
		return remainingAmountHistory;
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
		return remainingAmountHistory.length > 0;	
    }

	@Override
	public BigDecimal[] getAmountHistory() {
		return principalPaidHistory; //TODO: check if this is what you want
	}

	@Override
	public void plot(PlotVisitor visitor) {
		visitor.plotDebtMortgage(this);						
	}
}
