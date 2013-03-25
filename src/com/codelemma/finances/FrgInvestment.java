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
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class FrgInvestment extends SherlockFragment {
	
	private Account account;
	private Finances appState;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment    	
        View frView = inflater.inflate(R.layout.frg_investment, container, false);
        return frView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FrgInvestment.onActivityCreated()", "called");		
        // Inflate the layout for this fragment 
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgInvestment.onStart()", "called");
		appState = Finances.getInstance();
		account = appState.getAccount();	
		
	   	LinearLayout tip = (LinearLayout) getSherlockActivity().findViewById(R.id.investment_summary);
    	tip.removeAllViews();
    	
        TextView tv = new TextView(getSherlockActivity());
        tv.setText(R.string.investments_description);
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
		
		if (account.getInvestmentsSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = (Iterable<? extends NamedValue>) account.getInvestments();
		   	updateInputListing(values);		    
		} else {
	        tv = new TextView(getSherlockActivity());
	        tv.setText(R.string.no_investment_info);
	        tv.setGravity(Gravity.CENTER);
	        tv.setPadding(0, Utils.px(getSherlockActivity(), 30), 0, 0);
	        tip.addView(tv);
	    }
	}
	

	
	public void add(View view) {		
		switch(view.getId()) {
		case R.id.investment_savacct:
			Intent intent = new Intent(getActivity(), AddInvestmentSavAcct.class);		
			intent.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intent, AcctElements.ADD.getNumber());
		    break;
		case R.id.investment_401k:
			Intent intent401k = new Intent(getActivity(), AddInvestment401k.class);		
			intent401k.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intent401k, AcctElements.ADD.getNumber());			
            break;
		case R.id.investment_bond:
			Intent intentbond = new Intent(getActivity(), AddInvestmentBond.class);		
			intentbond.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intentbond, AcctElements.ADD.getNumber());	
		    break;
		case R.id.investment_stock:
			Intent intentstock = new Intent(getActivity(), AddInvestmentStock.class);		
			intentstock.putExtra("request", AcctElements.ADD.toString());
		    startActivityForResult(intentstock, AcctElements.ADD.getNumber());
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
    	
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investmentsav_id", -1);    		
    		InvestmentSavAcct investment = (InvestmentSavAcct) account.getInvestmentById(investment_id); 
    		account.removeInvestment(investment);
    		Log.d("Main.onInvestmentResult()", "removed Investment No. "+investment_id);
      	}
    	
    	InvestmentSavAcct investment = new InvestmentSavAcct(name,
    			init_amount,
    			tax_rate,                
                percontrib,
                capitalization,
                interest_rate,
		        start_year,
		    	start_month);                 
        account.addInvestment(investment);		
        
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();
        
        if ((appState.getCalculationStartYear() == start_year && appState.getCalculationStartMonth() >= start_month) 
    			|| (appState.getCalculationStartYear() > start_year)) {    
    	appState.setCalculationStartYear(start_year);
    	appState.setCalculationStartMonth(start_month);
    }
	}
	
	public void onInvestment401kResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investment401k_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investment401k_init_amount"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investment401k_percontrib"));
		int period = Integer.parseInt(data.getStringExtra("investment401k_period"));
		BigDecimal interest_rate = new BigDecimal(data.getStringExtra("investment401k_interest_rate"));
		BigDecimal salary = new BigDecimal(data.getStringExtra("investment401k_salary"));
		BigDecimal payrise = new BigDecimal(data.getStringExtra("investment401k_payrise"));
		BigDecimal withdrawal_tax_rate = new BigDecimal(data.getStringExtra("investment401k_withdrawal_tax_rate"));
		BigDecimal employer_match = new BigDecimal(data.getStringExtra("investment401k_employer_match"));
    	int start_year = Integer.parseInt((data.getStringExtra("investment401k_start_year")));
    	int start_month = Integer.parseInt((data.getStringExtra("investment401k_start_month")));
    	
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investment401k_id", -1);    		
    		Investment401k investment = (Investment401k) account.getInvestmentById(investment_id); 
    		account.removeInvestment(investment);
    		Log.d("Main.onInvestment401kResult()", "removed Investment No. "+investment_id);
      	}
    	
    	Investment401k investment = new Investment401k(name,
    			init_amount,
                percontrib,
                period,
                interest_rate, 
                salary,
                payrise,
                withdrawal_tax_rate,
                employer_match,
		        start_year,
		    	start_month);                 
        account.addInvestment(investment);		
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();
        
        if ((appState.getCalculationStartYear() == start_year && appState.getCalculationStartMonth() >= start_month) 
    			|| (appState.getCalculationStartYear() > start_year)) {    
    	    appState.setCalculationStartYear(start_year);
    	    appState.setCalculationStartMonth(start_month);
        }
	}
	
	public void onInvestmentBondResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investmentbond_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investmentbond_init_amount"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investmentbond_percontrib"));
		BigDecimal tax_rate = new BigDecimal(data.getStringExtra("investmentbond_tax_rate"));
		
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investmentbond_id", -1);    		
    		InvestmentBond investment = (InvestmentBond) account.getInvestmentById(investment_id); 
    		account.removeInvestment(investment);
    		Log.d("Main.onInvestmentBondResult()", "removed Investment No. "+investment_id);
      	}
    	
    	InvestmentBond investment = new InvestmentBond(name,
    			init_amount,
                percontrib,
                tax_rate);
        account.addInvestment(investment);		
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();

	}
	
	public void onInvestmentStockResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investmentstock_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investmentstock_init_amount"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investmentstock_percontrib"));
		BigDecimal tax_rate = new BigDecimal(data.getStringExtra("investmentstock_tax_rate"));
		BigDecimal dividends = new BigDecimal(data.getStringExtra("investmentstock_dividends"));
		BigDecimal appreciation = new BigDecimal(data.getStringExtra("investmentstock_appreciation"));
		
    	if (requestCode == AcctElements.UPDATE.getNumber()) {
    		int investment_id = data.getIntExtra("investmentstock_id", -1);    		
    		InvestmentStock investment = (InvestmentStock) account.getInvestmentById(investment_id); 
    		account.removeInvestment(investment);
    		Log.d("Main.onInvestmentstockResult()", "removed Investment No. "+investment_id);
      	}
    	
    	InvestmentStock investment = new InvestmentStock(name,
    			init_amount,
                percontrib,
                tax_rate,
                dividends,
                appreciation);
        account.addInvestment(investment);		
        Toast.makeText(getSherlockActivity(), "Use top CHART or TABLE icons to see results.", Toast.LENGTH_SHORT).show();

	}
	
    private void updateInputListing(Iterable<? extends NamedValue> values) {        
        InputListingUpdater modifier = new  InputListingUpdater(getSherlockActivity());
        for(NamedValue value : values) {        	
        	value.updateInputListing(modifier);        	
        } 		   		       
    }
    
}
