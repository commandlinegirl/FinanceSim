package com.codelemma.finances.accounting;

public interface ModifyUiVisitor {
	public void launchModifyUiForDebt(Debt debt);
	public void launchModifyUiForExpense(Expense expense);
    public void launchModifyUiForIncome(Income income);
    public void launchModifyUiForInvestment(InvestmentSavAcct investment);
    public void launchModifyUiForInvestment(Investment401k investment);
    public void launchModifyUiForInvestment(InvestmentBond investment);
    public void launchModifyUiForInvestment(InvestmentStock investment);
}