package com.codelemma.finances;


import java.util.ArrayList;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.Investment401k;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddInvestment401k extends SherlockFragmentActivity 
                               implements FrgDatePicker.OnDateSelectedListener, OnItemSelectedListener {
	
	private History history;
	private Account account;	
	private String requestCode;
	private int investmentId;
	private Finances appState;	
	private int setMonth;
	private int setYear;
	private ArrayList<Income> salaries = new ArrayList<Income>();
	private Income salary;
	
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
           		   /* Before removing investment account, set the income with which it is associated
             		* to null.
            		*/
            	   investment.getIncome().setInvestment401k(null);
            	   account.removeInvestment(investment); 
            	   history.removeInvestmentHistory(investment.getHistory()); 
            	   appState.needToRecalculate(true);
                   Toast.makeText(AddInvestment401k.this, investment.getName()+" deleted.", Toast.LENGTH_SHORT).show();
            	   finish();
               }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		salary = salaries.get(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.setSelection(0);		
	}
    
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new FrgDatePicker();
        Bundle b = new Bundle();
        b.putInt("setMonth", setMonth);
        b.putInt("setYear", setYear);        
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		TextView edit = (TextView) findViewById(R.id.investment401k_start_date);
		edit.setText((month+1)+"/"+year);
		setYear = year;
		setMonth = month;
	}
    
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
	    	       
        for (Income i : account.getIncomes()) {
        	salaries.add(i);
        }
	    
        Spinner spinner = (Spinner) findViewById(R.id.investment401k_salary);	    
        ArrayAdapter<Income> adapter = new ArrayAdapter<Income>(this, android.R.layout.simple_spinner_item, salaries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0); 
        spinner.setOnItemSelectedListener(this);
	    
		TextView start_date = (TextView) findViewById(R.id.investment401k_start_date);
        final Calendar c = Calendar.getInstance();
        setYear = c.get(Calendar.YEAR);
        setMonth = c.get(Calendar.MONTH);
		start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);

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
			
			for (int i = 0; i < salaries.size(); i++) {
				Income inc = salaries.get(i);
				if (inc == investment.getIncome()) {
					spinner.setSelection(i);		
				}
			}
			
			EditText withdrawal_tax_rate = (EditText) findViewById(R.id.investment401k_withdrawal_tax_rate);
			withdrawal_tax_rate.setText(investment.getInitWithdrawalTaxRate().toString(), TextView.BufferType.EDITABLE);
						
			EditText employer_match = (EditText) findViewById(R.id.investment401k_employer_match);
			employer_match.setText(investment.getInitEmployerMatch().toString(), TextView.BufferType.EDITABLE);
								    
				setYear = investment.getStartYear();
				setMonth = investment.getStartMonth() ;			
				start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
				
			
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
       
	    intent.putExtra("investment401k_salary", salary.getValue().toString());	  
	    intent.putExtra("investment401k_payrise", salary.getInitRiseRate().toString());
	    intent.putExtra("investment401k_incomeid", String.valueOf(salary.getId()));
	    
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
                
        intent.putExtra("investment401k_start_year",  String.valueOf(setYear));
        intent.putExtra("investment401k_start_month",  String.valueOf(setMonth));
        
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
