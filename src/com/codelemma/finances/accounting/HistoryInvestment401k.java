package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestment401k extends HistoryInvestment {

	private BigDecimal[] amountHistory;
	private BigDecimal[] salary;
	private BigDecimal[] employeeContribution;
	private BigDecimal[] employerContribution;
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryInvestment401k(Investment401k investment401k) {		
		amountHistory = new BigDecimal[listSize];
		salary = new BigDecimal[listSize];
		employeeContribution = new BigDecimal[listSize];
		employerContribution = new BigDecimal[listSize];
		name = investment401k.getName();
	}

	@Override
	public void add(int i, NamedValue acctElement) {
		Investment401k investment = (Investment401k) acctElement;
		try {		    
			amountHistory[i] = investment.getAmount();
		    salary[i] = investment.getSalary();
		    employeeContribution[i] = investment.getEmployeeContribution();
		    employerContribution[i] = investment.getEmployerContribution();
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
