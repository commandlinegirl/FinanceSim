package com.codelemma.finances;

import java.math.BigDecimal;

import android.app.AlertDialog;
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
import com.codelemma.finances.accounting.Debt;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class FrgDebt extends SherlockFragment {
	
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
	            Log.d("FrgDebt.onClick()", value.getName());
	        } else {
	        	Log.d("FrgDebt.updateAcctElementListing()", "value is null");
	        }
	        value.launchModifyUi(modifyUiLauncher);	        
	    }
	};
	
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
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
		modifyUiLauncher = new ModifyUiLauncher(getActivity());
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgDebt.onStart()", "called");	
		
		if (account.getDebtsSize() > 0) {	 
		   	Iterable<? extends NamedValue> values = account.getDebts();
		   	updateAcctElementListing(values, R.id.debt_summary);		    
		} else {
	    	LinearLayout tip = (LinearLayout) getActivity().findViewById(R.id.debt_summary);
	        tip.removeAllViews();
	    }
	}
	
	
	
	public void add(View view) {
		Intent intent = new Intent(getActivity(), AddDebt.class);		
		intent.putExtra("request", AcctElements.ADD.toString());
	    startActivityForResult(intent, AcctElements.ADD.getNumber());
	}
	
	private void showAlertDialog() {
    	new AlertDialog.Builder(getActivity()).setTitle("Not a number")
        .setMessage("Please, fill in field with a number")
        .setNeutralButton("Close", null)
        .show();		
	}

	
	public void onDebtResult(Intent data, int requestCode) {		
    	String debt_name = data.getStringExtra("debt_name");
    	BigDecimal debt_amount = new BigDecimal(data.getStringExtra("debt_amount"));
    	
		if (requestCode == AcctElements.UPDATE.getNumber()) {
      	
		    int debt_id = data.getIntExtra("debt_id", -1);    		
		    Debt debt = account.getDebtById(debt_id); 
		    account.removeDebt(debt);
		    Log.d("FrgDebt.onDebtResult()", "removed Debt No. "+debt_id);
		}
		
		Debt debt = new Debt(debt_name, 
				             debt_amount);
	    account.addDebt(debt);	   		
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
            Log.d("FrgDebt.updateAcctElementListing()", value.getName());
                                                         
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
