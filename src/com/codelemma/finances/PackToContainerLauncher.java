package com.codelemma.finances;

import com.codelemma.finances.accounting.Debt;
import com.codelemma.finances.accounting.Expense;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.PackToContainerVisitor;

public class PackToContainerLauncher implements PackToContainerVisitor {
	
	@Override
	public TypedContainer packIncome(Income income) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.YEARLY_INCOME, income.getValue());
        container.put(TypedKey.INCOME_TAX_RATE, income.getTaxRate());
        container.put(TypedKey.YEARLY_INCOME_RISE, income.getRiseRate());
        container.put(TypedKey.INCOME_INSTALLMENTS, income.getInstallments());
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
	public TypedContainer packInvestmentStock(InvestmentStock investmentstock) {
    	TypedContainer container = new TypedContainer();
    	container.put(TypedKey.INVESTMENTSTOCK_NAME, investmentstock.getName());    	
        container.put(TypedKey.INVESTMENTSTOCK_INIT_AMOUNT,  investmentstock.getInitAmount());
        container.put(TypedKey.INVESTMENTSTOCK_PERCONTRIB, investmentstock.getPercontrib());
        container.put(TypedKey.INVESTMENTSTOCK_TAX_RATE, investmentstock.getTaxRate());
        container.put(TypedKey.INVESTMENTSTOCK_APPRECIATION, investmentstock.getAppreciation());
        container.put(TypedKey.INVESTMENTSTOCK_DIVIDEND, investmentstock.getDividend());
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
	public TypedContainer packDebt(Debt debt) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.DEBT_ID, debt.getId());
        container.put(TypedKey.DEBT_AMOUNT, debt.getValue());
        container.put(TypedKey.DEBT_NAME, debt.getName());               
    	return container;
	}

	@Override
	public TypedContainer packExpense(Expense expense) {
    	TypedContainer container = new TypedContainer();
        container.put(TypedKey.EXPENSE_ID, expense.getId());
        container.put(TypedKey.EXPENSE_NAME, expense.getName());
        container.put(TypedKey.INIT_EXPENSE, expense.getValue());
        container.put(TypedKey.INFLATION_RATE, expense.getInflationRate());
        container.put(TypedKey.EXPENSE_FREQUENCY, expense.getFrequency());                    
    	return container;    	
	}
}