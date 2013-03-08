package com.codelemma.finances;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.Money;
import com.codelemma.finances.accounting.NamedValue;

public class FrgIncome extends SherlockFragment {
	
	private Account account;
	private History history;
	private int maxX = 60;
	private ModifyUiVisitor modifyUiLauncher;
				
	private OnClickListener modifyListener = new OnClickListener() {
    	@Override
	    public void onClick(View view) {
            TextView textView = (TextView) view;
	        textView.setBackgroundColor(0xFFE7C39C);	        
	        NamedValue value = (NamedValue) view.getTag(R.string.acct_object);
	        if (value != null) {
	            Log.d("FrgIncome.onClick()", value.getName());
	        } else {
	        	Log.d("FrgIncome.updateAcctElementListing()", "value is null");
	        }
	        value.launchModifyUi(modifyUiLauncher);	        
	    }
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment    	
        View frView = inflater.inflate(R.layout.frg_income, container, false);
        
        return frView;
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FrgIncome.onActivityCreated()", "called");		
        // Inflate the layout for this fragment 
		Finances appState = Finances.getInstance();
		history = appState.getHistory();
		modifyUiLauncher = new ModifyUiLauncher(getActivity());
	}


	
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgIncome.onStart()", "called");
		Finances appState = Finances.getInstance();
		account = appState.getAccount();			
		if (account.getIncomesSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = account.getIncomes();
		   	updateAcctElementListing(values, R.id.income_summary);		    
		} else {
	    	LinearLayout tip = (LinearLayout) getActivity().findViewById(R.id.income_summary);
	        tip.removeAllViews();
	    }
	}
	
	public void add(View view) {
		Intent intent = new Intent(getActivity(), AddIncome.class);		
		intent.putExtra("request", AcctElements.ADD.toString());
	    startActivityForResult(intent, AcctElements.ADD.getNumber());
	}
	
	private void showAlertDialog() {
    	new AlertDialog.Builder(getActivity()).setTitle("Not a number")
        .setMessage("Please, fill in field with a number")
        .setNeutralButton("Close", null)
        .show();		
	}
	
	public void onIncomeResult(Intent data, int requestCode) {
		BigDecimal yearly_income;
		BigDecimal income_tax_rate;
        BigDecimal yearly_income_rise;
        BigDecimal income_installments;
		
		try {
			yearly_income = Money.scale(new BigDecimal(data.getStringExtra("yearly_income")));
			income_tax_rate = Money.scaleRate(new BigDecimal(data.getStringExtra("income_tax_rate")));
	        yearly_income_rise = Money.scaleRate(new BigDecimal(data.getStringExtra("yearly_income_rise")));
	        income_installments = Money.scale(new BigDecimal(data.getStringExtra("income_installments")));			
		} catch (NumberFormatException e) {
			showAlertDialog();
	    	return; //TODO: return?
		}
		
    	String income_name = data.getStringExtra("income_name");     	
    	
    	if (income_installments.compareTo(new BigDecimal(1)) ==  -1) {
        	new AlertDialog.Builder(getActivity()).setTitle("Installments must be > 0")
            .setMessage("Please, set installments > 0.")
            .setNeutralButton("Close", null)
            .show();
        	return;
    	}
    	  	
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int income_id = data.getIntExtra("income_id", -1);    		
    		Income income = account.getIncomeById(income_id); 
    		account.removeIncome(income);
      	}
    	
		Income newIncome = new Income(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, //TODO: cannot be ZERO! DivisionByzeroException
                income_name);            
        account.addIncome(newIncome);
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
            Log.d("FrgIncome.updateAcctElementListing()", value.getName());
                                                         
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
