package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;


public class AddInvestmentBond extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_investmentbond);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.add_investmentbond, menu);
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
			dialog.setContentView(R.layout.help_investmentbond);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();			
			return true;					
		}	
		return super.onOptionsItemSelected(item);
	}

}
