package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.util.Log;

public class Account {
    
    private int income_id = 0;    
    private int expense_id = 0;
    private int investment_id = 0;
    private int debt_id = 0;
    private int simStartYear;
    private int simStartMonth;
    
    private HashMap<Integer,Income> income_ids = new HashMap<Integer,Income>();
    private HashMap<Integer,Expense> expense_ids = new HashMap<Integer,Expense>();
    private HashMap<Integer,Investment> investment_ids = new HashMap<Integer,Investment>();
    private HashMap<Integer,Debt> debt_ids = new HashMap<Integer,Debt>();
                
    private ArrayList<Income> incomes = new ArrayList<Income>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private ArrayList<Investment> investments = new ArrayList<Investment>();
    private ArrayList<Debt> debts = new ArrayList<Debt>();

    private HistoryCashflows cashflows = new HistoryCashflows("Aggregates");
    private HistoryNetWorth net_worth = new HistoryNetWorth("Aggregates");

    public Account(int year, int month) {
    	simStartYear = year;
    	simStartMonth = month;
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
    	BigDecimal total_debt = Money.scale(new BigDecimal(0));
        for (Debt debt: debts) {
        	
        	int debtCalcStartYear = debt.getStartYear();
        	int debtCalcStartMonth = debt.getStartMonth();

        	if ((year > debtCalcStartYear) || (year == debtCalcStartYear && month >= debtCalcStartMonth)) {
    		    debt.advance(month);
        	}
    		
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		debt.getHistory().add(index, debt, cashflows, net_worth);
        	}
            total_debt = total_debt.add(debt.getAmount());
        }
        return total_debt;
    }  
    
    public BigDecimal advanceExpense(int index, int year, int month) {
    	BigDecimal total_expense = Money.scale(new BigDecimal(0));
        for (Expense expense: expenses) {
        	int debtCalcStartYear = expense.getStartYear();
        	int debtCalcStartMonth = expense.getStartMonth();

        	if ((year > debtCalcStartYear) || (year == debtCalcStartYear && month >= debtCalcStartMonth)) {
        		expense.advance(month);
        	}        	
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		expense.getHistory().add(index, expense, cashflows);
        	}
            total_expense = total_expense.add(expense.getAmount()); 
        }
        return total_expense;
    }
    
    public BigDecimal advanceIncome(int index, int year, int month) {
    	BigDecimal total_income = Money.scale(new BigDecimal(0));
        for (Income income: incomes) {
        	int debtCalcStartYear = income.getStartYear();
        	int debtCalcStartMonth = income.getStartMonth();

        	if ((year > debtCalcStartYear) || (year == debtCalcStartYear && month >= debtCalcStartMonth)) {
        		income.advance(month);
        	} 
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		income.getHistory().add(index, income, cashflows);
        	}
            total_income = total_income.add(income.getAmount());
        }
        return total_income;
    }
    
    public void advanceInvestment(int index, BigDecimal excess, int year, int month) {
        for (Investment investment: investments) {
        	int investmentCalcStartYear = investment.getStartYear();
        	int investmentCalcStartMonth = investment.getStartMonth();
        	
        	if ((year > investmentCalcStartYear) || (year == investmentCalcStartYear && month >= investmentCalcStartMonth)) {
            	investment.advance(year, month, excess);

        	}        	        	
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		investment.getHistory().add(index, investment, cashflows, net_worth);
        		Log.d("investment", "ADDED TO HISTORY");
        	}
        }
    }
        
    public void advance(int index, int year, int month) {
    	// year and month - starting date of predictions (saved to history here)
        Preconditions.checkInBounds(month, 0, 11, "Month must be in 0..11");
        BigDecimal excess = Money.scale(new BigDecimal(0));
                 
        Log.d("MAIN INDEX ..........", String.valueOf(index));
        
        //Log.d("Account index", String.valueOf(index));
        //Log.d("Account year", String.valueOf(year));
        //Log.d("Account month", String.valueOf(month));
        
        BigDecimal total_income = advanceIncome(index, year, month);
        excess = excess.add(total_income);
        
        BigDecimal total_debt = advanceDebt(index, year, month);
        excess = excess.subtract(total_debt);

        BigDecimal total_expense = advanceExpense(index, year, month);        
        excess = excess.subtract(total_expense);
                
        advanceInvestment(index, excess, year, month);        
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
}
