package com.codelemma.finances.accounting;

public interface TableVisitor {
	
	public void makeTableIncome(HistoryIncomeGeneric income);
	public void makeTableIncomeWithPreTaxInv(HistoryIncomeGeneric income);
	
	public void makeTableExpense(HistoryExpenseGeneric expenses);		
	
	public void makeTableInvestment401k(HistoryInvestment401k historyInvestment401k);
	public void makeTableInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct);
	public void makeTableInvestmentCheckAcct(HistoryInvestmentCheckAcct historyInvestmentCheckAcct);
	public void makeTableInvestmentBond(HistoryInvestmentBond historyInvestmentBond);
	public void makeTableInvestmentStock(HistoryInvestmentStock historyInvestmentStock);

	public void makeTableDebtMortgage(HistoryDebtMortgage historyDebtMortgage);
	public void makeTableDebtLoan(HistoryDebtLoan historyDebtConsumerLoan);
	
	public void makeTableCashflowsAggregates(HistoryCashflows historyCashflows);
	public void makeTableNetWorthAggregates(HistoryNetWorth historyNetWorth);	
}
