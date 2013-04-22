package com.codelemma.finances;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
	private MenuItem currentMenuItem;
	private HashMap<MenuItem,Drawable> menuItems = new HashMap<MenuItem,Drawable>();
	private int[][] optionsMenuIds = new int[6][3];
	private ProgressBar progressBar;		
	
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
	    optionsMenuIds[0][0] = R.layout.expl_income_add;
	    optionsMenuIds[0][1] = R.layout.expl_income_chart;
	    optionsMenuIds[0][2] = R.layout.expl_income_table;
	    optionsMenuIds[1][0] = R.layout.expl_expenses_add;
	    optionsMenuIds[1][1] = R.layout.expl_expenses_chart;
	    optionsMenuIds[1][2] = R.layout.expl_expenses_table;
	    optionsMenuIds[2][0] = R.layout.expl_savings_add;
	    optionsMenuIds[2][1] = R.layout.expl_savings_chart;
	    optionsMenuIds[2][2] = R.layout.expl_savings_table;
	    optionsMenuIds[3][0] = R.layout.expl_debts_add;
	    optionsMenuIds[3][1] = R.layout.expl_debts_chart;
	    optionsMenuIds[3][2] = R.layout.expl_debts_table;
	    optionsMenuIds[4][0] = R.layout.expl_cashflows_add;
	    optionsMenuIds[4][1] = R.layout.expl_cashflows_chart;
	    optionsMenuIds[4][2] = R.layout.expl_cashflows_table;
	    optionsMenuIds[5][0] = R.layout.expl_networth_add;
	    optionsMenuIds[5][1] = R.layout.expl_networth_chart;
	    optionsMenuIds[5][2] = R.layout.expl_networth_table;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {		
	}

	private static final ArrayList<Class<? extends SherlockFragment>> TAB_FRG_CLASSES 
	        = new ArrayList<Class<? extends SherlockFragment>>();
	private static final ArrayList<Class<? extends SherlockFragment>> DATA_FRG_CLASSES 
	        = new ArrayList<Class<? extends SherlockFragment>>();
			
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
		Log.d("onTabSelected ", "called");
		currentElement = tab.getPosition();
		if (appState != null) {
		    appState.setSpinnerPosition(0);
		}
		ft.replace(R.id.main_container, getFragment(currentIcon, currentElement));		
	 }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void setToIncomeTab() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();		   
		ActionBar actionbar = getSupportActionBar();
		actionbar.setSelectedNavigationItem(0);		    
		ft.replace(R.id.main_container, getFragment(0, 0));
		ft.commit();
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
		menuItems.put(menuChart, getResources().getDrawable(R.drawable.ico_chart));
		menuItems.put(menuTable, getResources().getDrawable(R.drawable.ico_table));
		menuItems.put(menuAdd, getResources().getDrawable(R.drawable.ico_add));
		
		currentMenuItem = menuAdd;
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
    	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
    	    currentMenuItem = item;
    	    ft.commit();    	    
			return true;
		case R.id.menu_chart:
            currentIcon = 1;
    		if (appState.needToRecalculate() == true) {
    			recalculate(new FrgChart());
    		} else {           
    	        ft.replace(R.id.main_container, new FrgChart());     	    
    	        ft.commit();	
    		}
    	    item.setIcon(R.drawable.ico_chart_on);
    	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
    	    currentMenuItem = item;  
			return true;
		case R.id.menu_table:
            currentIcon = 2;               
    		if (appState.needToRecalculate() == true) {
    			recalculate(new FrgList());
    		} else {           
    	        ft.replace(R.id.main_container, new FrgList());     	    
    	        ft.commit();	
    		}    	    
    	    item.setIcon(R.drawable.ico_table_on);  
    	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
    	    currentMenuItem = item;
			return true;			
		case R.id.menu_about:
			Intent intent = new Intent(this, About.class);					
		    startActivity(intent);	
			return true;
		case R.id.menu_feedback:
			Intent intent2 = new Intent(this, Feedback.class);					
		    startActivity(intent2);	
			return true;		
		case R.id.menu_explain:	
			//Toast.makeText(this, "currentElement:"+currentElement+" "+"currentIcon:"+currentIcon, Toast.LENGTH_SHORT).show();
			Dialog dialog = new Dialog(this, R.style.FullHeightDialog);			
			dialog.setContentView(R.layout.help_incomegeneric);
			dialog.setContentView(optionsMenuIds[currentElement][currentIcon]);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
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
  	    ActionBar.Tab tab2 = actionbar.newTab().setText(getResources().getString(R.string.savings_title)).setTabListener(this);
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
		Log.d("Main.addInvestment()", view.toString());
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
        	appState.needToRecalculate(true);
        }        
        if (resultCode == AcctElements.INVESTMENTSAV.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentSavAcctResult(data, requestCode);
        	appState.needToRecalculate(true);
        }        
        if (resultCode == AcctElements.INVESTMENTCHECKACCT.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentCheckAcctResult(data, requestCode);
        	appState.needToRecalculate(true);
        }   
        if (resultCode == AcctElements.INVESTMENT401K.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestment401kResult(data, requestCode);
        	appState.needToRecalculate(true);
        }        
        if (resultCode == AcctElements.INVESTMENTBOND.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentBondResult(data, requestCode);
        	appState.needToRecalculate(true);
        }     
        if (resultCode == AcctElements.INVESTMENTSTOCK.getNumber()) {
        	FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgInvestment.onInvestmentStockResult(data, requestCode);
        	appState.needToRecalculate(true);
        }     
        if (resultCode == AcctElements.EXPENSE.getNumber()) {      
        	FrgExpense frgExpense = (FrgExpense) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgExpense.onExpenseResult(data, requestCode);
        	appState.needToRecalculate(true);
        }
        if (resultCode == AcctElements.DEBTLOAN.getNumber()) {      
        	FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgDebt.onDebtLoanResult(data, requestCode);
        }
        if (resultCode == AcctElements.DEBTMORTGAGE.getNumber()) {      
        	FrgDebt frgDebt = (FrgDebt) getSupportFragmentManager().findFragmentById(R.id.main_container);
        	frgDebt.onDebtMortgageResult(data, requestCode);
        }        
	    menuChart.setIcon(R.drawable.ico_chart_excl);
	    menuTable.setIcon(R.drawable.ico_table_excl);   	    
	}
	
	private class AsyncSimGenerator extends AsyncTask<SherlockFragment, Integer, SherlockFragment> {
		
		private Dialog dialog;
		int myProgress;
		
		 
		@Override
	    protected void onPreExecute() {
			dialog = new Dialog(Main.this, R.style.FullHeightDialog);			
			dialog.setContentView(R.layout.progress_bar);
			progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar_horizontal);
	        progressBar.setProgress(0);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
	        myProgress = 0;
	    };
		
		@Override
		protected SherlockFragment doInBackground(SherlockFragment... params) {
			
			SherlockFragment fragment = params[0];
	        account.setInitDebt();
	        account.setInitExpense();
	        account.setInitIncome();
	        account.setInitInvestment();
	        account.setInitCashflow();
	        account.setInitNetworth();
	        account.clearHistory(history);
	                                   
	        int month = appState.getCalculationStartMonth();
	        int year = appState.getCalculationStartYear();	        	        
	        appState.computeCalculationLength();
	        	        
	        int totalCalculationLength = appState.getTotalCalculationLength();
	        int preCalculationLength = appState.getPreCalculationLength(); 
	        
	        int index = -1; //TODO: should be -1???
	        for (int i = 0; i < totalCalculationLength; i++) {
	        	if (i >= preCalculationLength) {    
	                index++;
	        	}           
	        	account.advance(index, year, month); //TODO: dates - should be minimum dates
	        	publishProgress(index);
	            if (month == 11) {
	                month = 0;
	                year += 1;
	            } else {
	                month++;
	            }
	            myProgress++;
	            publishProgress(myProgress);
	        }
	        
	        account.addToHistory(history);
	        appState.needToRecalculate(false);
			return fragment;
		}

		@Override
	    protected void onProgressUpdate(Integer... progress) {        
			progressBar.setProgress(progress[0]);
	    }

		@Override
	    protected void onPostExecute(SherlockFragment fragment) {
	        //showDialog("Downloaded " + result + " bytes");
			super.onPostExecute(fragment);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); 
			ft.replace(R.id.main_container, fragment); 
    	    ft.commit();
			
			dialog.dismiss();
	    }
	}
		
	public void recalculate(SherlockFragment fragment) {			
		AsyncSimGenerator task = new AsyncSimGenerator();
		task.execute(fragment);		
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
	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
	    currentMenuItem = menuAdd;
	    ft.commit();			    
	}
	
	public void showChart(View view) {
        currentIcon = 1;	
        if (appState.needToRecalculate() == true) {
			recalculate(new FrgChart());
		} else {           
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        ft.replace(R.id.main_container, new FrgChart());     	    
	        ft.commit();	
		}
	    menuChart.setIcon(R.drawable.ico_chart_on);
	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
	    currentMenuItem = menuChart;
	}
	
	public void showTable(View view) {
        currentIcon = 2;       
        if (appState.needToRecalculate() == true) {
			recalculate(new FrgList());
		} else {           
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        ft.replace(R.id.main_container, new FrgList());     	    
	        ft.commit();	
		}
        
	    menuTable.setIcon(R.drawable.ico_table_on);
	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
	    currentMenuItem = menuTable;
	}
}
