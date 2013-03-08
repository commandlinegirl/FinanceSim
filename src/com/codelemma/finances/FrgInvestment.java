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
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class FrgInvestment extends SherlockFragment {
	
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
	            Log.d("FrgInvestment.onClick()", value.getName());
	        } else {
	        	Log.d("FrgInvestment.updateAcctElementListing()", "value is null");
	        }	        
	        value.launchModifyUi(modifyUiLauncher);	        
	    }
	};
	
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
		Finances appState = Finances.getInstance();
		history = appState.getHistory();
		modifyUiLauncher = new ModifyUiLauncher(getActivity());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgInvestment.onStart()", "called");
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
		
		if (account.getInvestmentsSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = (Iterable<? extends NamedValue>) account.getInvestments();
		   	updateAcctElementListing(values, R.id.investment_summary);		    
		} else {
	    	LinearLayout tip = (LinearLayout) getActivity().findViewById(R.id.investment_summary);
	        tip.removeAllViews();
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
                interest_rate);
        account.addInvestment(investment);			    		
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
                employer_match);
        account.addInvestment(investment);			    		
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
	}
	
	public void onInvestmentStockResult(Intent data, int requestCode) {
		String name = data.getStringExtra("investmentstock_name");
		BigDecimal init_amount = new BigDecimal(data.getStringExtra("investmentstock_init_amount"));
		BigDecimal percontrib = new BigDecimal(data.getStringExtra("investmentstock_percontrib"));
		BigDecimal tax_rate = new BigDecimal(data.getStringExtra("investmentstock_tax_rate"));
		BigDecimal dividend = new BigDecimal(data.getStringExtra("investmentstock_dividend"));
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
                dividend,
                appreciation);
        account.addInvestment(investment);			    		
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
            Log.d("FrgInvestment.updateAcctElementListing()", value.getName());
                                                         
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
