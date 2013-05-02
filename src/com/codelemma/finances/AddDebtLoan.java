package com.codelemma.finances;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.DebtLoan;

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

public class AddDebtLoan extends SherlockFragmentActivity 
                         implements FrgDatePicker.OnDateSelectedListener {

	private Finances appState;	
	private String requestCode;
	private int debtId;
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
	        final DebtLoan debt = (DebtLoan) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddDebtLoan.this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   appState.getAccount().removeDebt(debt); 
            	   appState.getHistory().removeDebtHistory(debt.getHistory());
            	   appState.needToRecalculate(true);
                   Toast.makeText(AddDebtLoan.this, debt.getName()+" deleted.", Toast.LENGTH_SHORT).show();
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
		TextView edit = (TextView) findViewById(R.id.debtloan_start_date);
		edit.setText((month+1)+"/"+year);		
		setYear = year;
		setMonth = month;
	}
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_debtloan);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddDebtLoan.onCreate()", "called");
		appState = Finances.getInstance();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    TextView start_date = (TextView) findViewById(R.id.debtloan_start_date);
        final Calendar c = Calendar.getInstance();
        setYear = c.get(Calendar.YEAR);
        setMonth = c.get(Calendar.MONTH);
		start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("debt_id", -1);
	    	DebtLoan debt = (DebtLoan) appState.getAccount().getDebtById(id); // TODO: if id == -1
	    	
	    	debtId = debt.getId();	    
	    	Log.d("supposed to be MORTGAGE ID", String.valueOf(id));
	    	
			EditText debtName = (EditText) findViewById(R.id.debtloan_name);
			debtName.setText(debt.getName().toString(), TextView.BufferType.EDITABLE);
	    	
	    	EditText debt_amount = (EditText) findViewById(R.id.debtloan_amount);
		    debt_amount.setText(debt.getValue().toString(), TextView.BufferType.EDITABLE);
		    	     						
			EditText loan_interest_rate = (EditText) findViewById(R.id.debtloan_interest_rate);
			loan_interest_rate.setText(debt.getInterestRate().toString(), TextView.BufferType.EDITABLE);

			EditText loan_term = (EditText) findViewById(R.id.debtloan_term);
			loan_term.setText(String.valueOf(debt.getTerm()), TextView.BufferType.EDITABLE);

			EditText loan_extra_payment = (EditText) findViewById(R.id.debtloan_extra_payment);
			loan_extra_payment.setText(debt.getExtraPayment().toString(), TextView.BufferType.EDITABLE);
			
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
		
	    EditText debtName = (EditText) findViewById(R.id.debtloan_name);
	    String debtNameData = debtName.getText().toString();
	    if (Utils.alertIfEmpty(this, debtNameData, getResources().getString(R.string.debtloan_name_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtloan_name", debtNameData);        

	    EditText debtAmount = (EditText) findViewById(R.id.debtloan_amount);
	    String debtAmountData = debtAmount.getText().toString();
	    if (Utils.alertIfEmpty(this, debtAmountData, getResources().getString(R.string.debtloan_amount_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtloan_amount", debtAmountData);   

	    EditText interestRate = (EditText) findViewById(R.id.debtloan_interest_rate);
	    String interestRateData = interestRate.getText().toString();
	    if (Utils.alertIfEmpty(this, interestRateData, getResources().getString(R.string.debtloan_interest_rate_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtloan_interest_rate", interestRateData);  
        
	    EditText term = (EditText) findViewById(R.id.debtloan_term);
	    String termData = term.getText().toString();
	    if (Utils.alertIfEmpty(this, termData, getResources().getString(R.string.debtloan_term_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtloan_term", termData);  
        
	    EditText extra = (EditText) findViewById(R.id.debtloan_extra_payment);
	    String extraData = extra.getText().toString();
	    if (Utils.alertIfEmpty(this, extraData, getResources().getString(R.string.debtloan_extra_payment_input))) {
	    	return;	    	
	    }	
        intent.putExtra("debtloan_extra_payment", extraData);  
        
        Log.d("DebtLoan setYear", String.valueOf(setYear));
        Log.d("DebtLoan setMonth", String.valueOf(setMonth));
        
        intent.putExtra("debtloan_start_year",  String.valueOf(setYear));
        intent.putExtra("debtloan_start_month",  String.valueOf(setMonth));
        
        
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddDebt.addDebt() requestCode", requestCode);
	        intent.putExtra("debt_id", debtId);
	    }
        
        setResult(AcctElements.DEBTLOAN.getNumber(), intent);
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
			dialog.setContentView(R.layout.help_debtloan);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}
}