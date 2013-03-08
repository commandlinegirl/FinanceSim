package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestment401k implements HistoryNew {

	private BigDecimal[] amount;
	private BigDecimal[] salary;
	private BigDecimal[] employee_contribution;
	private BigDecimal[] employer_contribution;
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryInvestment401k(Investment401k investment401k) {		
		amount = new BigDecimal[listSize];
		salary = new BigDecimal[listSize];
		employee_contribution = new BigDecimal[listSize];
		employer_contribution = new BigDecimal[listSize];
		name = investment401k.getName();
	}

	@Override
	public void add(int i, Object acctElement) {
		Investment401k investment = (Investment401k) acctElement;
		try {		    
		    amount[i] = investment.getAmount();
		    salary[i] = investment.getSalary();
		    employee_contribution[i] = investment.getEmployeeContribution();
		    employer_contribution[i] = investment.getEmployerContribution();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}	
	
	public String getName() {
		return name;
	}
	
	public BigDecimal[] getAmountHistory() {
		return amount;
	}
		
	public void plot(PlotVisitor plotter) {
		plotter.plotInvestment(this);		
	}    
}
