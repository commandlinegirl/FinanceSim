package com.codelemma.finances;

import java.math.BigDecimal;

import android.app.AlertDialog;
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
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.AccountingElement;

public class FrgIncome extends SherlockFragment {
	
	private Finances appState;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {       
        return inflater.inflate(R.layout.frg_income, container, false);
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FrgIncome.onActivityCreated()", "called");
		appState = Finances.getInstance();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgIncome.onStart()", "called");
		
    	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.income_summary);
    	tip.removeAllViews();
		
		if (appState.getAccount().getIncomesSize() > 0) {	 
		   	Iterable<? extends AccountingElement> values = (Iterable<? extends AccountingElement>) appState.getAccount().getIncomes();
		   	updateInputListing(values);		    
		} else {
			TextView tv = new TextView(getSherlockActivity());
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
	
	private void showBadDateAlertDialog() {
    	new AlertDialog.Builder(getActivity()).setTitle("Date incorrect")
        .setMessage("Please, fill in \"start date\" with a correct date.")
        .setNeutralButton("Close", null)
        .show();
	}
	
	public void onIncomeResult(Intent data, int requestCode) {
		String income_name;
		BigDecimal yearly_income;
		BigDecimal income_tax_rate;
        BigDecimal yearly_income_rise;
        BigDecimal income_installments;
        int income_term;
       	int start_year;
    	int start_month;

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
	
		try {
	        income_term = Integer.parseInt((data.getStringExtra("income_term")));
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

		try {
			start_year = Integer.parseInt((data.getStringExtra("incomegeneric_start_year")));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showBadDateAlertDialog();
		    return;
		} 
		
		try {
			start_month = Integer.parseInt((data.getStringExtra("incomegeneric_start_month")));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		    return;
		} catch (NumberFormatException nfe) {
		    nfe.printStackTrace();
		    showBadDateAlertDialog();
		    return;
		} 
		
    	String action = " added.";
    	Investment401k investment401k = null;
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int income_id = data.getIntExtra("income_id", -1);    		
    		IncomeGeneric income = (IncomeGeneric) appState.getAccount().getIncomeById(income_id); 
    		investment401k = income.getInvestment401k();
    		appState.getAccount().removeIncome(income);
    		action = " updated.";
      	}
    	
		IncomeGeneric newIncome = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, 
                income_name,
                income_term,
		        start_year,
		    	start_month);
		newIncome.setInvestment401k(investment401k);
		if (investment401k != null) {
			investment401k.setIncome(newIncome);	
		}		
		appState.getAccount().addIncome(newIncome);
        Toast.makeText(getSherlockActivity(), income_name+action, Toast.LENGTH_SHORT).show();
        
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
