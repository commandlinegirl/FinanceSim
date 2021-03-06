package com.codelemma.finances;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddIncomeGeneric extends SherlockFragmentActivity
                       implements FrgDatePicker.OnDateSelectedListener {
	
	private Finances appState;	
	private String requestCode;
	private int incomeId;
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
	        final IncomeGeneric income = (IncomeGeneric) v.getTag(R.string.acct_object);
	        
	        if (income.getInvestment401k() == null) {
	        	new AlertDialog.Builder(AddIncomeGeneric.this)
	            .setTitle("Delete")
                .setMessage("Do you want to delete this item?")                
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   appState.getAccount().removeIncome(income);
                	   appState.getHistory().removeIncomeHistory(income.getHistory());
                	   appState.needToRecalculate(true);
                       Toast.makeText(AddIncomeGeneric.this, income.getName()+" deleted.", Toast.LENGTH_SHORT).show();
                	   finish();
                   }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               })
              .show();
	        } else {
	        	new AlertDialog.Builder(AddIncomeGeneric.this)
	            .setTitle("Delete")
                .setMessage("This income is associated with a 401(k) account. To remove it, please " +
                		"remove or update the 401(k).")                
                .setNeutralButton("Take me to the 401(k) account", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Investment401k investment401k = income.getInvestment401k();               	   
                       Intent intent = new Intent(getApplicationContext(), AddInvestment401k.class);
                       intent.putExtra("investment_id", investment401k.getId());
                       intent.putExtra("request", AcctElements.UPDATE.toString());   
            	       startActivityForResult(intent, AcctElements.UPDATE.getNumber());
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
	    }
    };	
    
    private OnClickListener clickSaveListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
	        addIncome(null);	             	        
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
		TextView edit = (TextView) findViewById(R.id.incomegeneric_start_date);
		edit.setText((month+1)+"/"+year);	
		setYear = year;
		setMonth = month;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_incomegeneric);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		appState = Finances.getInstance();
	    
	    Intent intent = getIntent();
	    requestCode = intent.getStringExtra("request");
	    
	    TextView start_date = (TextView) findViewById(R.id.incomegeneric_start_date);
        Calendar c = Calendar.getInstance();
        setYear = c.get(Calendar.YEAR);
        setMonth = c.get(Calendar.MONTH);
		start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("income_id", -1);
	    	IncomeGeneric income = (IncomeGeneric) appState.getAccount().getIncomeById(id); // TODO: if id == -1
	    	incomeId = income.getId();	    
			
			EditText incomeName = (EditText) findViewById(R.id.income_name);
			incomeName.setText(income.getName().toString(), TextView.BufferType.EDITABLE);
			
	    	EditText yearly_income = (EditText) findViewById(R.id.yearly_income);
		    yearly_income.setText(income.getValue().toString(), TextView.BufferType.EDITABLE);
		    	     
			EditText incomeRise = (EditText) findViewById(R.id.yearly_income_rise);		
			incomeRise.setText(income.getInitRiseRate().toString(), TextView.BufferType.EDITABLE);
			
			EditText incomeTaxRate = (EditText) findViewById(R.id.income_tax_rate);
			incomeTaxRate.setText(income.getInitTaxRate().toString(), TextView.BufferType.EDITABLE);

			EditText incomeTerm = (EditText) findViewById(R.id.income_term);
			incomeTerm.setText(String.valueOf(income.getTerm()), TextView.BufferType.EDITABLE);
	    
			setYear = income.getStartYear();
			setMonth = income.getStartMonth() ;			
			start_date.setText((setMonth+1)+"/"+setYear, TextView.BufferType.EDITABLE);
			
	        // - add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitIncomeButtons);
			@SuppressWarnings("deprecation")
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
					                                                         LinearLayout.LayoutParams.WRAP_CONTENT); // warning: FILL_PARENT is deprecated
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
	
	public void cancelAdding(View view) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
	    if(Utils.alertIfEmpty(this, incomeRiseData, getResources().getString(R.string.yearly_income_rise_input))) {
	    	return;
	    }
	    if(Utils.alertIfNotInBounds(this, incomeRiseData, 0, 100, getResources().getString(R.string.yearly_income_rise_input))) {
	    	return;
	    }

        intent.putExtra("yearly_income_rise", incomeRiseData);

	    EditText incomeTaxRate = (EditText) findViewById(R.id.income_tax_rate);
	    String incomeTaxRateData = incomeTaxRate.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeTaxRateData, getResources().getString(R.string.income_tax_rate_input))) {
	    	return;
	    }
	    if(Utils.alertIfNotInBounds(this, incomeTaxRateData, 0, 100, getResources().getString(R.string.income_tax_rate_input))) {
	    	return;
	    }
        intent.putExtra("income_tax_rate", incomeTaxRateData);

        intent.putExtra("incomegeneric_start_year",  String.valueOf(setYear));
        intent.putExtra("incomegeneric_start_month",  String.valueOf(setMonth));

        
        
        
	    EditText incomeTerm = (EditText) findViewById(R.id.income_term);
	    String incomeTermData = incomeTerm.getText().toString();
	    if (Utils.alertIfEmpty(this, incomeTermData, getResources().getString(R.string.income_term_input))) {
	    	return;
	    }
	    if(Utils.alertIfIntNotInBounds(this, incomeTermData, 1, 100, getResources().getString(R.string.income_term_input))) {
	    	return;
	    }
        intent.putExtra("income_term", incomeTermData);

    	if (requestCode.equals(AcctElements.UPDATE.toString())) {
	        intent.putExtra("income_id", incomeId);
	        
	        // check that associated 401(k) must not start earlier than income
	        Income inc = appState.getAccount().getIncomeById(incomeId);
	        Investment401k inv = inc.getInvestment401k();
	        if (inv != null) {
	        	if (inv.getStartYear() < setYear || (inv.getStartYear() == setYear && inv.getStartMonth() < setMonth)) {
	        		alertIfWrongDate(inv.getStartYear(), inv.getStartMonth());
	        		return;
	        	}
	        }
    	}

        setResult(AcctElements.INCOME.getNumber(), intent);
        finish();
	}
	
	private void alertIfWrongDate(int year, int month) {
		new AlertDialog.Builder(this).setTitle("Incorrect date")
            .setMessage("The date of income start should not be later than the associated 401(k) account start date. Please, fill the date earlier or equal to " 
		           + String.valueOf(month+1)+"/"+String.valueOf(year)+".")
            .setNeutralButton("Close", null)
            .show();
	}
}