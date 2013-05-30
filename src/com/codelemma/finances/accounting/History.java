package com.codelemma.finances.accounting;
import java.util.ArrayList;
import java.util.Collections;

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
		incomes.remove(inc);
	}

	public void removeExpenseHistory(HistoryNew hex) {
		expenses.remove(hex);
	}

	public void removeInvestmentHistory(HistoryNew inv) {
		investments.remove(inv);
	}
	
	public void removeDebtHistory(HistoryNew deb) {
		debts.remove(deb);
	}		
	
	public void removeCashflowsHistory(HistoryNew cash) {
		cashflows.remove(cash);
	}
	
	public void removeNetWorthHistory(HistoryNew netw) {
		net_worth.remove(netw);
	}
}   
