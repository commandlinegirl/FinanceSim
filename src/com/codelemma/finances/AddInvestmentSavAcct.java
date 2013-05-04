package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.InvestmentSavAcct;

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

public class AddInvestmentSavAcct extends SherlockFragmentActivity 
                                  implements OnItemSelectedListener, FrgDatePicker.OnDateSelectedListener {

	private Finances appState;
	private String requestCode;
	private int investmentId;
    private int[] capitalization_items = {1, 3, 6, 12, 24}; 
    private int capitalization = 1;
	private int setMonth;
	private int setYear;
	private BigDecimal currentPercontrib;
	
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
            	   appState.getAccount().removeInvestment(investment);             	   
       	    	   /* Remove specified percent of excess money from current total percent of excess money */
            	   appState.getAccount().subtractFromInvestmentsPercontrib(investment.getPercontrib());
            	   appState.getAccount().setCheckingAcctPercontrib();
            	   appState.getHistory().removeInvestmentHistory(investment.getHistory());
            	   appState.needToRecalculate(true);
                   Toast.makeText(AddInvestmentSavAcct.this, investment.getName()+" deleted.", Toast.LENGTH_SHORT).show();
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
	
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new FrgDatePicker();
        Bundle b = new Bundle();
        b.putInt("setMonth", setMonth);
        b.putInt("setYear", setYear);        
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		TextView edit = (TextView) findViewById(R.id.investmentsav_start_date);
		edit.setText((month+1)+"/"+year);	
		setYear = year;
		setMonth = month;
	}
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	capitalization = capitalization_items[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
    	parent.setSelection(1);
    }

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_investmentsavacct);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddInvestmentSavAcct.onCreate()", "called");
		appState = Finances.getInstance();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    Spinner spinner = (Spinner) findViewById(R.id.investmentsav_capitalization);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	         R.array.compounding_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(0);
	    spinner.setOnItemSelectedListener(this);
	    
	    TextView start_date = (TextView) findViewById(R.id.investmentsav_start_date);
        final Calendar c = Calendar.getInstance();
        setYear = c.get(Calendar.YEAR);
        setMonth = c.get(Calendar.MONTH);
		start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
		
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("investment_id", -1);
	    	InvestmentSavAcct investment = (InvestmentSavAcct) appState.getAccount().getInvestmentById(id); // TODO: if id == -1
	    	
	    	investmentId = investment.getId();	    
	    	
			EditText name = (EditText) findViewById(R.id.investmentsav_name);
			name.setText(investment.getName().toString(), TextView.BufferType.EDITABLE);
	    	
	    	EditText amount = (EditText) findViewById(R.id.investmentsav_init_amount);
	    	amount.setText(investment.getInitAmount().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText tax = (EditText) findViewById(R.id.investmentsav_tax_rate);
			tax.setText(investment.getInitTaxRate().toString(), TextView.BufferType.EDITABLE);
	    	
			EditText percontrib = (EditText) findViewById(R.id.investmentsav_percontrib);
			percontrib.setText(investment.getInitPercontrib().toString(), TextView.BufferType.EDITABLE);
			currentPercontrib = investment.getInitPercontrib();
			
			int capt_index = Utils.getIndex(capitalization_items, investment.getCapitalization());
			if (capt_index != -1) {
			    spinner.setSelection(capt_index);
			}
			
			EditText interest_rate = (EditText) findViewById(R.id.investmentsav_interest_rate);
			interest_rate.setText(investment.getInitInterestRate().toString(), TextView.BufferType.EDITABLE);
					
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
	
	    EditText investmentName = (EditText) findViewById(R.id.investmentsav_name);
	    String investmentNameData = investmentName.getText().toString();
	    if (Utils.alertIfEmpty(this, investmentNameData, getResources().getString(R.string.investmentsav_name_input))) {
	    	return;	    	
	    }	
        intent.putExtra("investmentsav_name", investmentNameData);  

		EditText investmentAmount = (EditText) findViewById(R.id.investmentsav_init_amount);
		String investmentAmountData = investmentAmount.getText().toString();
		if (Utils.alertIfEmpty(this, investmentAmountData, getResources().getString(R.string.investmentsav_init_amount_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investmentsav_init_amount", investmentAmountData);
        
	    EditText investmentTaxRate = (EditText) findViewById(R.id.investmentsav_tax_rate);
		String investmentTaxRateData = investmentTaxRate.getText().toString();
		if (Utils.alertIfEmpty(this, investmentTaxRateData, getResources().getString(R.string.investmentsav_tax_rate_input))) {
	    	return;	    	
	    }
	    if (Utils.alertIfNotInBounds(this, investmentTaxRateData, 0, 100, getResources().getString(R.string.investmentsav_tax_rate_input))) {
	    	return;	    	
	    }
	    intent.putExtra("investmentsav_tax_rate", investmentTaxRateData);
		
	    EditText percontrib = (EditText) findViewById(R.id.investmentsav_percontrib);
	    String percontribData = percontrib.getText().toString();
	    if (Utils.alertIfEmpty(this, percontribData, getResources().getString(R.string.investmentsav_percontrib_input))) {
	    	return;	    	
	    }
	    if (Utils.alertIfNotInBounds(this, percontribData, 0, 100, getResources().getString(R.string.investmentsav_percontrib_input))) {
	    	return;	    	
	    }
	    
	    /* If view == null, it means the data is being updated, not new added */
	    
	    BigDecimal perc = new BigDecimal(percontribData);
	    BigDecimal currentinvestmentsPercontrib = appState.getAccount().getInvestmentsPercontrib();
	    BigDecimal totalPercontribToCheck;
	    if (view == null) {
	    	totalPercontribToCheck = currentinvestmentsPercontrib.subtract(currentPercontrib).add(perc);
	    } else {
	    	totalPercontribToCheck = currentinvestmentsPercontrib.add(perc);
	    }
	    
	    if (totalPercontribToCheck.compareTo(new BigDecimal(100)) > 0) {
	    	
	        new AlertDialog.Builder(AddInvestmentSavAcct.this)
            .setTitle("Investment excess > 100%")
            .setMessage("Savings from more than 100%!")           // TODO: update this info     
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
            })
            .show();
	        return;
	    } else {
            intent.putExtra("investmentsav_percontrib", percontribData);  	
	    }

        intent.putExtra("investmentsav_capitalization", String.valueOf(capitalization));  	
        
		EditText investmentInterestRate = (EditText) findViewById(R.id.investmentsav_interest_rate);
		String investmentInterestRateData = investmentInterestRate.getText().toString();
		if (Utils.alertIfEmpty(this, investmentInterestRateData, getResources().getString(R.string.investmentsav_interest_rate_input))) {
	    	return;	    	
	    }	
	    intent.putExtra("investmentsav_interest_rate", investmentInterestRateData);	  
	    
        intent.putExtra("investmentsav_start_year",  String.valueOf(setYear));
        intent.putExtra("investmentsav_start_month",  String.valueOf(setMonth));
	    
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
		case R.id.menu_help:
			Dialog dialog = new Dialog(this, R.style.FullHeightDialog);			
			dialog.setContentView(R.layout.help_investmentsavacct);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}

}
