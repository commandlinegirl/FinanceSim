package com.codelemma.finances.accounting;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class History {	
	private int month;  
	private int year; 
	private int listSize;	
	private Months[] months = Months.values(); 
	private String[] dates; // each elements is of "Jan 2013" format
	private ArrayList<HistoryDebt> debts = new ArrayList<HistoryDebt>();
	private ArrayList<HistoryExpense> expenses = new ArrayList<HistoryExpense>();
	private ArrayList<HistoryIncome> incomes = new ArrayList<HistoryIncome>();
	private ArrayList<HistoryInvestment> investments = new ArrayList<HistoryInvestment>();
    
    public History(int year, int month, int listSize) {
    	this.year = year;   // month and year make together the date corresponding
    	this.month = month; // to the first value in each of the ArrayLists
    	this.listSize = listSize;
    	dates = new String[listSize];
    	createDateList();
    }
    
    public void createDateList() {
    	int i;
    	int y = year;
    	int m = month;
    	for (i = 0; i < listSize; i++) {
    		dates[i] = months[m]+" "+y;
    		if (m == 11) {
                m = 0;
                y += 1;
            } else {
                m++;
            }        	
    	}    	    	
    }
    
    public int getMonth() {
    	return month;
    }
    
    public int getYear() {
    	return year;
    }
    
    public void addExpenseHistory(HistoryExpense n) {
        expenses.add(n);
    }

    public void addIncomeHistory(HistoryIncome n) {
        incomes.add(n);
    }

    public void addDebtHistory(HistoryDebt n) {
        debts.add(n);
    }
    
    public void addInvestmentHistory(HistoryInvestment n) {
        investments.add(n);
    }

    public String[] getDates() {
    	return dates;
    }

    public Iterable<HistoryExpense> getExpenseHistory() {
        return Collections.unmodifiableList(expenses);
    }

    public Iterable<HistoryIncome> getIncomeHistory() {
        return Collections.unmodifiableList(incomes);
    }

    public Iterable<HistoryDebt> getDebtHistory() {
        return Collections.unmodifiableList(debts);
    }

    public Iterable<HistoryInvestment> getInvestmentHistory() {
        return Collections.unmodifiableList(investments);
    }
       
    public void clear() {
    	incomes.clear();    	
    	expenses.clear();  	
    	investments.clear();
    	debts.clear();    	    	
    }

	public void removeIncomeHistory(HistoryIncome inc) {
		boolean t = incomes.remove(inc);
		Log.d("Income hist removed", String.valueOf(t));		
	}

	public void removeExpenseHistory(HistoryExpense hex) {
		boolean t = expenses.remove(hex);
		Log.d("Expense hist removed", String.valueOf(t));
	}

	public void removeInvestmentHistory(HistoryInvestment inv) {
		boolean t = investments.remove(inv);
		Log.d("Investment hist removed", String.valueOf(t));
	}
	
	public void removeDebtHistory(HistoryDebt deb) {
		boolean t = debts.remove(deb);
		Log.d("Debt hist removed", String.valueOf(t));
	}
	
	
	
}   
