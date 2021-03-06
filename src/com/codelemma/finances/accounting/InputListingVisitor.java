package com.codelemma.finances.accounting;

public interface InputListingVisitor {
	public void updateInputListingForExpenseGeneric(ExpenseGeneric expense);
    public void updateInputListingForIncomeGeneric(IncomeGeneric income);
    public void updateInputListingForInvestmentSavAcct(InvestmentSavAcct investment);
    public void updateInputListingForInvestmentCheckAcct(InvestmentCheckAcct investment);    
    public void updateInputListingForInvestment401k(Investment401k investment);
	public void updateInputListingForDebtLoan(DebtLoan debt);
	public void updateInputListingForDebtMortgage(DebtMortgage debt);
}
	