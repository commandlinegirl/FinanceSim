package com.codelemma.finances;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.Finances;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.StorageFactory;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;


public class Main extends SherlockFragmentActivity 
                  implements TabListener,
                             FrgControls.OnControlSelectedListener {

	private Storage storage;
	private Account account;
	private History history;
    private Finances appState;
	private AcctElements currentElement;
	private int currentYrsPred = 60;
	private View selectedYrsView;
	private View toggleChart;
	private View toggleTable;
	private View toggleSummary;
	private int currentTab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Main.onCreate()", "called");
		setContentView(R.layout.main);		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupActionBar();
        
	    appState = Finances.getInstance();
        storage = StorageFactory.create(PreferenceManager.getDefaultSharedPreferences(this));	    
	                    
        setupDate();
	    
	    if (appState.getHistory() == null) {
		    appState.setHistory();
	    }
	    if (appState.getAccount() == null) {
	    	Log.d("Main.onCreate()", "appState.getAccount() is null");
	    	try {
				appState.setAccount(storage);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }
	    	    	    	    	    	   
	    account = appState.getAccount();
	    history = appState.getHistory();
       	toggleChart = (View) findViewById(R.id.ico_chart);       	
    	toggleTable = (View) findViewById(R.id.ico_table);
    	toggleSummary = (View) findViewById(R.id.ico_summary);
    	toggleSummary.setSelected(true);	    
	}



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		toggleSummary = (View) findViewById(R.id.ico_summary);
		if(tab.getPosition() == 0) {	
	        ft.replace(R.id.main_container, new FrgIncome(), AcctElements.INCOME.toString()); 	        
	        currentTab = 0;
	        currentElement = AcctElements.INCOME;
	        toggleControlSelection(toggleSummary);
        } else if(tab.getPosition() == 1){		
            ft.replace(R.id.main_container, new FrgExpense(), AcctElements.EXPENSE.toString());
            currentTab = 1;
            currentElement = AcctElements.EXPENSE;
            toggleControlSelection(toggleSummary);
        } else if(tab.getPosition() == 2){		
            ft.replace(R.id.main_container, new FrgInvestment(), AcctElements.INVESTMENT.toString());
            currentTab = 2;
            currentElement = AcctElements.INVESTMENT;
            toggleControlSelection(toggleSummary);
        } else if(tab.getPosition() == 3){		
            ft.replace(R.id.main_container, new FrgDebt(), AcctElements.DEBT.toString());
            currentTab = 3;
            currentElement = AcctElements.DEBT;
            toggleControlSelection(toggleSummary);
        }
	 }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("Main.onPause()", "called");
		if (isFinishing() == true) {
			Log.d("Main.onPause()", "saving when finishing");
			try {
		        storage.saveAccount(account);
			} 
			catch (ParseException pe) {
				pe.printStackTrace();
			}
		}		
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	

	
	private void setupActionBar() {
		ActionBar actionbar = getSupportActionBar();		
		actionbar.setDisplayHomeAsUpEnabled(true);				
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		 	    
  	    ActionBar.Tab tab0 = actionbar.newTab().setText("Income").setTabListener(this);
        actionbar.addTab(tab0);	
  	    ActionBar.Tab tab1 = actionbar.newTab().setText("Expenses").setTabListener(this);
        actionbar.addTab(tab1);	
  	    ActionBar.Tab tab2 = actionbar.newTab().setText("Investments").setTabListener(this);
        actionbar.addTab(tab2);	
  	    ActionBar.Tab tab3 = actionbar.newTab().setText("Debts").setTabListener(this);
        actionbar.addTab(tab3);		
	}
	
	private void setupDate() {
		if (appState.getMonth() == -1 || appState.getYear() == -1) {
	    	Calendar cal = Calendar.getInstance();
	        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.US);
	        int yearNum = Integer.parseInt(year.format(cal.getTime()));
	        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.US);
	        int monthNum = Integer.parseInt(month.format(cal.getTime()));
		    appState.setYear(yearNum);		        
		    appState.setMonth(monthNum);	    
	    }
	}
	
	public void addIncome(View view) {
		FrgIncome frgIncome = (FrgIncome) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgIncome.add(view);
	}
	
	public void addInvestment(View view) {
		FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgInvestment.add(view);
	}

	public void addInvestment401k(View view) {
		FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgInvestment.add(view);
	}
	
	public void addInvestmentBond(View view) {
		FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgInvestment.add(view);
	}
	
	public void addInvestmentStock(View view) {
		FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgInvestment.add(view);
	}
	
	public void addExpense(View view) {
		FrgExpense frgExpense = (FrgExpense) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgExpense.add(view);
	}
	
	public void addDebt(View view) {
		FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgDebt.add(view);
	}
	
	@Override	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);        
                         
        // user pressed Back button
        if (resultCode == RESULT_CANCELED) {        
            return;
        }        
        if (resultCode == AcctElements.INCOME.getNumber()) {      
        	FrgIncome frgIncome = (FrgIncome) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgIncome.onIncomeResult(data, requestCode);
        }        
        if (resultCode == AcctElements.INVESTMENTSAV.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentSavAcctResult(data, requestCode);
        }        
        if (resultCode == AcctElements.INVESTMENT401K.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestment401kResult(data, requestCode);
        }        
        if (resultCode == AcctElements.INVESTMENTBOND.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentBondResult(data, requestCode);
        }     
        if (resultCode == AcctElements.INVESTMENTSTOCK.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentStockResult(data, requestCode);
        }     
        if (resultCode == AcctElements.EXPENSE.getNumber()) {      
        	FrgExpense frgExpense = (FrgExpense) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgExpense.onExpenseResult(data, requestCode);
        }
        if (resultCode == AcctElements.DEBT.getNumber()) {      
        	FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgDebt.onDebtResult(data, requestCode);
        }
        appState.needToRecalculate(true);
	}	
	
	private void recalculate() {
		
		if (appState.needToRecalculate() == false) {
			return;
		}		
                                
        storage.clearHistory();
        //history.clear();
        account.setInitDebt();
        account.setInitExpense();
        account.setInitIncome();
        account.setInitInvestment();
                                   
        int month = appState.getMonth();
        int year = appState.getYear();                
        
        // new CalculationTask().execute(appState); 
                
        for (int i = 0; i < appState.getListSize(); i++) { 
        	account.advance(i, month); //TODO: this call should be done in BKG
            if (month == 11) {
                month = 0;
                year += 1;
            } else {
                month++;
            }            	                
        }
        account.addHistoriesToHistory(history);        
        storage.saveHistory(history);    
        appState.needToRecalculate(false);
	}
	
	public void selectFragment(View view, Fragment fragment) {				
			
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();			
		
		switch (view.getId()) {
		case R.id.ico_summary:
			ft.replace(R.id.main_container, fragment);
			ft.commit();
		    break;
		case R.id.ico_table:	
			recalculate(); //TODO: only recalc if data changed!
			ft.replace(R.id.main_container, new FrgExpandableList());
			ft.commit();
			break;
		case R.id.ico_chart:
			recalculate();//TODO: only recalc if data changed!
			ft.replace(R.id.main_container, new FrgChart());
			ft.commit();
			break;
		default:			
			break;
		}							
		
	}	
	
	public void onControlSelected(View view) {
		
		HashMap<Integer, Fragment> m = new HashMap<Integer, Fragment>();
		
		switch (currentTab) {
		case 0:			
			selectFragment(view, new FrgBalance());
			break;		
	    case 1:
	    	
			selectFragment(view, new FrgIncome());		
			break;
	    case 2:
	    	
			selectFragment(view, new FrgExpense());		
			break;
	    case 3:
			selectFragment(view, new FrgInvestment());		
			break;
	    case 4:
	    	
			selectFragment(view, new FrgDebt());		
			break;			
		}
	}
	
	public void pickControl(View view) {
		toggleControlSelection(view);
		FrgControls frgControls = (FrgControls) getSupportFragmentManager().findFragmentById(R.id.frg_controls);
		frgControls.selectControl(view); 			
	}
	
	public void pred1yrs(View view) {
		currentYrsPred = 1*12;	 
		selectedYrsView = view;
	    FrgChart FrgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    FrgChart.onPredYrsSelection(view, currentYrsPred, currentElement);
	}

	public void pred5yrs(View view) {
		currentYrsPred = 5*12;
		selectedYrsView = view;
	    FrgChart FrgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    FrgChart.onPredYrsSelection(view, currentYrsPred, currentElement);	    
	}

	public void pred10yrs(View view) {
		currentYrsPred = 10*12;	    
		selectedYrsView = view;
	    FrgChart FrgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    FrgChart.onPredYrsSelection(view, currentYrsPred, currentElement);	    
	}

	public void pred20yrs(View view) {
		currentYrsPred = 20*12;	    
		selectedYrsView = view;
	    FrgChart FrgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    FrgChart.onPredYrsSelection(view, currentYrsPred, currentElement);
	}

	public void pred30yrs(View view) {
		currentYrsPred = 30*12;
		selectedYrsView = view;
	    FrgChart FrgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    FrgChart.onPredYrsSelection(view, currentYrsPred, currentElement);
	}
	
	
	public String[] getDates(){
	    return history.getDates();
	}			

	public String[] getValues(){
	    return history.getDates();
	}	
	
	public int getCurrentYrsPred(){
	    return currentYrsPred;
	}
	
	public View getSelectedYrsView(){
	    return selectedYrsView;
	}
	
	public AcctElements getCurrentElement(){
	    return currentElement;
	}
	
	private void toggleControlSelection(View view) {
       	toggleChart = (View) findViewById(R.id.ico_chart);       	
    	toggleTable = (View) findViewById(R.id.ico_table);
    	toggleSummary = (View) findViewById(R.id.ico_summary);
        toggleChart.setSelected(false);
        toggleTable.setSelected(false);
        toggleSummary.setSelected(false);		        
	    view.setSelected(true);
	}	
}
