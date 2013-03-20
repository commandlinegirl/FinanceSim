package com.codelemma.finances.accounting;

public interface ModifyUiVisitor {
	public void launchModifyUiForExpense(ExpenseGeneric expense);
    public void launchModifyUiForIncome(IncomeGeneric income);
    public void launchModifyUiForInvestmentSavAcct(InvestmentSavAcct investment);
    public void launchModifyUiForInvestment401k(Investment401k investment);
    public void launchModifyUiForInvestmentBond(InvestmentBond investment);
    public void launchModifyUiForInvestmentStock(InvestmentStock investment);
	public void launchModifyUiForDebtLoan(DebtLoan debt);
	public void launchModifyUiForDebtMortgage(DebtMortgage debt);
}