package com.codelemma.finances.accounting;

public interface PlotVisitor {
	public void plotDebt(HistoryDebt debts);
	public void plotExpense(HistoryExpense expenses);
	public void plotIncome(HistoryIncome incomes);
	public void plotInvestment(HistoryInvestment401k historyInvestment401k);
	public void plotInvestment(HistoryInvestmentSavAcct historyInvestmentSavAcct);
	
}
