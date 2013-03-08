package com.codelemma.finances;

import com.codelemma.finances.accounting.HistoryDebt;
import com.codelemma.finances.accounting.HistoryExpense;
import com.codelemma.finances.accounting.HistoryIncome;
import com.codelemma.finances.accounting.HistoryInvestment;
import com.codelemma.finances.accounting.HistoryInvestment401k;
import com.codelemma.finances.accounting.HistoryInvestmentSavAcct;
import com.codelemma.finances.accounting.PlotVisitor;

public class Plotter implements PlotVisitor {

	@Override
	public void plotDebt(HistoryDebt debtHistory) {
	}
	
	@Override
	public void plotExpense(HistoryExpense expenseHistory) {
	}
	
	@Override
    public void plotIncome(HistoryIncome incomeHistory) {
    }			

	@Override
	public void plotInvestment(HistoryInvestment401k historyInvestment401k) {		
	}

	@Override
	public void plotInvestment(HistoryInvestmentSavAcct historyInvestmentSavAcct) {
		
	}
	
}
