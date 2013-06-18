package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestment401k extends HistoryNew {

	private BigDecimal[] amountHistory;
	private BigDecimal[] salary;
	private BigDecimal[] employeeContribution;
	private BigDecimal[] employerContribution;
	private int listSize = 600; //TODO: take from
	private String name;
	
	public HistoryInvestment401k(Investment401k investment401k) {		
		amountHistory = new BigDecimal[listSize];
		salary = new BigDecimal[listSize];
		employeeContribution = new BigDecimal[listSize];
		employerContribution = new BigDecimal[listSize];
		name = investment401k.getName();
	}

	@Override
	public void add(int index, AccountingElement acctElement, HistoryCashflows cashflows, HistoryNetWorth net_worth) {
		Investment401k investment = (Investment401k) acctElement;
		try {		    
			amountHistory[index] = investment.getAmount();
		    salary[index] = investment.getSalary();
		    employeeContribution[index] = investment.getEmployeeContribution();
		    employerContribution[index] = investment.getEmployerContribution();

		    cashflows.addInvestment401k(index, investment);
		    net_worth.addInvestment401k(index, investment);

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}	

	public String getName() {
		return name;
	}
	
	public BigDecimal[] getAmountHistory() {
		return amountHistory;
	}

	public BigDecimal[] getEmployeeContributionHistory() {
		return employeeContribution;
	}
	
	public BigDecimal[] getEmployerContributionHistory() {
		return employerContribution;
	}
	
	public BigDecimal[] getSalaryHistory() {
		return salary;
	}
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableInvestment401k(this);		
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
		visitor.plotInvestment401k(this);								
	}
}
