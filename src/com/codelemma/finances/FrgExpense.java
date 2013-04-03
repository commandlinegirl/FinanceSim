package com.codelemma.finances;

import java.math.BigDecimal;

import android.content.Intent;
import android.graphics.Color;
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
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.NamedValue;

public class FrgExpense extends SherlockFragment {
	
	private Account account;
	private Finances appState;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment    	
        View frView = inflater.inflate(R.layout.frg_expense, container, false);
        return frView;
	}
	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        // Inflate the layout for this fragment 
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
		

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgExpense.onStart()", "called");
		appState = Finances.getInstance();
		account = appState.getAccount();	
		
    	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.expense_summary);
    	tip.removeAllViews();
    	
        TextView tv = new TextView(getSherlockActivity());
        tv.setText(R.string.expenses_description);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#FF771100"));
        tv.setPadding(0, 5, 0, 10);
        tip.addView(tv);
		
	    View line = new View( getSherlockActivity());		
	 	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
	 			                                                        Utils.px(getSherlockActivity(), 1));	
	 	line.setLayoutParams(param);
	 	line.setBackgroundColor(0xFFCCCCCC);	    
		tip.addView(line);
		
		if (account.getExpensesSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = (Iterable<? extends NamedValue>) account.getExpenses();
		   	updateInputListing(values);		    
		} else {
	        tv = new TextView(getSherlockActivity());
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
    		ExpenseGeneric expense = (ExpenseGeneric) account.getExpenseById(expense_id); 
    		account.removeExpense(expense);
    		Log.d("FrgExpense.onExpenseResult()", "removed Expense No. "+expense_id);
    		action = " updated.";
      	}	    		
    		
    	ExpenseGeneric expense = new ExpenseGeneric(expense_name,
    			                      init_expense, 
    			                      inflation_rate, 
    			                      expense_frequency,
    			     		          start_year,
    			    		    	  start_month); 
    	account.addExpense(expense);
        Toast.makeText(getSherlockActivity(), expense_name+action, Toast.LENGTH_SHORT).show();
        
        if ((appState.getCalculationStartYear() == start_year && appState.getCalculationStartMonth() >= start_month) 
    			|| (appState.getCalculationStartYear() > start_year)) {    
    	    appState.setCalculationStartYear(start_year);
    	    appState.setCalculationStartMonth(start_month);
        }
	}
	
    private void updateInputListing(Iterable<? extends NamedValue> values) {        
        InputListingUpdater modifier = new  InputListingUpdater(getSherlockActivity());
        for(NamedValue value : values) {        	
        	value.updateInputListing(modifier);        	
        } 		   		       
    }
	    


}
