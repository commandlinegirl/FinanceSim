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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.NamedValue;

public class FrgIncome extends SherlockFragment {
	
	private Account account;
	private Finances appState;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frView = inflater.inflate(R.layout.frg_income, container, false);        
        return frView;
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FrgIncome.onActivityCreated()", "called");		
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgIncome.onStart()", "called");
		appState = Finances.getInstance();
		account = appState.getAccount();
		
    	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.income_summary);
    	tip.removeAllViews();
    	
        TextView tv = new TextView(getSherlockActivity());
        tv.setText(R.string.income_description);
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
		
		if (account.getIncomesSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = (Iterable<? extends NamedValue>) account.getIncomes();
		   	updateInputListing(values);		    
		} else {
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
	
	private void showNotANumberAlertDialog() {
    	new AlertDialog.Builder(getActivity()).setTitle("Not a number")
        .setMessage("Please, fill in the field with a number.")
        .setNeutralButton("Close", null)
        .show();		    	
	}
	
	public void onIncomeResult(Intent data, int requestCode) {
		String income_name;
		BigDecimal yearly_income;
		BigDecimal income_tax_rate;
        BigDecimal yearly_income_rise;
        BigDecimal income_installments;
       	int start_year = Integer.parseInt((data.getStringExtra("incomegeneric_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("incomegeneric_start_month")));

		try {		
    	    income_name = data.getStringExtra("income_name");     	
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		}
		
		try {
			yearly_income = new BigDecimal(data.getStringExtra("yearly_income"));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showNotANumberAlertDialog();
		    return;
		}	
		
		try {
			income_tax_rate = new BigDecimal(data.getStringExtra("income_tax_rate"));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showNotANumberAlertDialog();
		    return;
		}
		
		try {
	        yearly_income_rise = new BigDecimal(data.getStringExtra("yearly_income_rise"));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showNotANumberAlertDialog();
		    return;
		}
	     
		try {
	        income_installments = new BigDecimal(data.getStringExtra("income_installments"));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showNotANumberAlertDialog();
		    return;
		}
	     
		
    	if (income_installments.compareTo(new BigDecimal(1)) ==  -1) {
        	new AlertDialog.Builder(getActivity()).setTitle("Payment frequency must be larger than 0")
            .setMessage("Please, set frequency to a positive number.")
            .setNeutralButton("Close", null)
            .show();
        	return;
    	}
	
    	String action = " added.";
    	Investment401k investment401k = null;
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int income_id = data.getIntExtra("income_id", -1);    		
    		IncomeGeneric income = (IncomeGeneric) account.getIncomeById(income_id); 
    		investment401k = income.getInvestment401k();
    		account.removeIncome(income);
    		action = " updated.";
      	}
    	
		IncomeGeneric newIncome = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, 
                income_name,
		        start_year,
		    	start_month);
		newIncome.setInvestment401k(investment401k);
		if (investment401k != null) {
			investment401k.setIncome(newIncome);	
		}		
        account.addIncome(newIncome);
        
        Toast.makeText(getSherlockActivity(), income_name+action, Toast.LENGTH_SHORT).show();
        
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
