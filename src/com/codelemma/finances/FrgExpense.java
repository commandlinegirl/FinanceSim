package com.codelemma.finances;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.AccountingElement;

public class FrgExpense extends SherlockFragment {
	
	private Finances appState;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_expense, container, false);
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appState = Finances.getInstance();		
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgExpense.onStart()", "called");

    	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.expense_summary);
    	tip.removeAllViews();    	
		
		if (appState.getAccount().getExpensesSize() > 0) {	 
		   	Iterable<? extends AccountingElement> values = (Iterable<? extends AccountingElement>) appState.getAccount().getExpenses();
		   	updateInputListing(values);		    
		} else {
	        TextView tv = new TextView(getSherlockActivity());
	        tv.setText(R.string.no_expense_info);
	        tv.setGravity(Gravity.CENTER);
	        tv.setPadding(0, Utils.px(getSherlockActivity(), 30), 0, 0);
	        tip.addView(tv);
	    }
	}

	public void add(View view) {
		Intent intent = new Intent(getActivity(), AddExpenseGeneric.class);		
		intent.putExtra("request", AcctElements.ADD.toString());
	    startActivityForResult(intent, AcctElements.ADD.getNumber());
	}
		
	public void onExpenseResult(Intent data, int requestCode) {
    	String expense_name = data.getStringExtra("expense_name");		
		BigDecimal init_expense = new BigDecimal(data.getStringExtra("init_expense"));
		BigDecimal inflation_rate = new BigDecimal(data.getStringExtra("inflation_rate"));
		int expense_frequency = Integer.parseInt(data.getStringExtra("expense_frequency"));
       	int start_year = Integer.parseInt((data.getStringExtra("expensegeneric_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("expensegeneric_start_month")));
    	
    	String action = " added.";
        if (requestCode == AcctElements.UPDATE.getNumber()) {
        	int expense_id = data.getIntExtra("expense_id", -1);    		
    		ExpenseGeneric expense = (ExpenseGeneric) appState.getAccount().getExpenseById(expense_id); 
    		appState.getAccount().removeExpense(expense);
    		Log.d("FrgExpense.onExpenseResult()", "removed Expense No. "+expense_id);
    		action = " updated.";
      	}	    		
    		
    	ExpenseGeneric expense = new ExpenseGeneric(expense_name,
    			                      init_expense, 
    			                      inflation_rate, 
    			                      expense_frequency,
    			     		          start_year,
    			    		    	  start_month); 
    	appState.getAccount().addExpense(expense);
        Toast.makeText(getSherlockActivity(), expense_name+action, Toast.LENGTH_SHORT).show();
        
        if ((appState.getAccount().getCalculationStartYear() == start_year && appState.getAccount().getCalculationStartMonth() >= start_month) 
    			|| (appState.getAccount().getCalculationStartYear() > start_year)) {    
        	appState.getAccount().setCalculationStartYear(start_year);
        	appState.getAccount().setCalculationStartMonth(start_month);
        }
	}
	
    private void updateInputListing(Iterable<? extends AccountingElement> values) {        
        InputListingUpdater modifier = new  InputListingUpdater(getSherlockActivity());
        for(AccountingElement value : values) {        	
        	value.updateInputListing(modifier);        	
        } 		   		       
    }
}
