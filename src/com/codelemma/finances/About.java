package com.codelemma.finances;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.content.pm.ActivityInfo;

public class About extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActionBar actionbar = getSupportActionBar();		
		actionbar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);						
			return true;
		case R.id.menu_settings:
			Log.d("User pressed", "Settings");
			return true;
		case R.id.menu_about:
			Intent intent = new Intent(this, About.class);					
		    startActivity(intent);	
			return true;
		case R.id.menu_feedback:
			Intent intent2 = new Intent(this, Feedback.class);					
		    startActivity(intent2);	
			return true;			
		}							
		return super.onOptionsItemSelected(item);
	}

}
