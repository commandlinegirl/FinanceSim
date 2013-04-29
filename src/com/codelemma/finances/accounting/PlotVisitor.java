package com.codelemma.finances.accounting;

public interface PlotVisitor {

	public void plotIncomeGeneric(HistoryIncomeGeneric incomes);	
	public void plotExpenseGeneric(HistoryExpenseGeneric expenses);			
	public void plotInvestment401k(HistoryInvestment401k historyInvestment401k);
	public void plotInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct);
	public void plotInvestmentCheckAcct(HistoryInvestmentCheckAcct historyInvestmentCheckAcct);
	public void plotDebtMortgage(HistoryDebtMortgage historyDebtMortgage);
	public void plotDebtLoan(HistoryDebtLoan historyDebtConsumerLoan);
	public void plotCashflows(HistoryCashflows historyCashflows);
	public void plotNetWorth(HistoryNetWorth historyNetWorth);
	

}
