package com.codelemma.finances.accounting;
import com.codelemma.finances.TypedContainer;

public interface PackToContainerVisitor {
    public TypedContainer packIncome(Income income);   
    public TypedContainer packInvestmentSavAcct(InvestmentSavAcct investmentsav);
    public TypedContainer packInvestment401k(Investment401k investment401k);
    public TypedContainer packInvestmentBond(InvestmentBond investmentbond);
    public TypedContainer packInvestmentStock(InvestmentStock investmentstock);    
    public TypedContainer packDebt(Debt debt);
    public TypedContainer packExpense(Expense expense);
}
