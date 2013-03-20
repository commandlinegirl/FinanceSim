package com.codelemma.finances;

import java.math.BigDecimal;

import android.app.AlertDialog;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class FrgIncome extends SherlockFragment {
	
	private Account account;

	
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
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgIncome.onStart()", "called");
		Finances appState = Finances.getInstance();
		account = appState.getAccount();
		
    	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.income_summary);
    	tip.removeAllViews();
    	

    	
		if (account.getIncomesSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = (Iterable<? extends NamedValue>) account.getIncomes();
		   	updateInputListing(values);		    
		} else {
	        TextView tv = new TextView(getSherlockActivity());
	        tv = new TextView(getSherlockActivity());
	        tv.setText(R.string.no_income_info);
	        tv.setPadding(0, Utils.px(getSherlockActivity(), 30), 0, 0);
	        tv.setGravity(Gravity.CENTER);
	        tip.addView(tv);
	    }
	}
	
	public void add(View view) {
		Intent intent = new Intent(getActivity(), AddIncomeGeneric.class);		
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
			yearly_income = new BigDecimal(data.getStringExtra("yearly_income"));
			income_tax_rate = new BigDecimal(data.getStringExtra("income_tax_rate"));
	        yearly_income_rise = new BigDecimal(data.getStringExtra("yearly_income_rise"));
	        income_installments = new BigDecimal(data.getStringExtra("income_installments"));			
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
    		IncomeGeneric income = (IncomeGeneric) account.getIncomeById(income_id); 
    		account.removeIncome(income);
      	}
    	
		IncomeGeneric newIncome = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, //TODO: cannot be ZERO! DivisionByzeroException
                income_name);            
        account.addIncome(newIncome);
	}	
	
	
    private void updateInputListing(Iterable<? extends NamedValue> values) {        
        InputListingUpdater modifier = new  InputListingUpdater(getSherlockActivity());
        for(NamedValue value : values) {        	
        	value.updateInputListing(modifier);        	
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
