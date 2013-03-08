package com.codelemma.finances;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.Expense;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class FrgExpense extends SherlockFragment {
	
	private Account account;
	private History history;
	private ModifyUiVisitor modifyUiLauncher;
				
	private OnClickListener modifyListener = new OnClickListener() {
    	@Override
	    public void onClick(View view) {
            TextView textView = (TextView) view;
	        textView.setBackgroundColor(0xFFE7C39C);	        
	        NamedValue value = (NamedValue) view.getTag(R.string.acct_object);
	        if (value != null) {
	            Log.d("FrgExpense.onClick()", value.getName());
	        } else {
	        	Log.d("FrgExpense.updateAcctElementListing()", "value is null");
	        }
	        value.launchModifyUi(modifyUiLauncher);	        
	    }
	};

	
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
		modifyUiLauncher = new ModifyUiLauncher(getActivity());

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgExpense.onStart()", "called");
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
		
		if (account.getExpensesSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = account.getExpenses();
		   	updateAcctElementListing(values, R.id.expense_summary);		    
		} else {
	    	LinearLayout tip = (LinearLayout) getActivity().findViewById(R.id.expense_summary);
	        tip.removeAllViews();
	    }
	}

	public void add(View view) {
		Intent intent = new Intent(getActivity(), AddExpense.class);		
		intent.putExtra("request", AcctElements.ADD.toString());
	    startActivityForResult(intent, AcctElements.ADD.getNumber());
	}
		
	public void onExpenseResult(Intent data, int requestCode) {
    	String expense_name = data.getStringExtra("expense_name");		
		BigDecimal init_expense = new BigDecimal(data.getStringExtra("init_expense"));
		BigDecimal inflation_rate = new BigDecimal(data.getStringExtra("inflation_rate"));
		int expense_frequency = Integer.parseInt(data.getStringExtra("expense_frequency"));
		
        if (requestCode == AcctElements.UPDATE.getNumber()) {
        	int expense_id = data.getIntExtra("expense_id", -1);    		
    		Expense expense = account.getExpenseById(expense_id); 
    		account.removeExpense(expense);
    		Log.d("FrgExpense.onExpenseResult()", "removed Expense No. "+expense_id);
      	}	    		
    		
    	Expense expense = new Expense(expense_name,
    			                      init_expense, 
    			                      inflation_rate, 
    			                      expense_frequency);
    	account.addExpense(expense);
    	
	}
	
	   private void updateAcctElementListing(Iterable<? extends NamedValue> values, int tipId) {
	        
	    	LinearLayout tip = (LinearLayout) getActivity().findViewById(tipId);
	        tip.removeAllViews();
	        
	        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
	        	                                                                  LinearLayout.LayoutParams.WRAP_CONTENT);                  
	        
	        for(NamedValue value : values) {
	        	                
	            TextView tv = new TextView(getActivity());
	            tv.setText(value.getName() + " " + (value.getValue()).toString());
	            tv.setLayoutParams(layoutParam);
	            int padding = Utils.px(getActivity(), 6);
	            tv.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     
	            tv.setOnClickListener(modifyListener);
	            tv.setTag(R.string.acct_object, value);
	            Log.d("FrgExpense.updateAcctElementListing()", value.getName());
	                                                         
	            tip.addView(tv);
	            addUnderline(tip);
	        } 		   		
	    }	
	    
		private void addUnderline(LinearLayout underline) {	    
		    View v = new View(getActivity());		
		 	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
		 			                                                        Utils.px(getActivity(), 1));	
			v.setLayoutParams(param);
			v.setBackgroundColor(0xFFCCCCCC);	    
		    underline.addView(v);
	    }	

}
