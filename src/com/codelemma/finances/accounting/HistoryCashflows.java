package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryCashflows extends HistoryNew {

	private BigDecimal[] netIncomeHistory;
	private BigDecimal[] capitalGainsHistory;
	private BigDecimal[] expensesHistory;
	private BigDecimal[] debtRatesHistory;
	private BigDecimal[] investmentRatesHistory;	
	
	private int listSize = 600; //TODO: take from
	private String name;
	
	public HistoryCashflows(String name) {		
		netIncomeHistory = new BigDecimal[listSize];
		capitalGainsHistory = new BigDecimal[listSize];
		expensesHistory = new BigDecimal[listSize];
		debtRatesHistory = new BigDecimal[listSize];
		investmentRatesHistory = new BigDecimal[listSize];
		
        initialize();
				
        
		this.name = name;
	}	
		
	public void addIncomeGeneric(int index, IncomeGeneric income) {	
		netIncomeHistory[index] = netIncomeHistory[index].add(income.getNetIncome()); 
	}

	public void addExpenseGeneric(int index, ExpenseGeneric expense) {
		expensesHistory[index] = expensesHistory[index].add(expense.getAmount()); 
	}
	
	public void addInvestment401k(int index, Investment401k investment) {
		//investmentRatesHistory[index] = investmentRatesHistory[index].add(investment.getEmployeeContribution()); 
		//capitalGainsHistory[index].add(investment.getInterestsNet(interests_gross, interests_gross_tax));
	}

	public void addInvestmentSavAcct(int index, InvestmentSavAcct investment) {
		investmentRatesHistory[index] = investmentRatesHistory[index].add(investment.getContribution()); 
		capitalGainsHistory[index] = capitalGainsHistory[index].add(investment.getInterestsNet());
	}
	
	public void addInvestmentCheckAcct(int index, InvestmentCheckAcct investment) {
		investmentRatesHistory[index] = investmentRatesHistory[index].add(investment.getContribution()); 
		capitalGainsHistory[index] = capitalGainsHistory[index].add(investment.getInterestsNet());		
	}
	
	public void addDebtLoan(int index, DebtLoan debt) {
		debtRatesHistory[index] = debtRatesHistory[index].add(debt.getMonthlyPayment()); 
	}

	public void addDebtMortgage(int index, DebtMortgage debt) {
		debtRatesHistory[index] = debtRatesHistory[index].add(debt.getMonthlyPayment()); 
	}
	
	public BigDecimal[] getNetIncomeHistory() {
		return netIncomeHistory;
	}
	
	public BigDecimal[] getCapitalGainsHistory() {
		return capitalGainsHistory;
	}
	
	public BigDecimal[] getExpensesHistory() {
		return expensesHistory;
	}
	
	public BigDecimal[] getDebtRatesHistory() {
		return debtRatesHistory;
	}		
	
	public BigDecimal[] getInvestmentRatesHistory() {
		return investmentRatesHistory;
	}
	
	public void initialize() {
		for (int i = 0; i < listSize; i++) {
			netIncomeHistory[i] = Money.ZERO;
			capitalGainsHistory[i] = Money.ZERO;
			expensesHistory[i] = Money.ZERO;
			debtRatesHistory[i] = Money.ZERO;
			investmentRatesHistory[i] = Money.ZERO;			
		}
	}
	
	@Override
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableCashflowsAggregates(this);				
		
	}

	@Override
	public void plot(PlotVisitor visitor) {
		visitor.plotCashflows(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isNonEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

}
