package com.codelemma.finances.accounting;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class History {

	private ArrayList<HistoryNew> debts = new ArrayList<HistoryNew>();
	private ArrayList<HistoryNew> expenses = new ArrayList<HistoryNew>();
	private ArrayList<HistoryNew> incomes = new ArrayList<HistoryNew>();
	private ArrayList<HistoryNew> investments = new ArrayList<HistoryNew>();	
	private ArrayList<HistoryNew> cashflows = new ArrayList<HistoryNew>();	
	private ArrayList<HistoryNew> net_worth = new ArrayList<HistoryNew>();	

    private ArrayList<ArrayList<HistoryNew>> histories = new ArrayList<ArrayList<HistoryNew>>(6);
            
    public History() {

    	histories.add(incomes);
    	histories.add(expenses);
    	histories.add(investments);
    	histories.add(debts);
    	histories.add(cashflows);
    	histories.add(net_worth);
    }
    
    public void addExpenseHistory(HistoryNew n) {
        expenses.add(n);
    }

    public void addIncomeHistory(HistoryNew n) {
        incomes.add(n);
    }

    public void addDebtHistory(HistoryNew n) {
        debts.add(n);
    }
    
    public void addInvestmentHistory(HistoryNew n) {
        investments.add(n);
    }

    public void addCashflowHistory(HistoryNew n) {
        cashflows.add(n);
    }
    
    public void addNetWorthHistory(HistoryNew n) {
        net_worth.add(n);
    }

    public Iterable<HistoryNew> getHistory(int index) {
        return Collections.unmodifiableList(histories.get(index));
    }
    
    public void clear() {
    	incomes.clear();    	
    	expenses.clear();  	
    	investments.clear();
    	debts.clear();
    	cashflows.clear();   	    	
    	net_worth.clear();    	    	    	
    }

	public void removeIncomeHistory(HistoryNew inc) {
		boolean t = incomes.remove(inc);
		Log.d("Income hist removed", String.valueOf(t));		
	}

	public void removeExpenseHistory(HistoryNew hex) {
		boolean t = expenses.remove(hex);
		Log.d("Expense hist removed", String.valueOf(t));
	}

	public void removeInvestmentHistory(HistoryNew inv) {
		boolean t = investments.remove(inv);
		Log.d("Investment hist removed", String.valueOf(t));
	}
	
	public void removeDebtHistory(HistoryNew deb) {
		boolean t = debts.remove(deb);
		Log.d("Debt hist removed", String.valueOf(t));
	}		
	
	public void removeCashflowsHistory(HistoryNew cash) {
		boolean t = cashflows.remove(cash);
		Log.d("Cashflow hist removed", String.valueOf(t));
	}
	
	public void removeNetWorthHistory(HistoryNew netw) {
		boolean t = net_worth.remove(netw);
		Log.d("Debt hist removed", String.valueOf(t));
	}
}   
