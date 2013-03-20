package com.codelemma.finances;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Investment401k;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AddInvestment401k extends SherlockActivity {

	private History history;
	private Account account;	
	private String requestCode;
	private int investmentId;
	private Finances appState;	
    

	
    private OnClickListener clickCancelListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        finish();	             	        
	    }
    };	
	
	private OnClickListener clickDeleteListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        final Investment401k investment = (Investment401k) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddInvestment401k.this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   account.removeInvestment(investment); 
            	   history.removeInvestmentHistory(investment.getHistory()); 
            	   appState.needToRecalculate(true);
            	   finish();
               }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   finish();
               }
           })
          .show();	        	         
	    }
    };	
    
    private OnClickListener clickSaveListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	    	Log.d("saving investment", "Saving ");
	        addInvestment(null);	             	        
	    }
    };	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_investment401k);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddInvestment.onCreate()", "called");
		appState = Finances.getInstance();
	    account = appState.getAccount();  		
	    history = appState.getHistory();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("investment_id", -1);
	    	Investment401k investment = (Investment401k) account.getInvestmentById(id); // TODO: if id == -1
	    	
	    	investmentId = investment.getId();	    
	    	
			EditText name = (EditText) findViewById(R.id.investment401k_name);
			name.setText(investment.getName().toString(), TextView.BufferType.EDITABLE);
							    	
	    	EditText init_amount = (EditText) findViewById(R.id.investment401k_init_amount);
	    	init_amount.setText(investment.getInitAmount().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText percontrib = (EditText) findViewById(R.id.investment401k_percontrib);
			percontrib.setText(investment.getInitPercontrib().toString(), TextView.BufferType.EDITABLE);
			
			EditText period = (EditText) findViewById(R.id.investment401k_period);
			period.setText(String.valueOf(investment.getPeriod()), TextView.BufferType.EDITABLE);
			
			EditText interestRate = (EditText) findViewById(R.id.investment401k_interest_rate);
			interestRate.setText(investment.getInitInterestRate().toString(), TextView.BufferType.EDITABLE);

			EditText salary = (EditText) findViewById(R.id.investment401k_salary);
			salary.setText(investment.getInitSalary().toString(), TextView.BufferType.EDITABLE);
			
			EditText payrise = (EditText) findViewById(R.id.investment401k_payrise);
			payrise.setText(investment.getInitPayrise().toString(), TextView.BufferType.EDITABLE);
			
			EditText withdrawal_tax_rate = (EditText) findViewById(R.id.investment401k_withdrawal_tax_rate);
			withdrawal_tax_rate.setText(investment.getInitWithdrawalTaxRate().toString(), TextView.BufferType.EDITABLE);
						
			EditText employer_match = (EditText) findViewById(R.id.investment401k_employer_match);
			employer_match.setText(investment.getInitEmployerMatch().toString(), TextView.BufferType.EDITABLE);
						
	        // Add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitInvestmentButtons);
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
            
			Button delete = new Button(this);
            delete.setText("Delete");
            params.setMargins(0, 0, px, 0);
            delete.setLayoutParams(params);
            delete.setTag(R.string.acct_object, investment);
            delete.setOnClickListener(clickDeleteListener);
            delete.setBackgroundResource(R.drawable.button_delete);            
            buttons.addView(delete);
            
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

	    EditText name = (EditText) findViewById(R.id.investment401k_name);
	    String nameData = name.getText().toString();
	    if (Utils.alertIfEmpty(this, nameData, getResources().getString(R.string.investment401k_name_input))) {
	    	return;	    	
	    }		    
        intent.putExtra("investment401k_name", nameData);  
        
		EditText init_amount = (EditText) findViewById(R.id.investment401k_init_amount);
		String init_amountData = init_amount.getText().toString();
	    if (Utils.alertIfEmpty(this, init_amountData, getResources().getString(R.string.investment401k_init_amount_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investment401k_init_amount", init_amountData);
   	
	    EditText percontrib = (EditText) findViewById(R.id.investment401k_percontrib);
		String percontribData = percontrib.getText().toString();
	    if (Utils.alertIfEmpty(this, percontribData, getResources().getString(R.string.investment401k_percontrib_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investment401k_percontrib", percontribData);		     
	    
	    EditText period = (EditText) findViewById(R.id.investment401k_period);
	    String withdrawalData = period.getText().toString();
	    if (Utils.alertIfEmpty(this, withdrawalData, getResources().getString(R.string.investment401k_period_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investment401k_period", withdrawalData); 
        
	    EditText interestRate = (EditText) findViewById(R.id.investment401k_interest_rate);
	    String interestRateData = interestRate.getText().toString();
	    if (Utils.alertIfEmpty(this, interestRateData, getResources().getString(R.string.investment401k_interest_rate_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investment401k_interest_rate", interestRateData);  
	    
	    EditText salary = (EditText) findViewById(R.id.investment401k_salary);
		String salaryData = salary.getText().toString();
	    if (Utils.alertIfEmpty(this, salaryData, getResources().getString(R.string.investment401k_salary_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investment401k_salary", salaryData);	  
	    
	    EditText payrise = (EditText) findViewById(R.id.investment401k_payrise);
		String payriseData = payrise.getText().toString();
	    if (Utils.alertIfEmpty(this, payriseData, getResources().getString(R.string.investment401k_payrise_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investment401k_payrise", payriseData);	
	    
	    EditText withdrawalTax = (EditText) findViewById(R.id.investment401k_withdrawal_tax_rate);
	    String withdrawalTaxData = withdrawalTax.getText().toString();
	    if (Utils.alertIfEmpty(this, withdrawalTaxData, getResources().getString(R.string.investment401k_withdrawal_tax_rate_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investment401k_withdrawal_tax_rate", withdrawalTaxData);  	
        
	    EditText match = (EditText) findViewById(R.id.investment401k_employer_match);
	    String matchData = match.getText().toString();
	    if (Utils.alertIfEmpty(this, matchData, getResources().getString(R.string.investment401k_employer_match_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investment401k_employer_match", matchData);  	
                
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("Addinvestment401k.addinvestment401k() requestCode", requestCode);
	        intent.putExtra("investment401k_id", investmentId);
	    }
        
        setResult(AcctElements.INVESTMENT401K.getNumber(), intent);
        finish();	
    }	


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_investment401k, menu);
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
			dialog.setContentView(R.layout.help_investment401k);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}						
		return super.onOptionsItemSelected(item);
	}

}
