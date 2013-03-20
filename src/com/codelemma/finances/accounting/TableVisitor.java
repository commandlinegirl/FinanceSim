package com.codelemma.finances.accounting;

public interface TableVisitor {
	
	public void makeTableIncome(HistoryIncomeGeneric incomes);
	
	public void makeTableExpense(HistoryExpenseGeneric expenses);		
	
	public void makeTableInvestment401k(HistoryInvestment401k historyInvestment401k);
	public void makeTableInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct);
	public void makeTableInvestmentBond(HistoryInvestmentBond historyInvestmentBond);
	public void makeTableInvestmentStock(HistoryInvestmentStock historyInvestmentStock);
	
	public void makeTableDebtMortgage(HistoryDebtMortgage historyDebtMortgage);
	public void makeTableDebtLoan(HistoryDebtLoan historyDebtConsumerLoan);
}
