package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.Money;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;

public class AddInvestmentStock extends SherlockActivity {

	private Account account;	
	private History history;
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
	        final InvestmentStock investment = (InvestmentStock) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddInvestmentStock.this)
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
		setContentView(R.layout.act_add_investmentstock);
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
	    	InvestmentStock investment = (InvestmentStock) account.getInvestmentById(id); // TODO: if id == -1
	    	
	    	investmentId = investment.getId();	    
	    	
			EditText name = (EditText) findViewById(R.id.investmentstock_name);
			name.setText(investment.getName().toString(), TextView.BufferType.EDITABLE);
	    	
	    	EditText amount = (EditText) findViewById(R.id.investmentstock_init_amount);
	    	amount.setText(investment.getInitAmount().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText percontrib = (EditText) findViewById(R.id.investmentstock_percontrib);
			percontrib.setText(investment.getPercontrib().setScale(2, Money.ROUNDING_MODE).toString(), TextView.BufferType.EDITABLE);
	    	
			EditText tax = (EditText) findViewById(R.id.investmentstock_tax_rate);
			tax.setText(investment.getTaxRate().setScale(2, Money.ROUNDING_MODE).toString(), TextView.BufferType.EDITABLE);
	    	
			EditText dividend = (EditText) findViewById(R.id.investmentstock_dividends);
			dividend.setText(String.valueOf(investment.getDividends().setScale(2, Money.ROUNDING_MODE)), TextView.BufferType.EDITABLE);			
			
			EditText appreciation = (EditText) findViewById(R.id.investmentstock_appreciation);
			appreciation.setText(investment.getAppreciation().setScale(2, Money.ROUNDING_MODE).toString(), TextView.BufferType.EDITABLE);
					
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
	
	    EditText investmentName = (EditText) findViewById(R.id.investmentstock_name);
	    String investmentNameData = investmentName.getText().toString();
        intent.putExtra("investmentstock_name", investmentNameData);  

		EditText investmentAmount = (EditText) findViewById(R.id.investmentstock_init_amount);
		String investmentAmountData = investmentAmount.getText().toString();
	    intent.putExtra("investmentstock_init_amount", investmentAmountData);
        		
	    EditText percontrib = (EditText) findViewById(R.id.investmentstock_percontrib);
	    String percontribData = percontrib.getText().toString();
        intent.putExtra("investmentstock_percontrib", percontribData);  
	    
	    EditText investmentTaxRate = (EditText) findViewById(R.id.investmentstock_tax_rate);
		String investmentTaxRateData = investmentTaxRate.getText().toString();
	    intent.putExtra("investmentstock_tax_rate", investmentTaxRateData);
		
	    EditText dividend = (EditText) findViewById(R.id.investmentstock_dividends);
	    String dividendData = dividend.getText().toString();
        intent.putExtra("investmentstock_dividends", dividendData);  	
			    	           
		EditText appreciation = (EditText) findViewById(R.id.investmentstock_appreciation);
		String appreciationData = appreciation.getText().toString();
	    intent.putExtra("investmentstock_appreciation", appreciationData);	  
	    
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddInvestment.addInvestment() requestCode", requestCode);
	        intent.putExtra("investmentstock_id", investmentId);
	    }
        
        setResult(AcctElements.INVESTMENTSTOCK.getNumber(), intent);
        finish();	
    }	
    	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.add_investmentstock, menu);
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
			dialog.setContentView(R.layout.help_investmentstock);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}

}
