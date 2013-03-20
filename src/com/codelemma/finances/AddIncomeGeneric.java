package com.codelemma.finances;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.IncomeGeneric;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddIncomeGeneric extends SherlockActivity
                       implements OnItemSelectedListener{
	
	private Account account;
	private History history;
	private String requestCode;
	private int incomeId;
	private Finances appState;	
	private int[] installments_items = {12, 13}; // in months
	private int installments;
	
    private OnClickListener clickCancelListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        finish();	             	        
	    }
    };	
	
    private OnClickListener clickDeleteListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        final IncomeGeneric income = (IncomeGeneric) v.getTag(R.string.acct_object);
	        		   	
	        new AlertDialog.Builder(AddIncomeGeneric.this)
	            .setTitle("Delete")
                .setMessage("Do you want to delete this item?")                
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   account.removeIncome(income); 
                	   history.removeIncomeHistory(income.getHistory());
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
	    	Log.d("saving income", "Saving ");
	        addIncome(null);	             	        
	    }
    };	
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	installments = installments_items[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
    	parent.setSelection(1);
    }

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_income);
		// Show the Up button in the action bar.
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddIncome.onCreate()", "called");
		appState = Finances.getInstance();
	    account = appState.getAccount();    
	    history = appState.getHistory();
	    
	    Spinner spinner = (Spinner) findViewById(R.id.income_installments);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	         R.array.installments_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(0);
	    spinner.setOnItemSelectedListener(this);
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("income_id", -1);
	    	IncomeGeneric income = (IncomeGeneric) account.getIncomeById(id); // TODO: if id == -1
	    	
	    	incomeId = income.getId();	    

	    	EditText yearly_income = (EditText) findViewById(R.id.yearly_income);
		    yearly_income.setText(income.getValue().toString(), TextView.BufferType.EDITABLE);
		    	     
			EditText incomeRise = (EditText) findViewById(R.id.yearly_income_rise);		
			incomeRise.setText(income.getInitRiseRate().toString(), TextView.BufferType.EDITABLE);
			
			EditText incomeTaxRate = (EditText) findViewById(R.id.income_tax_rate);
			incomeTaxRate.setText(income.getInitTaxRate().toString(), TextView.BufferType.EDITABLE);
						
			if (income.getInitInstallments().doubleValue() == 13) {			
			    spinner.setSelection(1);
			} else {
				spinner.setSelection(0);
			}
			
			EditText incomeName = (EditText) findViewById(R.id.income_name);
			incomeName.setText(income.getName().toString(), TextView.BufferType.EDITABLE);
	    
	        // - add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitIncomeButtons);
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
            delete.setTag(R.string.acct_object, income);
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
    
	@Override
	protected void onStart() {
		super.onStart();
		Log.d("AddIncome.onStart()", "called");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("AddIncome.onRestart()", "called");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("AddIncome.onResume()", "called");
	}	
	
	@Override
	protected void onPause() {
		// Here save Application state
		super.onPause();
		Log.d("AddIncome.onPause()", "called");	
	}	
	
	@Override
	protected void onStop() {
		// Here save Application state
		super.onStop();
		Log.d("AddIncome.onStop()", "called");
	}		
	
	@Override
	protected void onDestroy() {
		// Here save Application state
		super.onStop();
		Log.d("AddIncome.onDestroyed()", "called");
	}
	
	public void cancelAdding(View view) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.add_incomegeneric, menu);
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
			dialog.setContentView(R.layout.help_incomegeneric);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void addIncome(View view) {
		Intent intent = new Intent(this, Main.class);				
		
	    EditText incomeName = (EditText) findViewById(R.id.income_name);
	    String incomeNameData = incomeName.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeNameData, getResources().getString(R.string.income_name_input))) {
	    	return;	    	
	    }
        intent.putExtra("income_name", incomeNameData);                
        				
        EditText income = (EditText) findViewById(R.id.yearly_income);
	    String incomeData = income.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeData, getResources().getString(R.string.yearly_income_input))) {
	    	return;
	    }
        intent.putExtra("yearly_income", incomeData);
            
	    EditText incomeRise = (EditText) findViewById(R.id.yearly_income_rise);
	    String incomeRiseData = incomeRise.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeRiseData, getResources().getString(R.string.yearly_income_rise_input))) {
	    	return;	    	
	    }	    
        intent.putExtra("yearly_income_rise", incomeRiseData);

	    EditText incomeTaxRate = (EditText) findViewById(R.id.income_tax_rate);
	    String incomeTaxRateData = incomeTaxRate.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeTaxRateData, getResources().getString(R.string.income_tax_rate_input))) {
	    	return;	    	
	    }	    
        intent.putExtra("income_tax_rate", incomeTaxRateData);

        intent.putExtra("income_installments", String.valueOf(installments));        

 	    //TODO: verify if text is alphanumeric
	    //InputValidator.validateName(incomeNameData); 
	    
    	if (requestCode.equals(AcctElements.UPDATE.toString())) {
	        intent.putExtra("income_id", incomeId);
	    }
	                                    
        setResult(AcctElements.INCOME.getNumber(), intent);
        finish();
	}
	

}
