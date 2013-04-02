package com.codelemma.finances;

import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.PackToContainerVisitor;

public class PackToContainerLauncher implements PackToContainerVisitor {
	
	@Override
	public TypedContainer packIncomeGeneric(IncomeGeneric income) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.YEARLY_INCOME, income.getValue());
        container.put(TypedKey.INCOME_TAX_RATE, income.getInitTaxRate());
        container.put(TypedKey.YEARLY_INCOME_RISE, income.getInitRiseRate());
        container.put(TypedKey.INCOME_INSTALLMENTS, income.getInitInstallments());
        container.put(TypedKey.INCOME_NAME, income.getName());
    	return container;
	}
	
	@Override
	public TypedContainer packInvestment401k(Investment401k investment401k) {	
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENT401K_NAME, investment401k.getName());    	
        container.put(TypedKey.INVESTMENT401K_INIT_AMOUNT,  investment401k.getInitAmount());
        container.put(TypedKey.INVESTMENT401K_PERCONTRIB, investment401k.getPercontrib());
        container.put(TypedKey.INVESTMENT401K_INTEREST_RATE, investment401k.getInterestRate());
        container.put(TypedKey.INVESTMENT401K_SALARY, investment401k.getInitSalary());
        container.put(TypedKey.INVESTMENT401K_PAYRISE, investment401k.getPayrise());
        container.put(TypedKey.INVESTMENT401K_WITHDRAWAL_TAX_RATE, investment401k.getWithdrawalTaxRate());
        container.put(TypedKey.INVESTMENT401K_EMPLOYER_MATCH, investment401k.getEmployerMatch());        
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
    	return container;
	}
	
	@Override
	public TypedContainer packInvestmentCheckAcct(InvestmentCheckAcct investmentcheck) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTCHECK_NAME, investmentcheck.getName());    	
        container.put(TypedKey.INVESTMENTCHECK_INIT_AMOUNT,  investmentcheck.getInitAmount());
        container.put(TypedKey.INVESTMENTCHECK_TAX_RATE, investmentcheck.getTaxRate());
        container.put(TypedKey.INVESTMENTCHECK_PERCONTRIB, investmentcheck.getPercontrib());
        container.put(TypedKey.INVESTMENTCHECK_CAPITALIZATION, investmentcheck.getCapitalization());
        container.put(TypedKey.INVESTMENTCHECK_INTEREST_RATE, investmentcheck.getInterestRate());
    	return container;
	}
	
	@Override
	public TypedContainer packInvestmentStock(InvestmentStock investmentstock) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTSTOCK_NAME, investmentstock.getName());    	
        container.put(TypedKey.INVESTMENTSTOCK_INIT_AMOUNT,  investmentstock.getInitAmount());
        container.put(TypedKey.INVESTMENTSTOCK_PERCONTRIB, investmentstock.getPercontrib());
        container.put(TypedKey.INVESTMENTSTOCK_TAX_RATE, investmentstock.getTaxRate());
        container.put(TypedKey.INVESTMENTSTOCK_APPRECIATION, investmentstock.getAppreciation());
        container.put(TypedKey.INVESTMENTSTOCK_DIVIDENDS, investmentstock.getDividends());
    	return container;
	}
	
	@Override
	public TypedContainer packInvestmentBond(InvestmentBond investmentbond) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTBOND_NAME, investmentbond.getName());    	
        container.put(TypedKey.INVESTMENTBOND_INIT_AMOUNT,  investmentbond.getInitAmount());
        container.put(TypedKey.INVESTMENTBOND_PERCONTRIB, investmentbond.getPercontrib());
        container.put(TypedKey.INVESTMENTBOND_TAX_RATE, investmentbond.getTaxRate());
    	return container;
	}

	@Override
	public TypedContainer packDebtLoan(DebtLoan debtloan) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.DEBTLOAN_ID, debtloan.getId());
        container.put(TypedKey.DEBTLOAN_AMOUNT, debtloan.getValue());
        container.put(TypedKey.DEBTLOAN_NAME, debtloan.getName());               
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
    	return container;    	
	}

	@Override
	public TypedContainer packDebtMortgage(DebtMortgage debtmortgage) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.DEBTMORTGAGE_ID, debtmortgage.getId());
        container.put(TypedKey.DEBTMORTGAGE_PURCHASE_PRICE, debtmortgage.getPurchasePrice());
        container.put(TypedKey.DEBTMORTGAGE_NAME, debtmortgage.getName());               
    	return container;
	}
}
