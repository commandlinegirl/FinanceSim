package com.codelemma.finances;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.History;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddDebtMortgage extends SherlockFragmentActivity 
                             implements FrgDatePicker.OnDateSelectedListener {
	
	private Account account;
	private History history;
	private String requestCode;
	private int debtId;
	private Finances appState;	
	private int setMonth;
	private int setYear;
	
    private OnClickListener clickCancelListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        finish();	             	        
	    }
    };	
	
    private OnClickListener clickDeleteListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        final DebtMortgage debt = (DebtMortgage) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddDebtMortgage.this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   account.removeDebt(debt); 
            	   history.removeDebtHistory(debt.getHistory());
            	   appState.needToRecalculate(true);
                   Toast.makeText(AddDebtMortgage.this, debt.getName()+" deleted.", Toast.LENGTH_SHORT).show();
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
	    	Log.d("saving debt", "Saving ");
	        addDebt(null);	             	        
	    }
    };
	
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new FrgDatePicker();
        Bundle b = new Bundle();
        b.putInt("setMonth", setMonth);
        b.putInt("setYear", setYear);        
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		TextView edit = (TextView) findViewById(R.id.debtmortgage_start_date);
		edit.setText((month+1)+"/"+year);	
		setYear = year;
		setMonth = month;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_debtmortgage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddDebt.onCreate()", "called");
		appState = Finances.getInstance();
		account = appState.getAccount();
		history = appState.getHistory();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    TextView start_date = (TextView) findViewById(R.id.debtmortgage_start_date);
        final Calendar c = Calendar.getInstance();
        setYear = c.get(Calendar.YEAR);
        setMonth = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
		start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("debt_id", -1);
	    	DebtMortgage debt = (DebtMortgage) account.getDebtById(id); // TODO: if id == -1
	    	Log.d("supposed to be MORTGAGE ID", String.valueOf(id));
	    	debtId = debt.getId();	    

	    	EditText debt_amount = (EditText) findViewById(R.id.debtmortgage_purchase_price);
		    debt_amount.setText(debt.getPurchasePrice().toString(), TextView.BufferType.EDITABLE);
		    	     						
			EditText debtName = (EditText) findViewById(R.id.debtmortgage_name);
			debtName.setText(debt.getName().toString(), TextView.BufferType.EDITABLE);

			EditText downpayment = (EditText) findViewById(R.id.debtmortgage_downpayment);
			downpayment.setText(debt.getDownpayment().toString(), TextView.BufferType.EDITABLE);
			
			EditText interestRate = (EditText) findViewById(R.id.debtmortgage_interest_rate);
			interestRate.setText(debt.getInterestRate().toString(), TextView.BufferType.EDITABLE);
			
			EditText term = (EditText) findViewById(R.id.debtmortgage_term);
			term.setText(String.valueOf(debt.getTerm()), TextView.BufferType.EDITABLE);
			
			EditText propertyInsurance = (EditText) findViewById(R.id.debtmortgage_property_insurance);
			propertyInsurance.setText(debt.getPropertyInsurance().toString(), TextView.BufferType.EDITABLE);
			
			EditText propertyTax = (EditText) findViewById(R.id.debtmortgage_property_tax);
			propertyTax.setText(debt.getPropertyTax().toString(), TextView.BufferType.EDITABLE);

			EditText pmi = (EditText) findViewById(R.id.debtmortgage_pmi);
			pmi.setText(debt.getPMI().toString(), TextView.BufferType.EDITABLE);
						
			setYear = debt.getStartYear();
			setMonth = debt.getStartMonth() ;			
			start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
			
	        // - add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitDebtButtons);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
					                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 0.5f;		
			
			buttons.removeAllViews();
            int px = Utils.px(this, 5); 			
			
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
            delete.setTag(R.string.acct_object, debt);
            delete.setOnClickListener(clickDeleteListener);
            delete.setBackgroundResource(R.drawable.button_delete);            
            buttons.addView(delete);
            
            Button update = new Button(this);
            update.setText("Save");
            params.setMargins(px, 0, 0, 0);                                    
            update.setLayoutParams(params);
            update.setOnClickListener(clickSaveListener);
			buttons.addView(update);						
            update.setBackgroundResource(R.drawable.button_green);          			
	    }
	}

	public void cancelAdding(View view) {
		finish();
	}
	
	public void addDebt(View view) {
		Intent intent = new Intent(this, Main.class);			
		
	    EditText debtName = (EditText) findViewById(R.id.debtmortgage_name);
	    String debtNameData = debtName.getText().toString();
	    if (Utils.alertIfEmpty(this, debtNameData, getResources().getString(R.string.debtmortgage_name_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_name", debtNameData);        

	    EditText debtAmount = (EditText) findViewById(R.id.debtmortgage_purchase_price);
	    String debtAmountData = debtAmount.getText().toString();
	    if (Utils.alertIfEmpty(this, debtAmountData, getResources().getString(R.string.debtmortgage_purchase_price_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_purchase_price", debtAmountData);   
        
	    EditText downpayment = (EditText) findViewById(R.id.debtmortgage_downpayment);
	    String downpaymentData = downpayment.getText().toString();
	    if (Utils.alertIfEmpty(this, downpaymentData, getResources().getString(R.string.debtmortgage_downpayment_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_downpayment", downpaymentData);   
        
	    EditText interestRate = (EditText) findViewById(R.id.debtmortgage_interest_rate);
	    String interestRateData = interestRate.getText().toString();
	    if (Utils.alertIfEmpty(this, interestRateData, getResources().getString(R.string.debtmortgage_interest_rate_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_interest_rate", interestRateData);   
        
	    EditText term = (EditText) findViewById(R.id.debtmortgage_term);
	    String termData = term.getText().toString();
	    if (Utils.alertIfEmpty(this, termData, getResources().getString(R.string.debtmortgage_term_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_term", termData);   
        
	    EditText propertyInsurance = (EditText) findViewById(R.id.debtmortgage_property_insurance);
	    String propertyInsuranceData = propertyInsurance.getText().toString();
	    if (Utils.alertIfEmpty(this, propertyInsuranceData, getResources().getString(R.string.debtmortgage_property_insurance_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_property_insurance", propertyInsuranceData);   
        
	    EditText propertyTax = (EditText) findViewById(R.id.debtmortgage_property_tax);
	    String propertyTaxData = propertyTax.getText().toString();
	    if (Utils.alertIfEmpty(this, propertyTaxData, getResources().getString(R.string.debtmortgage_property_tax_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_property_tax", propertyTaxData);   

	    EditText pmi = (EditText) findViewById(R.id.debtmortgage_pmi);
	    String pmiData = pmi.getText().toString();
	    if (Utils.alertIfEmpty(this, pmiData, getResources().getString(R.string.debtmortgage_pmi_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtmortgage_pmi", pmiData);  
        
        intent.putExtra("debtmortgage_start_year",  String.valueOf(setYear));
        intent.putExtra("debtmortgage_start_month",  String.valueOf(setMonth));
        
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddDebt.addDebt() requestCode", requestCode);
	        intent.putExtra("debt_id", debtId);
	    }
        
        setResult(AcctElements.DEBTMORTGAGE.getNumber(), intent);
        finish();
	}
		

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_debtloan, menu);
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
			dialog.setContentView(R.layout.help_debtmortgage);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}
}