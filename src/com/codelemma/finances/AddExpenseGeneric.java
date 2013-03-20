package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Money;

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


public class AddExpenseGeneric extends SherlockActivity 
                        implements OnItemSelectedListener {

	private Account account;
	private History history;
	private String requestCode;
	private int expenseId;
	private Finances appState;
	private int[] frequency_items = {1, 3, 6, 12}; // in months
	int frequency = 1;
	
    private OnClickListener clickCancelListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        finish();	             	        
	    }
    };	
	
    private OnClickListener clickDeleteListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        final ExpenseGeneric expense = (ExpenseGeneric) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddExpenseGeneric.this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   account.removeExpense(expense);             	   
            	   history.removeExpenseHistory(expense.getHistory());            	   
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
	    	Log.d("saving expense", "Saving ");
	        addExpense(null);	             	        
	    }
    };
	
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	frequency = frequency_items[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
    	parent.setSelection(0);
    }

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_expense);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddExpense.onCreate()", "called");		
		appState = Finances.getInstance();
	    account = appState.getAccount();    
	    history = appState.getHistory();
	    
	    
	    Spinner spinner = (Spinner) findViewById(R.id.expense_frequency);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	         R.array.expense_frequency_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(0);
	    spinner.setOnItemSelectedListener(this);
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("expense_id", -1);
	    	ExpenseGeneric expense = (ExpenseGeneric) account.getExpenseById(id); // TODO: if id == -1
	    	
	    	expenseId = expense.getId();	    
		    	     									
			EditText expenseName = (EditText) findViewById(R.id.expense_name);
			expenseName.setText(expense.getName().toString(), TextView.BufferType.EDITABLE);
			
			EditText init_expense = (EditText) findViewById(R.id.init_expense);
			init_expense.setText(expense.getValue().toString(), TextView.BufferType.EDITABLE);
			
	    	EditText inflation_rate = (EditText) findViewById(R.id.inflation_rate);
	    	inflation_rate.setText(expense.getInitInflationRate().toString(), TextView.BufferType.EDITABLE);

			int index = Utils.getIndex(frequency_items, expense.getFrequency());
			if (index != -1) {
			    spinner.setSelection(index);
			}			
	    
	        // - add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitExpenseButtons);
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
            delete.setTag(R.string.acct_object, expense);
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
    
	
	
	public void addExpense(View view) {
		Intent intent = new Intent(this, Main.class);			

	    EditText expenseName = (EditText) findViewById(R.id.expense_name);
	    String expenseNameData = expenseName.getText().toString();
        intent.putExtra("expense_name", expenseNameData);  	
		
		EditText init_expense = (EditText) findViewById(R.id.init_expense);
		String init_expenseData = init_expense.getText().toString();
	    intent.putExtra("init_expense", init_expenseData);	  
        
		EditText inflationRate = (EditText) findViewById(R.id.inflation_rate);
		String inflationRateData = inflationRate.getText().toString();
	    intent.putExtra("inflation_rate", inflationRateData);
	    
	    intent.putExtra("expense_frequency", String.valueOf(frequency));
		         
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddExpense.addExpense() requestCode", requestCode);
	        intent.putExtra("expense_id", expenseId);
	    }
        
        setResult(AcctElements.EXPENSE.getNumber(), intent);
        finish();
    }		

	public void cancelAdding(View view) {
		finish();
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_expense, menu);
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
