package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class Account {
    
    private int income_id = 0;    
    private int expense_id = 0;
    private int investment_id = 0;
    private int debt_id = 0;
    
    private Map<Integer,Income> income_ids = new HashMap<Integer,Income>();
    private Map<Integer,Expense> expense_ids = new HashMap<Integer,Expense>();
    private Map<Integer,Investment> investment_ids = new HashMap<Integer,Investment>();
    private Map<Integer,Debt> debt_ids = new HashMap<Integer,Debt>();
                
    private ArrayList<Income> incomes = new ArrayList<Income>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private ArrayList<Investment> investments = new ArrayList<Investment>();
    private ArrayList<Debt> debts = new ArrayList<Debt>();

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
    
    public BigDecimal advanceDebt(int month, int index) {
    	BigDecimal total_debt = Money.scale(new BigDecimal(0));
        for (Debt debt: debts) {
            debt.advance(month);
            debt.getHistory().add(index, debt);
            total_debt = total_debt.add(debt.getAmount());
        }
        return total_debt;
    }  
    
    public BigDecimal advanceExpense(int month, int index) {
    	BigDecimal total_expense = Money.scale(new BigDecimal(0));
        for (Expense expense: expenses) {
            expense.advance(month);
            expense.getHistory().add(index, expense);
            total_expense = total_expense.add(expense.getAmount()); 
        }
        return total_expense;
    }
    
    public BigDecimal advanceIncome(int month, int index) {
    	BigDecimal total_income = Money.scale(new BigDecimal(0));
        for (Income income: incomes) {
            income.advance(month);
            income.getHistory().add(index, income);
            total_income = total_income.add(income.getAmount());
        }
        return total_income;
    }
    
    public void advanceInvestment(int month, BigDecimal excess, int index) {
        for (Investment investment: investments) {
            investment.advance(month, excess);
            investment.getHistory().add(index, investment);            
        }
    }
    
    public void addToHistory(History history) {
    	for (Investment investment: investments) {
    		history.addInvestmentHistory((HistoryInvestment) investment.getHistory());
    	}
    	
    	for (Income income: incomes) {
    		history.addIncomeHistory((HistoryIncome) income.getHistory());
    	}
    	
    	for (Expense expense: expenses) {
    		history.addExpenseHistory((HistoryExpense) expense.getHistory());
    	}
    	
    	for (Debt debt: debts) {
    		history.addDebtHistory((HistoryDebt) debt.getHistory());
    	}
    }
    
	public void clearHistory(History history) {
		history.clear();
	}
      
    public void advance(int index, int month) {
    	// year and month - starting date of predictions (saved to history here)
        Preconditions.checkInBounds(month, 0, 11, "Month must be in 0..11");
        BigDecimal excess = Money.scale(new BigDecimal(0));
        
        BigDecimal total_income = advanceIncome(month, index);
        excess = excess.add(total_income);
        
        BigDecimal total_debt = advanceDebt(month, index);
        excess = excess.subtract(total_debt);

        BigDecimal total_expense = advanceExpense(month, index);        
        excess = excess.subtract(total_expense);
                
        advanceInvestment(month, excess, index);
    }

}
