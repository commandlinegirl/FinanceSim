package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.InvestmentCheckAcct;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddInvestmentCheckAcct extends SherlockFragmentActivity {

	private Finances appState;
	private String requestCode;
	private int investmentId;
	private int setMonth;
	private int setYear;
	
    private OnClickListener clickCancelListener = new OnClickListener() {    	
    	@Override
	    public void onClick(View v) {
	        finish();	             	        
	    }
    };	
		        
    private OnClickListener clickSaveListener = new OnClickListener() {    	
    	@Override
	    public void onClick(View v) {
	    	addInvestment(null);	             	        
	    }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_investmentcheckacct);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		appState = Finances.getInstance();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
				
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("investment_id", -1);
	    	InvestmentCheckAcct investment = (InvestmentCheckAcct) appState.getAccount().getInvestmentById(id); // TODO: if id == -1
	    		    	
	    	investmentId = investment.getId();	    
	    	
			EditText name = (EditText) findViewById(R.id.investmentcheck_name);
			name.setText(investment.getName().toString(), TextView.BufferType.EDITABLE);
	    	
	    	EditText amount = (EditText) findViewById(R.id.investmentcheck_init_amount);
	    	amount.setText(investment.getInitAmount().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText tax = (EditText) findViewById(R.id.investmentcheck_tax_rate);
			tax.setText(investment.getInitTaxRate().toString(), TextView.BufferType.EDITABLE);
			
			EditText interest_rate = (EditText) findViewById(R.id.investmentcheck_interest_rate);
			interest_rate.setText(investment.getInitInterestRate().toString(), TextView.BufferType.EDITABLE);
						
	        // Add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitInvestmentButtons);
			@SuppressWarnings("deprecation")
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
					                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 0.5f;
			
			buttons.removeAllViews();
			
            int px = Utils.px(this, 5); // convert from dp to pixels (since setMargins takes pixels)
			
            Button cancel = new Button(this);
            cancel.setText("Cancel");
            params.setMargins(px, 0, 0, 0);            
            cancel.setLayoutParams(params);
            cancel.setOnClickListener(clickCancelListener);
            cancel.setBackgroundResource(R.drawable.button_cancel);                      
			buttons.addView(cancel);
                        
            Button update = new Button(this);
            update.setText("Save");
            params.setMargins(px, 0, 0, 0);            
            update.setLayoutParams(params);
            update.setOnClickListener(clickSaveListener);
            update.setBackgroundResource(R.drawable.button_green);                      
			buttons.addView(update);									
	    }
	}
	
	public void cancelAdding(View view) {
		finish();
	}
	
	public void addInvestment(View view) {
		Intent intent = new Intent(this, Main.class);			
	
	    EditText investmentName = (EditText) findViewById(R.id.investmentcheck_name);
	    String investmentNameData = investmentName.getText().toString();
	    if (Utils.alertIfEmpty(this, investmentNameData, getResources().getString(R.string.investmentcheck_name_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investmentcheck_name", investmentNameData);  

		EditText investmentAmount = (EditText) findViewById(R.id.investmentcheck_init_amount);
		String investmentAmountData = investmentAmount.getText().toString();
		if (Utils.alertIfEmpty(this, investmentAmountData, getResources().getString(R.string.investmentcheck_init_amount_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investmentcheck_init_amount", investmentAmountData);
        
	    EditText investmentTaxRate = (EditText) findViewById(R.id.investmentcheck_tax_rate);
		String investmentTaxRateData = investmentTaxRate.getText().toString();
		if (Utils.alertIfEmpty(this, investmentTaxRateData, getResources().getString(R.string.investmentcheck_tax_rate_input))) {
	    	return;	    	
	    }
	    if (Utils.alertIfNotInBounds(this, investmentTaxRateData, 0, 100, getResources().getString(R.string.investmentcheck_tax_rate_input))) {
	    	return;	    	
	    }
	    intent.putExtra("investmentcheck_tax_rate", investmentTaxRateData);		
			    	           
		EditText investmentInterestRate = (EditText) findViewById(R.id.investmentcheck_interest_rate);
		String investmentInterestRateData = investmentInterestRate.getText().toString();
		if (Utils.alertIfEmpty(this, investmentInterestRateData, getResources().getString(R.string.investmentcheck_interest_rate_input))) {
	    	return;	    	
	    }
	    if (Utils.alertIfNotInBounds(this, investmentInterestRateData, 0, 100, getResources().getString(R.string.investmentcheck_interest_rate_input))) {
	    	return;	    	
	    }
	    intent.putExtra("investmentcheck_interest_rate", investmentInterestRateData);	  

        intent.putExtra("investmentcheck_start_year",  String.valueOf(setYear));
        intent.putExtra("investmentcheck_start_month",  String.valueOf(setMonth));
	    
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
	        intent.putExtra("investmentcheck_id", investmentId);
	    }
        
        setResult(AcctElements.INVESTMENTCHECKACCT.getNumber(), intent);
        finish();	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_investmentcheckacct, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_help:
			Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
		    dialog.setContentView(R.layout.help_investmentcheckacct);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}
}