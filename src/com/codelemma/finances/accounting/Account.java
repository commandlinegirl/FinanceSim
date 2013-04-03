package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import android.util.SparseArray;

public class Account {
    
    private int income_id = 0;    
    private int expense_id = 0;
    private int investment_id = 0;
    private int debt_id = 0;
    private int simStartYear;
    private int simStartMonth;
    private BigDecimal investmentsPercontrib;
    private BigDecimal checkingAcctPercontrib;
    private BigDecimal prevMonthCapitalGains;
	private InvestmentCheckAcct checkingAcct; 	

    private SparseArray<Income> income_ids = new SparseArray<Income>();
    private SparseArray<Expense> expense_ids = new SparseArray<Expense>();
    private SparseArray<Investment> investment_ids = new SparseArray<Investment>();
    private SparseArray<Debt> debt_ids = new SparseArray<Debt>();
    
    private ArrayList<Income> incomes = new ArrayList<Income>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private ArrayList<Investment> investments = new ArrayList<Investment>();
    private ArrayList<Debt> debts = new ArrayList<Debt>();

    private HistoryCashflows cashflows = new HistoryCashflows("Aggregates");
    private HistoryNetWorth net_worth = new HistoryNetWorth("Aggregates");    
    
    public Account(int year, int month) {
    	simStartYear = year;
    	simStartMonth = month;
    	investmentsPercontrib = Money.ZERO;
    	checkingAcctPercontrib = Money.ZERO;
    	prevMonthCapitalGains = Money.ZERO;
    }
    
    public BigDecimal getInvestmentsPercontrib() {
    	return investmentsPercontrib;
    }
    
    public void initPrevMonthCapitalGains() {
    	prevMonthCapitalGains = Money.ZERO;
    }

