package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryDebtLoan extends HistoryNew {
	
	private BigDecimal[] monthlyPaymentHistory;
	private BigDecimal[] interestsPaidHistory;
	private BigDecimal[] totalInterestsHistory;
	private BigDecimal[] principalPaidHistory;
	private BigDecimal[] remainingAmountHistory;	
	
	
	private int listSize = 600; //TODO: take from
	private String name;
	
	public HistoryDebtLoan(DebtLoan debtLoan) {		
		monthlyPaymentHistory = new BigDecimal[listSize];
		interestsPaidHistory = new BigDecimal[listSize];
		totalInterestsHistory = new BigDecimal[listSize];
		principalPaidHistory = new BigDecimal[listSize];
		remainingAmountHistory = new BigDecimal[listSize];
		name = debtLoan.getName();
	}
	
	@Override
	public void add(int index, NamedValue acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		// TODO Auto-generated method stub
		DebtLoan debt = (DebtLoan) acctElement;
		try {		    			
		    monthlyPaymentHistory[index] = debt.getMonthlyPayment();
		    interestsPaidHistory[index] = debt.getInterestPaid();
		    totalInterestsHistory[index] = debt.getTotalInterestPaid();
		    principalPaidHistory[index] = debt.getPrincipalPaid();
		    remainingAmountHistory[index] = debt.getRemainingAmount();		
		    
		    cashflows.addDebtLoan(index, debt);
		    net_worth.addDebtLoan(index, debt);
		    index++;
		    
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableDebtLoan(this);		
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

	public BigDecimal[] getAmountHistory() {
		return principalPaidHistory; //TODO: check if this is what you want
	}

	@Override
	public void plot(PlotVisitor visitor) {
		visitor.plotDebtLoan(this);				
	}

}
