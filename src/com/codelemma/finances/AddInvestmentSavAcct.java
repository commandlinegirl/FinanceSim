package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.InvestmentSavAcct;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddInvestmentSavAcct extends SherlockActivity 
                                  implements OnItemSelectedListener {

	private Account account;	
	private History history;
	private String requestCode;
	private int investmentId;
    private int[] capitalization_items = {1, 3, 6, 12, 24}; 
    private int capitalization = 1;
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
	        final InvestmentSavAcct investment = (InvestmentSavAcct) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddInvestmentSavAcct.this)
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
	
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	capitalization = capitalization_items[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    	parent.setSelection(1);
    }

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_investmentsavacct);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddInvestment.onCreate()", "called");
		appState = Finances.getInstance();
	    account = appState.getAccount();  		
	    history = appState.getHistory();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    Spinner spinner = (Spinner) findViewById(R.id.investmentsav_capitalization);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	         R.array.compounding_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(0);
	    spinner.setOnItemSelectedListener(this);
	    
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("investment_id", -1);
	    	InvestmentSavAcct investment = (InvestmentSavAcct) account.getInvestmentById(id); // TODO: if id == -1
	    	
	    	investmentId = investment.getId();	    
	    	
			EditText name = (EditText) findViewById(R.id.investmentsav_name);
			name.setText(investment.getName().toString(), TextView.BufferType.EDITABLE);
	    	
	    	EditText amount = (EditText) findViewById(R.id.investmentsav_init_amount);
	    	amount.setText(investment.getInitAmount().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText tax = (EditText) findViewById(R.id.investmentsav_tax_rate);
			tax.setText(investment.getInitTaxRate().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText percontrib = (EditText) findViewById(R.id.investmentsav_percontrib);
			percontrib.setText(investment.getInitPercontrib().toString(), TextView.BufferType.EDITABLE);
			
			int capt_index = Utils.getIndex(capitalization_items, investment.getCapitalization());
			if (capt_index != -1) {
			    spinner.setSelection(capt_index);
			}
			
			EditText interest_rate = (EditText) findViewById(R.id.investmentsav_interest_rate);
			interest_rate.setText(investment.getInitInterestRate().toString(), TextView.BufferType.EDITABLE);
					
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
	
	    EditText investmentName = (EditText) findViewById(R.id.investmentsav_name);
	    String investmentNameData = investmentName.getText().toString();
        intent.putExtra("investmentsav_name", investmentNameData);  

		EditText investmentAmount = (EditText) findViewById(R.id.investmentsav_init_amount);
		String investmentAmountData = investmentAmount.getText().toString();
	    intent.putExtra("investmentsav_init_amount", investmentAmountData);
        
	    EditText investmentTaxRate = (EditText) findViewById(R.id.investmentsav_tax_rate);
		String investmentTaxRateData = investmentTaxRate.getText().toString();
	    intent.putExtra("investmentsav_tax_rate", investmentTaxRateData);
		
	    EditText percontrib = (EditText) findViewById(R.id.investmentsav_percontrib);
	    String percontribData = percontrib.getText().toString();
        intent.putExtra("investmentsav_percontrib", percontribData);  	
			    	   
        intent.putExtra("investmentsav_capitalization", String.valueOf(capitalization));  	
        
		EditText investmentInterestRate = (EditText) findViewById(R.id.investmentsav_interest_rate);
		String investmentInterestRateData = investmentInterestRate.getText().toString();
	    intent.putExtra("investmentsav_interest_rate", investmentInterestRateData);	  
	    
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddInvestment.addInvestment() requestCode", requestCode);
	        intent.putExtra("investmentsav_id", investmentId);
	    }
        
        setResult(AcctElements.INVESTMENTSAV.getNumber(), intent);
        finish();	
    }	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_investmentsavacct, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
