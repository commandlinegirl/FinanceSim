package com.codelemma.finances;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

public class Main extends SherlockFragmentActivity 
                  implements TabListener, 
                             DirectToHomeListener.OnClickOnEmptyListener {

	private Storage storage;
	private Account account;
	private History history;
    private Finances appState;
	private int currentElement = 0;
	private int currentIcon = 0;
	private MenuItem menuChart;
	private MenuItem menuTable;
	private MenuItem menuAdd;	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Main.onCreate()", "called");
		setContentView(R.layout.main);		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    
        setupActionBar();
        
	    appState = Finances.getInstance();
        storage = StorageFactory.create(PreferenceManager.getDefaultSharedPreferences(this));	    
	                    
        setupCurrentDate();
	    
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
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {		
	}

	private static final ArrayList<Class<? extends SherlockFragment>> TAB_FRG_CLASSES = new ArrayList<Class<? extends SherlockFragment>>();
	private static final ArrayList<Class<? extends SherlockFragment>> DATA_FRG_CLASSES = new ArrayList<Class<? extends SherlockFragment>>();
			
	static {
		TAB_FRG_CLASSES.add(FrgIncome.class);
		TAB_FRG_CLASSES.add(FrgExpense.class);
		TAB_FRG_CLASSES.add(FrgInvestment.class);
		TAB_FRG_CLASSES.add(FrgDebt.class);
		TAB_FRG_CLASSES.add(FrgCashflow.class);
		TAB_FRG_CLASSES.add(FrgNetWorth.class);
		DATA_FRG_CLASSES.add(FrgChart.class);
		DATA_FRG_CLASSES.add(FrgList.class);
	}
	
	private SherlockFragment getFragment(int iconIndex, int tabIndex) {
		try {
    		if (iconIndex == 0) {
				return TAB_FRG_CLASSES.get(tabIndex).newInstance();
    		} else {
    			return DATA_FRG_CLASSES.get(iconIndex - 1).newInstance();
    		}
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		currentElement = tab.getPosition();
		ft.replace(R.id.main_container, getFragment(currentIcon, currentElement));		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		menuChart = menu.findItem(R.id.menu_chart);
		menuTable = menu.findItem(R.id.menu_table);
		menuAdd = menu.findItem(R.id.menu_add);
		menuAdd.setIcon(R.drawable.ico_add_on);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (item.getItemId()) {
		case R.id.menu_add:
            currentIcon = 0;    	
    	    ft.replace(R.id.main_container, getFragment(currentIcon, currentElement));
    	    item.setIcon(R.drawable.ico_add_on);
    	    menuChart.setIcon(R.drawable.ico_chart);
    	    menuTable.setIcon(R.drawable.ico_table);    	    
    	    ft.commit();    	    
			return true;
		case R.id.menu_chart:
            currentIcon = 1;
            recalculate();
    	    ft.replace(R.id.main_container, new FrgChart());     	    
    	    ft.commit();		
    	    item.setIcon(R.drawable.ico_chart_on);
    	    menuAdd.setIcon(R.drawable.ico_add);
    	    menuTable.setIcon(R.drawable.ico_table);    
			return true;
		case R.id.menu_table:
            currentIcon = 2;    
            recalculate();
    	    ft.replace(R.id.main_container, new FrgList()); 
    	    ft.commit();
    	    item.setIcon(R.drawable.ico_table_on);
    	    menuAdd.setIcon(R.drawable.ico_add);
    	    menuChart.setIcon(R.drawable.ico_chart);    
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
	

	
	private void setupActionBar() {
		ActionBar actionbar = getSupportActionBar();		
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(true);						
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		 	    
  	    ActionBar.Tab tab0 = actionbar.newTab().setText(getResources().getString(R.string.income_title)).setTabListener(this);
        actionbar.addTab(tab0);	
  	    ActionBar.Tab tab1 = actionbar.newTab().setText(getResources().getString(R.string.expenses_title)).setTabListener(this);
        actionbar.addTab(tab1);	
  	    ActionBar.Tab tab2 = actionbar.newTab().setText(getResources().getString(R.string.investments_title)).setTabListener(this);
        actionbar.addTab(tab2);	
  	    ActionBar.Tab tab3 = actionbar.newTab().setText(getResources().getString(R.string.debts_title)).setTabListener(this);
        actionbar.addTab(tab3);
  	    ActionBar.Tab tab4 = actionbar.newTab().setText(getResources().getString(R.string.cashflow_title)).setTabListener(this);
        actionbar.addTab(tab4);
  	    ActionBar.Tab tab5 = actionbar.newTab().setText(getResources().getString(R.string.net_worth_title)).setTabListener(this);
        actionbar.addTab(tab5);        
	}
	
	private void setupCurrentDate() {
		if (appState.getCalculationStartMonth() == -1 || appState.getCalculationStartYear() == -1) {
	        final Calendar c = Calendar.getInstance();	        
		    appState.setCalculationStartYear(c.get(Calendar.YEAR));		        
		    appState.setCalculationStartMonth(c.get(Calendar.MONTH));
		    appState.setSimulationStartYear(c.get(Calendar.YEAR));		        
		    appState.setSimulationStartMonth(c.get(Calendar.MONTH));	
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
        if (resultCode == AcctElements.DEBTLOAN.getNumber()) {      
        	FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgDebt.onDebtLoanResult(data, requestCode);
        }
        if (resultCode == AcctElements.DEBTMORTGAGE.getNumber()) {      
        	FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgDebt.onDebtMortgageResult(data, requestCode);
        }        
        appState.needToRecalculate(true);
	}	
	
	private void recalculate() {
		
		if (appState.needToRecalculate() == false) {
			return;
		}		
                                
        account.setInitDebt();
        account.setInitExpense();
        account.setInitIncome();
        account.setInitInvestment();
        account.setInitCashflow();
        account.setInitNetworth();
        account.clearHistory(history);
                                   
        int month = appState.getCalculationStartMonth();
        int year = appState.getCalculationStartYear();
        
        int simStartYear = appState.getSimulationStartYear();
        int simStartMonth = appState.getSimulationStartMonth();
        
        
        Log.d("appState.getCalculationStartMonth()", String.valueOf(month));
        Log.d("appState.getCalculationStartYear()", String.valueOf(year));
        Log.d("simStartYear", String.valueOf(simStartYear));
        Log.d("simStartMonth", String.valueOf(simStartMonth));
        
        appState.computeCalculationLength();
        
        
        int totalCalculationLength = appState.getTotalCalculationLength();
        int preCalculationLength = appState.getPreCalculationLength(); 
        
        
        Log.d("preCalculationLength", String.valueOf(preCalculationLength));
        Log.d("totalCalculationLength", String.valueOf(totalCalculationLength));
        Log.d("simStartYear", String.valueOf(simStartYear));
        Log.d("simStartMonth", String.valueOf(simStartMonth));

        
        int index = -1;
        for (int i = 0; i < totalCalculationLength; i++) {
        	if (i >= preCalculationLength) {    
                index++;
        	}           
        	account.advance(index, year, month); //TODO: dates - should be minimum dates
            if (month == 11) {
                month = 0;
                year += 1;
            } else {
                month++;
            }
            
            
            
        }
        
        account.addToHistory(history);
        appState.needToRecalculate(false);
	}		
		
	public void showChartForNYears(View view) {		
	    FrgChart frgChart = (FrgChart) getSupportFragmentManager().findFragmentById(R.id.main_container);
	    frgChart.onPredYrsSelection(view, currentElement);
	}
	
	public String[] getDates(){
	    return history.getDates();
	}			
	
	public int getCurrentElement(){
	    return currentElement;
	}


	@Override
	public void onClickOnEmptySelected(View view, int element) {
		/* Called when used clicks on CHART or TABLE icons while there is no input data
		 * so instead of showing empty chart/table, the user is directed to HOME (input) view */
        currentIcon = 0;    	
       // Log.d("getFragment(currentIcon, currentElement))", getFragment(currentIcon, currentElement).toString());
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.replace(R.id.main_container, getFragment(currentIcon, currentElement));
	    menuAdd.setIcon(R.drawable.ico_add_on);
	    menuChart.setIcon(R.drawable.ico_chart);
	    menuTable.setIcon(R.drawable.ico_table);    	    
	    ft.commit();		
	}
}
