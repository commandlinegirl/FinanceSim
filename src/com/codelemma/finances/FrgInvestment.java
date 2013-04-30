package com.codelemma.finances;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.AccountingElement;

public class FrgInvestment extends SherlockFragment {
	
	private Finances appState;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_investment, container, false);
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FrgInvestment.onActivityCreated()", "called");	
		appState = Finances.getInstance();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgInvestment.onStart()", "called");
		
	   	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.investment_summary);
    	tip.removeAllViews();
		
		if (appState.getAccount().getInvestmentsSize() > 0) {	 
		   	Iterable<? extends AccountingElement> values = (Iterable<? extends AccountingElement>) appState.getAccount().getInvestments();
		   	updateInputListing(values);		    
		} else {
	        TextView tv = new TextView(getSherlockActivity());
	        tv.setText(R.string.no_investment_info);
	        tv.setGravity(Gravity.CENTER);
	        tv.setPadding(0, Utils.px(getSherlockActivity(), 30), 0, 0);
	        tip.addView(tv);
	    }
	}
	
	public void add(View view) {		
		switch(view.getId()) {
		case R.id.investment_savacct:
			Intent intent = new Intent(getSherlockActivity(), AddInvestmentSavAcct.class);		
			intent.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intent, AcctElements.ADD.getNumber());
		    break;
		case R.id.investment_401k:
			
			if (appState.getAccount().getIncomesSize() == 0) {
	        	new AlertDialog.Builder(getSherlockActivity())
	            .setTitle("Adding 401(k) requires an income/salary account")
                .setMessage("To add a 401(k) account, please, add an income account first.")                
                .setNeutralButton("Add Income", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {                	   
                	   ((Main) getSherlockActivity()).setToIncomeTab();
                   }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialog.cancel();
                   }
               })
              .show();
			} else {
				Intent intent401k = new Intent(getActivity(), AddInvestment401k.class);		
				intent401k.putExtra("request", AcctElements.ADD.toString());
			    startActivityForResult(intent401k, AcctElements.ADD.getNumber());	
			}									   
		    break;
		}		
	}
	
	public void onInvestmentSavAcctResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investmentsav_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investmentsav_init_amount"));
		BigDecimal tax_rate = new BigDecimal(data.getStringExtra("investmentsav_tax_rate"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investmentsav_percontrib"));
		int capitalization = Integer.parseInt(data.getStringExtra("investmentsav_capitalization"));
		BigDecimal interest_rate = new BigDecimal(data.getStringExtra("investmentsav_interest_rate"));
    	int start_year = Integer.parseInt((data.getStringExtra("investmentsav_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("investmentsav_start_month")));
    	
		String action = " added.";
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investmentsav_id", -1);    		
    		InvestmentSavAcct investment = (InvestmentSavAcct) appState.getAccount().getInvestmentById(investment_id); 
    		appState.getAccount().removeInvestment(investment);
    		appState.getAccount().subtractFromInvestmentsPercontrib(investment.getPercontrib()); // update total percent contribution to investments  
    		appState.getAccount().setCheckingAcctPercontrib();
    		Log.d("Main.onInvestmentResult()", "removed Investment No. "+investment_id);
    		action = " updated.";
      	}
    	
    	InvestmentSavAcct investment = new InvestmentSavAcct(name,
    			init_amount,
    			tax_rate,                
                percontrib,
                capitalization,
                interest_rate,
		        start_year,
		    	start_month);                 
    	appState.getAccount().addInvestment(investment);
    	appState.getAccount().addToInvestmentsPercontrib(percontrib); // update total percent contribution to investments  
    	appState.getAccount().setCheckingAcctPercontrib();
    	Log.d("FrgInvestment TotalInvestmentsPercontrib", appState.getAccount().getInvestmentsPercontrib().toString());
    	Log.d("FrgInvestment checkingAcctPercontrib", appState.getAccount().getCheckingAcctPercontrib().toString());

        Toast.makeText(getSherlockActivity(), name+action, Toast.LENGTH_SHORT).show();
        
        if ((appState.getAccount().getCalculationStartYear() == start_year && appState.getAccount().getCalculationStartMonth() >= start_month) 
    			|| (appState.getAccount().getCalculationStartYear() > start_year)) {    
        	appState.getAccount().setCalculationStartYear(start_year);
        	appState.getAccount().setCalculationStartMonth(start_month);
        }
	}
	
	public void onInvestmentCheckAcctResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investmentcheck_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investmentcheck_init_amount"));
		BigDecimal tax_rate = new BigDecimal(data.getStringExtra("investmentcheck_tax_rate"));
		int capitalization = Integer.parseInt(data.getStringExtra("investmentcheck_capitalization"));
		BigDecimal interest_rate = new BigDecimal(data.getStringExtra("investmentcheck_interest_rate"));
    	int start_year = appState.getAccount().getSimulationStartYear();
    	int start_month = appState.getAccount().getSimulationStartMonth();
    	
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investmentcheck_id", -1);    		
    		InvestmentCheckAcct investment = (InvestmentCheckAcct) appState.getAccount().getInvestmentById(investment_id);
    		appState.getAccount().setCheckingAcct(null);
    		appState.getAccount().removeInvestment(investment);
    		Log.d("Main.onInvestmentResult()", "removed Investment No. "+investment_id);
      	}
    	
    	InvestmentCheckAcct investment = new InvestmentCheckAcct(name,
    			init_amount,
    			tax_rate,     
                capitalization,
                interest_rate,
		        start_year,
		    	start_month);                 
    	appState.getAccount().addInvestment(investment);
    	appState.getAccount().setCheckingAcct(investment);
        
        Toast.makeText(getSherlockActivity(), name+" updated.", Toast.LENGTH_SHORT).show();
        
	}
	
	public void onInvestment401kResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investment401k_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investment401k_init_amount"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investment401k_percontrib"));
		int period = Integer.parseInt(data.getStringExtra("investment401k_period"));
		BigDecimal interest_rate = new BigDecimal(data.getStringExtra("investment401k_interest_rate"));
		BigDecimal withdrawal_tax_rate = new BigDecimal(data.getStringExtra("investment401k_withdrawal_tax_rate"));
		BigDecimal employer_match = new BigDecimal(data.getStringExtra("investment401k_employer_match"));
    	int start_year = Integer.parseInt((data.getStringExtra("investment401k_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("investment401k_start_month")));
    	int income_id =  Integer.parseInt((data.getStringExtra("investment401k_incomeid")));   	
    	
		String action = " added.";

    	Income income = appState.getAccount().getIncomeById(income_id);
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investment401k_id", -1);    		
    		Investment401k investment = (Investment401k) appState.getAccount().getInvestmentById(investment_id);     		
    		/* Before removing investment account, set the income with which it is associated
    		 * to null.
    		 */
    		investment.getIncome().setInvestment401k(null);
    		appState.getAccount().removeInvestment(investment);
    		action = " updated.";

      	}

    	Investment401k investment = new Investment401k(name,
    			init_amount,
                percontrib,
                period,
                interest_rate, 
                income,
                withdrawal_tax_rate,
                employer_match,
		        start_year,
		    	start_month);                     	   	
    	    	  	
    	income.setInvestment401k(investment);
    	appState.getAccount().addInvestment(investment);
        Toast.makeText(getSherlockActivity(), name+action, Toast.LENGTH_SHORT).show();
        
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
