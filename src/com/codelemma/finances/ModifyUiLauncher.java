package com.codelemma.finances;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.codelemma.finances.accounting.Debt;
import com.codelemma.finances.accounting.Expense;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.ModifyUiVisitor;

public class ModifyUiLauncher implements ModifyUiVisitor {

	private Activity activity;
	
	public ModifyUiLauncher(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void launchModifyUiForDebt(Debt debt) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddDebt.class);
        intent.putExtra("debt_id", debt.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForDebt(debt)", String.valueOf(debt.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
	public void launchModifyUiForExpense(Expense expense) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddExpense.class);
        intent.putExtra("expense_id", expense.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForExpense(expense)", String.valueOf(expense.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
    public void launchModifyUiForIncome(Income income) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddIncome.class);
        intent.putExtra("income_id", income.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForIncome(income)", String.valueOf(income.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
    public void launchModifyUiForInvestment(InvestmentSavAcct investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestmentSavAcct.class);
        intent.putExtra("investment_id", investment.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForInvestment(investment)", String.valueOf(investment.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}

	@Override
	public void launchModifyUiForInvestment(Investment401k investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestment401k.class);
        intent.putExtra("investment_id", investment.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForInvestment(investment)", String.valueOf(investment.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
	public void launchModifyUiForInvestment(InvestmentBond investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestmentBond.class);
        intent.putExtra("investment_id", investment.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForInvestment(investment)", String.valueOf(investment.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
	
	@Override
	public void launchModifyUiForInvestment(InvestmentStock investment) {
    	Intent intent = new Intent(activity.getApplicationContext(), AddInvestmentStock.class);
        intent.putExtra("investment_id", investment.getId());
        Log.d("ModifyUiLauncher.launchModifyUiForInvestment(investment)", String.valueOf(investment.getId()));
        intent.putExtra("request", AcctElements.UPDATE.toString());   
	    activity.startActivityForResult(intent, AcctElements.UPDATE.getNumber());
	}
}
