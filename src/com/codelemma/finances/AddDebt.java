package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.Debt;

import android.app.AlertDialog;
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

public class AddDebt extends SherlockActivity {

	private Account account;
	private String requestCode;
	private int debtId;
	private Finances appState;	
	
    private OnClickListener clickDeleteListener = new OnClickListener() {
    	
    	@Override
	    public void onClick(View v) {
	        final Debt debt = (Debt) v.getTag(R.string.acct_object);
	        new AlertDialog.Builder(AddDebt.this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   account.removeDebt(debt); 
            	   appState.needToRecalculate(true);
            	   finish();
               }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   // User cancelled the dialog
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_debt);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("AddDebt.onCreate()", "called");
		appState = Finances.getInstance();
		account = appState.getAccount();
	    
	    Intent intent = getIntent(); //TODO: check if there are 
	    requestCode = intent.getStringExtra("request");
	    
	    if (requestCode.equals(AcctElements.UPDATE.toString())) {
	    	
	    	int id = intent.getIntExtra("debt_id", -1);
	    	Debt debt = account.getDebtById(id); // TODO: if id == -1
	    	
	    	debtId = debt.getId();	    

	    	EditText debt_amount = (EditText) findViewById(R.id.debt_amount);
		    debt_amount.setText(debt.getValue().toString(), TextView.BufferType.EDITABLE);
		    	     						
			EditText debtName = (EditText) findViewById(R.id.debt_name);
			debtName.setText(debt.getName().toString(), TextView.BufferType.EDITABLE);
	    
	        // - add Save & Delete button view
			
			LinearLayout buttons = (LinearLayout) findViewById(R.id.submitDebtButtons);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
					                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 0.5f;		
			
			buttons.removeAllViews();
            int px = Utils.px(this, 5); 			
			
			Button delete = new Button(this);
            delete.setText("Delete");
            params.setMargins(0, 0, px, 0);                                    
            delete.setLayoutParams(params);
            delete.setTag(R.string.acct_object, debt);
            delete.setOnClickListener(clickDeleteListener);
            delete.setBackgroundResource(R.drawable.button_cancel);            
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
		
	    EditText debtName = (EditText) findViewById(R.id.debt_name);
	    String debtNameData = debtName.getText().toString();
        intent.putExtra("debt_name", debtNameData);        

	    EditText debtAmount = (EditText) findViewById(R.id.debt_amount);
	    String debtAmountData = debtAmount.getText().toString();
        intent.putExtra("debt_amount", debtAmountData);   
           
        if (requestCode.equals(AcctElements.UPDATE.toString())) {
    		Log.d("AddDebt.addDebt() requestCode", requestCode);
	        intent.putExtra("debt_id", debtId);
	    }
        
        setResult(AcctElements.DEBT.getNumber(), intent);
        finish();
	}
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_debt, menu);
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