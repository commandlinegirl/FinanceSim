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
import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.NamedValue;

public class FrgDebt extends SherlockFragment {
	
	private Account account;
    private Finances appState;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment    	
        View frView = inflater.inflate(R.layout.frg_debt, container, false);
        return frView;
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        // Inflate the layout for this fragment 
		appState = Finances.getInstance();
		account = appState.getAccount();	
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgDebt.onStart()", "called");	
		LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.debt_summary);
    	tip.removeAllViews();
    	
        TextView tv = new TextView(getSherlockActivity());
        tv.setText(R.string.debt_description);
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
        
		if (account.getDebtsSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = account.getDebts();
		   	updateInputListing(values);		    
		} else {
	        tv = new TextView(getSherlockActivity());
	        tv.setText(R.string.no_debt_info);
	        tv.setGravity(Gravity.CENTER);
	        tv.setPadding(0, Utils.px(getSherlockActivity(), 30), 0, 0);
	        tip.addView(tv);
	    }
	}
			
	public void add(View view) {
	    Intent intent;
		switch(view.getId()) {
		case R.id.debt_loan:
			intent = new Intent(getActivity(), AddDebtLoan.class);		
			intent.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intent, AcctElements.ADD.getNumber());
		    break;
		case R.id.debt_mortgage:
			intent = new Intent(getActivity(), AddDebtMortgage.class);		
			intent.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intent, AcctElements.ADD.getNumber());			
            break;
		}	    
	    
	}
	
	private void showAlertDialog() {
    	new AlertDialog.Builder(getActivity()).setTitle("Not a number")
        .setMessage("Please, fill in field with a number")
        .setNeutralButton("Close", null)
        .show();		
	}

	
	public void onDebtLoanResult(Intent data, int requestCode) {		
    	String name = data.getStringExtra("debtloan_name"); //TODO: trhwo exception when there is no such key!!!
    	BigDecimal amount = new BigDecimal(data.getStringExtra("debtloan_amount"));
    	BigDecimal interest_rate = new BigDecimal(data.getStringExtra("debtloan_interest_rate"));
    	int term = Integer.parseInt((data.getStringExtra("debtloan_term")));
    	BigDecimal extra_payment = new BigDecimal(data.getStringExtra("debtloan_extra_payment"));
    	int start_year = Integer.parseInt((data.getStringExtra("debtloan_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("debtloan_start_month")));
    	
		if (requestCode == AcctElements.UPDATE.getNumber()) {
      	
		    int debt_id = data.getIntExtra("debt_id", -1);    		
		    DebtLoan debt = (DebtLoan) account.getDebtById(debt_id); 
		    account.removeDebt(debt);
		    Log.d("FrgDebt.onDebtLoanResult()", "removed Debt No. "+debt_id);
		}
		
		DebtLoan debt = new DebtLoan(name, 
	    		amount,
	    		interest_rate,
	    		term,
	    		extra_payment,
	    		start_year,
	    		start_month); 
	    account.addDebt(debt);
	    	    
        if ((appState.getCalculationStartYear() == start_year && appState.getCalculationStartMonth() >= start_month) 
        			|| (appState.getCalculationStartYear() > start_year)) {    
        	appState.setCalculationStartYear(start_year);
        	appState.setCalculationStartMonth(start_month);
        }
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();

	}	
	
	public void onDebtMortgageResult(Intent data, int requestCode) {		
    	String name = data.getStringExtra("debtmortgage_name");    	
    	BigDecimal price = new BigDecimal(data.getStringExtra("debtmortgage_purchase_price"));
    	BigDecimal downpayment = new BigDecimal(data.getStringExtra("debtmortgage_downpayment"));
    	BigDecimal interest_rate = new BigDecimal(data.getStringExtra("debtmortgage_interest_rate"));
    	int term = Integer.parseInt((data.getStringExtra("debtmortgage_term")));
    	BigDecimal property_insurance = new BigDecimal(data.getStringExtra("debtmortgage_property_insurance"));
    	BigDecimal property_tax = new BigDecimal(data.getStringExtra("debtmortgage_property_tax"));
    	BigDecimal pmi = new BigDecimal(data.getStringExtra("debtmortgage_pmi"));
    	int start_year = Integer.parseInt((data.getStringExtra("debtmortgage_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("debtmortgage_start_month")));
    	
		if (requestCode == AcctElements.UPDATE.getNumber()) {
      	
		    int debt_id = data.getIntExtra("debt_id", -1);    		
		    DebtMortgage debt = (DebtMortgage) account.getDebtById(debt_id); 
		    account.removeDebt(debt);
		    Log.d("FrgDebt.onDebtMortgageResult()", "removed Debt No. "+debt_id);
		}
		
		DebtMortgage debt = new DebtMortgage(
				 name, 
	    		 price,
	    		 downpayment,
	    		 interest_rate,
	    		 term,
	    		 property_insurance,
	    		 property_tax,
	    		 pmi,
		         start_year,
		    	 start_month); 
	    account.addDebt(debt);	   		
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();
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
