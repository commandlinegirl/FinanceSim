package com.codelemma.finances;

import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.PackToContainerVisitor;

public class PackToContainerLauncher implements PackToContainerVisitor {
	
	@Override
	public TypedContainer packIncomeGeneric(IncomeGeneric income) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.YEARLY_INCOME, income.getValue());
        container.put(TypedKey.INCOME_TAX_RATE, income.getInitTaxRate());
        container.put(TypedKey.YEARLY_INCOME_RISE, income.getInitRiseRate());
        container.put(TypedKey.INCOME_INSTALLMENTS, income.getInitInstallments());
        container.put(TypedKey.INCOME_NAME, income.getName()); // TODO: how to store reference to Investment401k?
        container.put(TypedKey.INCOME_START_YEAR, income.getStartYear()); // TODO: how to store reference to Investment401k?
        container.put(TypedKey.INCOME_START_MONTH, income.getStartMonth()); // TODO: how to store reference to Investment401k?
    	return container;
	}

	@Override
	public TypedContainer packInvestment401k(Investment401k investment401k) {	
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENT401K_NAME, investment401k.getName());    	
        container.put(TypedKey.INVESTMENT401K_INIT_AMOUNT,  investment401k.getInitAmount());
        container.put(TypedKey.INVESTMENT401K_PERCONTRIB, investment401k.getPercontrib());
        container.put(TypedKey.INVESTMENT401K_INTEREST_RATE, investment401k.getInterestRate());
        container.put(TypedKey.INVESTMENT401K_WITHDRAWAL_TAX_RATE, investment401k.getWithdrawalTaxRate());
        container.put(TypedKey.INVESTMENT401K_EMPLOYER_MATCH, investment401k.getEmployerMatch()); // TODO: how to store reference to Income?
        container.put(TypedKey.INVESTMENT401K_PERIOD, investment401k.getPeriod());
        container.put(TypedKey.INVESTMENT401K_INCOMEID, investment401k.getIncome().getId());
        container.put(TypedKey.INVESTMENT401K_START_YEAR, investment401k.getStartYear());
        container.put(TypedKey.INVESTMENT401K_START_MONTH, investment401k.getStartMonth());
    	return container;
	}

	@Override
	public TypedContainer packInvestmentSavAcct(InvestmentSavAcct investmentsav) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTSAV_NAME, investmentsav.getName());    	
        container.put(TypedKey.INVESTMENTSAV_INIT_AMOUNT,  investmentsav.getInitAmount());
        container.put(TypedKey.INVESTMENTSAV_TAX_RATE, investmentsav.getTaxRate());
        container.put(TypedKey.INVESTMENTSAV_PERCONTRIB, investmentsav.getPercontrib());
        container.put(TypedKey.INVESTMENTSAV_CAPITALIZATION, investmentsav.getCapitalization());
        container.put(TypedKey.INVESTMENTSAV_INTEREST_RATE, investmentsav.getInterestRate());
        container.put(TypedKey.INVESTMENTSAV_START_YEAR, investmentsav.getStartYear());
        container.put(TypedKey.INVESTMENTSAV_START_MONTH, investmentsav.getStartMonth());
    	return container;
	}
	
	@Override
	public TypedContainer packInvestmentCheckAcct(InvestmentCheckAcct investmentcheck) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTCHECK_NAME, investmentcheck.getName());    	
        container.put(TypedKey.INVESTMENTCHECK_INIT_AMOUNT,  investmentcheck.getInitAmount());
        container.put(TypedKey.INVESTMENTCHECK_TAX_RATE, investmentcheck.getTaxRate());
        container.put(TypedKey.INVESTMENTCHECK_CAPITALIZATION, investmentcheck.getCapitalization());
        container.put(TypedKey.INVESTMENTCHECK_INTEREST_RATE, investmentcheck.getInterestRate());
        container.put(TypedKey.INVESTMENTCHECK_START_YEAR, investmentcheck.getStartYear());
        container.put(TypedKey.INVESTMENTCHECK_START_MONTH, investmentcheck.getStartMonth());
    	return container;
	}

	@Override
	public TypedContainer packDebtLoan(DebtLoan debtloan) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.DEBTLOAN_ID, debtloan.getId());
        container.put(TypedKey.DEBTLOAN_AMOUNT, debtloan.getValue());
        container.put(TypedKey.DEBTLOAN_NAME, debtloan.getName());
        container.put(TypedKey.DEBTLOAN_INTEREST_RATE, debtloan.getInterestRate());               
        container.put(TypedKey.DEBTLOAN_TERM, debtloan.getTerm());               
        container.put(TypedKey.DEBTLOAN_EXTRA_PAYMENT, debtloan.getExtraPayment());               
        container.put(TypedKey.DEBTLOAN_START_YEAR, debtloan.getStartYear());               
        container.put(TypedKey.DEBTLOAN_START_MONTH, debtloan.getStartMonth());               
    	return container;
	}
	
	@Override
	public TypedContainer packExpenseGeneric(ExpenseGeneric expense) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.EXPENSE_ID, expense.getId());
        container.put(TypedKey.EXPENSE_NAME, expense.getName());
        container.put(TypedKey.INIT_EXPENSE, expense.getValue());
        container.put(TypedKey.INFLATION_RATE, expense.getInitInflationRate());
        container.put(TypedKey.EXPENSE_FREQUENCY, expense.getFrequency());   
        container.put(TypedKey.EXPENSE_START_YEAR, expense.getStartYear());                    
        container.put(TypedKey.EXPENSE_START_MONTH, expense.getStartMonth());                    
    	return container;    	
	}
	
	@Override
	public TypedContainer packDebtMortgage(DebtMortgage debtmortgage) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.DEBTMORTGAGE_ID, debtmortgage.getId());
        container.put(TypedKey.DEBTMORTGAGE_PURCHASE_PRICE, debtmortgage.getPurchasePrice());
        container.put(TypedKey.DEBTMORTGAGE_NAME, debtmortgage.getName());
        container.put(TypedKey.DEBTMORTGAGE_DOWNPAYMENT, debtmortgage.getDownpayment());               
        container.put(TypedKey.DEBTMORTGAGE_INTEREST_RATE, debtmortgage.getInterestRate());
        container.put(TypedKey.DEBTMORTGAGE_TERM, debtmortgage.getTerm());               
        container.put(TypedKey.DEBTMORTGAGE_PROPERTY_INSURANCE, debtmortgage.getPropertyInsurance());               
        container.put(TypedKey.DEBTMORTGAGE_PROPERTY_TAX, debtmortgage.getPropertyTax());               
        container.put(TypedKey.DEBTMORTGAGE_PMI, debtmortgage.getPMI());               
        container.put(TypedKey.DEBTMORTGAGE_START_YEAR, debtmortgage.getStartYear());               
        container.put(TypedKey.DEBTMORTGAGE_START_MONTH, debtmortgage.getStartMonth());               
    	return container;
	}
}
