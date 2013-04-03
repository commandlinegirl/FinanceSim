package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import android.util.Log;

public class HistoryNetWorth extends HistoryNew {

	private BigDecimal[] savingsHistory;
	private BigDecimal[] outstandingDebtsHistory;
	private BigDecimal[] netWorthHistory;
	
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryNetWorth(String name) {		
		savingsHistory = new BigDecimal[listSize];
		outstandingDebtsHistory = new BigDecimal[listSize];
		netWorthHistory = new BigDecimal[listSize];

        initialize();
				
		this.name = name;
	}	
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableNetWorthAggregates(this);				

	}

	@Override
	public void plot(PlotVisitor visitor) {
        visitor.plotNetWorth(this);
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
		// TODO Auto-generated method stub
		return false;
	}

	public void addInvestment401k(int index, Investment401k investment) {
		savingsHistory[index] = savingsHistory[index].add(investment.getAccumulatedSavings());
		netWorthHistory[index] = netWorthHistory[index].add(investment.getAccumulatedSavings()); 
	}

	public void addInvestmentSavAcct(int index, InvestmentSavAcct investment) {
		savingsHistory[index] = savingsHistory[index].add(investment.getAccumulatedSavings());
		netWorthHistory[index] = netWorthHistory[index].add(investment.getAccumulatedSavings());
	}

	public void addInvestmentCheckAcct(int index, InvestmentCheckAcct investment) {
		savingsHistory[index] = savingsHistory[index].add(investment.getAccumulatedSavings());
		netWorthHistory[index] = netWorthHistory[index].add(investment.getAccumulatedSavings());
	}
	
	public void addDebtLoan(int index, DebtLoan debt) {
		outstandingDebtsHistory[index] = outstandingDebtsHistory[index].add(debt.getRemainingAmount());
		Log.d("before: netWorthHistory["+index+"]", netWorthHistory[index].toString());
		netWorthHistory[index] = netWorthHistory[index].subtract(debt.getRemainingAmount());
		Log.d("after: netWorthHistory["+index+"]", netWorthHistory[index].toString());
	}

	public void addDebtMortgage(int index, DebtMortgage debt) {
		outstandingDebtsHistory[index] = outstandingDebtsHistory[index].add(debt.getRemainingAmount());
		netWorthHistory[index] = netWorthHistory[index].subtract(debt.getRemainingAmount());
	}
	
	public BigDecimal[] getSavingsHistory() {
		return savingsHistory;
	}
	
	public BigDecimal[] getOutstandingDebtsHistory() {
		return outstandingDebtsHistory;
	}		

	public BigDecimal[] getNetWorthHistory() {
		return netWorthHistory;
	}	
	
	public void initialize() {
		for (int i = 0; i < listSize; i++) {
			savingsHistory[i] = Money.ZERO;
			outstandingDebtsHistory[i] = Money.ZERO;
			netWorthHistory[i] = Money.ZERO;		
		}
	}
}
