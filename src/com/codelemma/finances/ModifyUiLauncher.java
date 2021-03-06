package com.codelemma.finances;

import android.app.Activity;
import android.content.Intent;

import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.ModifyUiVisitor;

public class ModifyUiLauncher implements ModifyUiVisitor {

	private Activity activity;
	
	public ModifyUiLauncher(Activity activity) {
		this.activity = activity;
	}
		
	@Override
	public void launchModifyUiForDebtLoan(DebtLoan debt) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddDebtLoan.class);
        intent.putExtra("debt_id", debt.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
	public void launchModifyUiForDebtMortgage(DebtMortgage debt) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddDebtMortgage.class);
        intent.putExtra("debt_id", debt.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	
	@Override
	public void launchModifyUiForExpense(ExpenseGeneric expense) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddExpenseGeneric.class);
        intent.putExtra("expense_id", expense.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
    public void launchModifyUiForIncome(IncomeGeneric income) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddIncomeGeneric.class);
        intent.putExtra("income_id", income.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
    public void launchModifyUiForInvestmentSavAcct(InvestmentSavAcct investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestmentSavAcct.class);
        intent.putExtra("investment_id", investment.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}

	@Override
    public void launchModifyUiForInvestmentCheckAcct(InvestmentCheckAcct investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestmentCheckAcct.class);
        intent.putExtra("investment_id", investment.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
	public void launchModifyUiForInvestment401k(Investment401k investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestment401k.class);
        intent.putExtra("investment_id", investment.getId());
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
}