    public void addToInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = investmentsPercontrib.add(newPercentage);
    }

    public void subtractFromInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = investmentsPercontrib.subtract(newPercentage);
    }
    
    public void setInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = newPercentage;
    }
    
    public void setCheckingAcctPercontrib() {
    	checkingAcctPercontrib = (new BigDecimal(100)).subtract(investmentsPercontrib);
    }
    
    public BigDecimal getCheckingAcctPercontrib() {
    	return checkingAcctPercontrib;
    }
    
    public void addExpense(Expense expense) {
    	Preconditions.checkNotNull(expense, "Missing expenses");
    	expense.setId(expense_id);        
        expenses.add(expense);
        expense_ids.put(expense_id, expense);
        expense_id++;
    }   

    public void addInvestment(Investment investment) {
    	Preconditions.checkNotNull(investment, "Missing investment");
    	investment.setId(investment_id);        
        investments.add(investment);        
        investment_ids.put(investment_id, investment);        
        investment_id++;
    }   

    public void addIncome(Income income) {
        Preconditions.checkNotNull(income, "Missing income");    	
    	income.setId(income_id);
        incomes.add(income);
        income_ids.put(income_id, income);
        income_id++;        
    }

    public void addDebt(Debt debt) {
    	Preconditions.checkNotNull(debt, "Missing debt");
    	debt.setId(debt_id);        
        debts.add(debt);
        debt_ids.put(debt_id, debt);
        debt_id++;        
    }   
           
    public void removeIncome(Income income) {
    	if (income != null) {
    		incomes.remove(income);
        	income_ids.remove(income.getId());    		
    	}    	    	
    }

    public void removeExpense(Expense expense) {
    	if (expense != null) {
    		expenses.remove(expense);
        	expense_ids.remove(expense.getId());
    	}
    }

    public void removeInvestment(Investment investment) {
    	if (investment != null) {
    		investments.remove(investment);
        	investment_ids.remove(investment.getId());
    	}    	
    }

    public void removeDebt(Debt debt) {
    	if (debt != null) {
    		debts.remove(debt);
        	debt_ids.remove(debt.getId());
    	}
    }
    
    public int getIncomesSize() {
    	return incomes.size();
    }    

    public int getExpensesSize() {
    	return expenses.size();
    }    

    public int getInvestmentsSize() {
    	return investments.size();
    }    

    public int getDebtsSize() {
    	return debts.size();
    }    

    public Iterable<Income> getIncomes() {
    	return Collections.unmodifiableList(incomes);
    }
   
    public Iterable<Expense> getExpenses() {
    	return Collections.unmodifiableList(expenses);
    }

    public Iterable<Investment> getInvestments() {
    	return Collections.unmodifiableList(investments);
    }

    public Iterable<Debt> getDebts() {
    	return Collections.unmodifiableList(debts);
    }   
    
    public Income getIncomeById(int id) {
    	return income_ids.get(id);
    }
    
    public Expense getExpenseById(int id) {
    	return expense_ids.get(id);
    }
    
    public Investment getInvestmentById(int id) {
    	return investment_ids.get(id);
    }
    
    public Debt getDebtById(int id) {
    	return debt_ids.get(id);
    }
        
    public void setInitDebt() {
        for (Debt debt: debts) {
        	debt.initialize();
        }
    }

    public void setInitExpense() {
        for (Expense expense: expenses) {
        	expense.initialize();
        }
    }
    
    public void setInitIncome() {
        for (Income income: incomes) {
            income.initialize();
        }
    }

    public void setInitInvestment() {
        for (Investment investment: investments) {
            investment.initialize();
        }
    }
    
    public void setInitCashflow() {
    	cashflows.initialize();
    }
    
    public void setInitNetworth() {
    	net_worth.initialize();
    }
    
    public BigDecimal advanceDebt(int index, int year, int month) {
    	BigDecimal total_debt = Money.ZERO;
        for (Debt debt: debts) {
    		debt.advance(year, month);   		
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		debt.getHistory().add(index, debt, cashflows, net_worth);
        	}
            total_debt = total_debt.add(debt.getMonthlyPayment());
        }
        return total_debt;
    }  
    
    public BigDecimal advanceExpense(int index, int year, int month) {
    	BigDecimal total_expense = Money.ZERO;
        for (Expense expense: expenses) {
        	expense.advance(year, month);
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		expense.getHistory().add(index, expense, cashflows);
        	}
            total_expense = total_expense.add(expense.getAmount()); 
        }
        return total_expense;
    }
    
    public BigDecimal[] advanceIncome(int index, int year, int month) {
    	
    	BigDecimal total_income = Money.ZERO;
    	BigDecimal total_income_tax = Money.ZERO;
    	
        for (Income income: incomes) {
            income.advance(year, month);
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		income.getHistory().add(index, income, cashflows);
        	}
            total_income = total_income.add(income.getGrossIncome()); // total gross income
            total_income_tax = total_income_tax.add(income.getTax()); // total income tax
        }
        BigDecimal[] income_values = {total_income, total_income_tax};
        return income_values;
    }
    
    public void advanceInvestment(int index, BigDecimal excess, int year, int month, BigDecimal total_income_tax) {    	    	
    	
        for (Investment investment: investments) {          	
        	if (investment.isPreTax()) { // pretax investment (taken from salary before tax, eg. 401(k))
        		investment.advance(year, month, null);
        		excess = excess.subtract(investment.getEmployeeContribution());   // 401k investment is taken from GROSS income
        		// add capital gains
        	        		
    		    if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		    investment.getHistory().add(index, investment, cashflows, net_worth);
        	    }
        	}
        }          
        
		// subtract tax from income part of excess
        excess = excess.subtract(total_income_tax);

        BigDecimal capitalGains = Money.ZERO;
        for (Investment investment: investments) {
        	if (!investment.isPreTax()) { // posttax investment (taken from excess money)        	
        		                		
        		investment.advance(year, month, excess, checkingAcctPercontrib);
        		// here add capital gains to excess
        		capitalGains = capitalGains.add(investment.getInterestsNet());
        		       		                       
    		    if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		    investment.getHistory().add(index, investment, cashflows, net_worth);
        	    }
        	}
        }
        prevMonthCapitalGains = capitalGains;
    }
        
    public void advance(int index, int year, int month) {
    	// year and month - starting date of predictions (saved to history here)
        Preconditions.checkInBounds(month, 0, 11, "Month must be in 0..11");
        BigDecimal excess = prevMonthCapitalGains;
        
        BigDecimal[] income_values = advanceIncome(index, year, month);
        BigDecimal total_income = income_values[0];
        BigDecimal total_income_tax = income_values[1];
        excess = excess.add(total_income); // total GROSS income
        
        BigDecimal total_debt = advanceDebt(index, year, month);
        excess = excess.subtract(total_debt);

        BigDecimal total_expense = advanceExpense(index, year, month);        
        excess = excess.subtract(total_expense);
                
        advanceInvestment(index, excess, year, month, total_income_tax);
    }
    
    public void addToHistory(History history) {
    	for (Investment investment: investments) {
    		history.addInvestmentHistory(investment.getHistory());
    	}
    	
    	for (Income income: incomes) {
    		history.addIncomeHistory(income.getHistory());
    	}
    	
    	for (Expense expense: expenses) {
    		history.addExpenseHistory(expense.getHistory());
    	}
    	
    	for (Debt debt: debts) {
    		history.addDebtHistory(debt.getHistory());
    	}
    	
    	history.addCashflowHistory(cashflows);
    	history.addNetWorthHistory(net_worth);
    }
    
	public void clearHistory(History history) {
		history.clear();
	}
	
	public void setCheckingAcct(InvestmentCheckAcct investment) {
		checkingAcct = investment;
	}
	
	public InvestmentCheckAcct getCheckingAcct() {
		return checkingAcct;
	}
}
