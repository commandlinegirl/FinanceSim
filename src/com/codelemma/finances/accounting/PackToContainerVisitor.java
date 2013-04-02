package com.codelemma.finances.accounting;
import com.codelemma.finances.TypedContainer;

public interface PackToContainerVisitor {
    public TypedContainer packIncomeGeneric(IncomeGeneric income);   
    public TypedContainer packInvestmentSavAcct(InvestmentSavAcct investmentsav);
    public TypedContainer packInvestmentCheckAcct(InvestmentCheckAcct investmentcheck);
    public TypedContainer packInvestment401k(Investment401k investment401k);
    public TypedContainer packInvestmentBond(InvestmentBond investmentbond);
    public TypedContainer packInvestmentStock(InvestmentStock investmentstock);    
    public TypedContainer packExpenseGeneric(ExpenseGeneric expense);
    public TypedContainer packDebtLoan(DebtLoan debtloan);
    public TypedContainer packDebtMortgage(DebtMortgage debtmortgage);
}
