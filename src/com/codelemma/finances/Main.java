package com.codelemma.finances;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.finances.Finances;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;

public class Main extends SherlockFragmentActivity 
                  implements TabListener, 
                             DirectToHomeListener.OnClickOnEmptyListener {

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
	private Dialog progressBarDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
    
        setupActionBar();
	    appState = Finances.getInstance();
        showStartPopup();  
	                            	    
	    if (appState.getAccount() == null) {
			appState.setAccount();
	    }
	    if (appState.getHistory() == null) {
		    appState.setHistory();
	    }

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
        Preconditions.checkNotNull(Finances.getInstance(), "Finances instance doesn't exist");
	    Finances.getInstance().saveAccount();
		// close dialog before exiting activity
		if (progressBarDialog != null) {
		    progressBarDialog.dismiss();
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
		case R.id.menu_settings:
			Intent intent3 = new Intent(this, Settings.class);					
		    startActivity(intent3);
			return true;	
		case R.id.menu_explain:	
			Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
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
	
	public void addIncome(View view) {
		/* It is assumed AddIncome button is clicked from within FrgIncome fragment */
		FrgIncome frgIncome = (FrgIncome) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgIncome.add(view);
	}
	
	public void addInvestment(View view) {
		/* It is assumed AddInvestment button is clicked from within FrgInvestment fragment */
		FrgInvestment frgInvestment = (FrgInvestment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgInvestment.add(view);
	}
	
	public void addExpense(View view) {
		/* It is assumed AddExpense button is clicked from within FrgExpense fragment */
		FrgExpense frgExpense = (FrgExpense) getSupportFragmentManager().findFragmentById(R.id.main_container);
		frgExpense.add(view);
	}
	
	public void addDebt(View view) {
		/* It is assumed AddDebt button is clicked from within FrgDebt fragment */
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

		@Override
	    protected void onPreExecute() {
			progressBarDialog = new Dialog(Main.this, R.style.FullHeightDialog);			
			progressBarDialog.setContentView(R.layout.progress_bar);
			progressBar = (ProgressBar) progressBarDialog.findViewById(R.id.progressbar_horizontal);
	        progressBar.setProgress(0);
	        progressBarDialog.setCanceledOnTouchOutside(true);
	        progressBarDialog.show();
	    };
		
		@Override
		protected SherlockFragment doInBackground(SherlockFragment... params) {
			
			SherlockFragment fragment = params[0];
			appState.getAccount().setInitDebt();
			appState.getAccount().setInitExpense();
			appState.getAccount().setInitIncome();
			appState.getAccount().setInitInvestment();
			appState.getAccount().setInitCashflow();
			appState.getAccount().setInitNetworth();
	        appState.getAccount().clearHistory(appState.getHistory());
	                                   
	        int month = appState.getAccount().getCalculationStartMonth();
	        int year = appState.getAccount().getCalculationStartYear();	        	        
	        appState.getAccount().computeCalculationLength();
	        int totalCalculationLength = appState.getAccount().getTotalCalculationLength();
	        int preCalculationLength = appState.getAccount().getPreCalculationLength(); 
	        
	        int index = -1; //TODO: should be -1???
	        for (int i = 0; i < totalCalculationLength; i++) {
	        	if (i >= preCalculationLength) {    
	                index++;
	        	}
	        	appState.getAccount().advance(index, year, month); //TODO: dates - should be minimum dates
	        	publishProgress(index);
	            if (month == 11) {
	                month = 0;
	                year += 1;
	            } else {
	                month++;
	            }
	        }
	        
	        appState.getAccount().addToHistory(appState.getHistory());
	        appState.needToRecalculate(false);
			return fragment;
		}

		@Override
	    protected void onProgressUpdate(Integer... progress) {        
			progressBar.setProgress(progress[0]);
	    }

		@Override
	    protected void onPostExecute(SherlockFragment fragment) {
			/* Show fragment with data when simulation is finished */
			super.onPostExecute(fragment);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); 
			ft.replace(R.id.main_container, fragment); 
    	    ft.commit();
    	    progressBarDialog.dismiss();
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
	    return appState.getAccount().getDates();
	}
	
	public int getCurrentElement(){
	    return currentElement;
	}

	@Override
	public void onClickOnEmptySelected(View view, int element) {
		/* Called when used clicks on CHART or TABLE icons while there is no input data
		 * so instead of showing empty chart/table, the user is directed to HOME (input) view */
        currentIcon = 0;        
        if (currentElement == 0) {
        	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	    ft.replace(R.id.main_container, getFragment(0, 0));    	    
    	    ft.commit();
    	    Intent intent = new Intent(this, AddIncomeGeneric.class);		
    		intent.putExtra("request", AcctElements.ADD.toString());
    	    startActivityForResult(intent, AcctElements.ADD.getNumber());
        } else if (currentElement == 1) { 
        	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	    ft.replace(R.id.main_container, new FrgExpense());    	    
    	    ft.commit();
    	    Intent intent = new Intent(this, AddExpenseGeneric.class);		
    		intent.putExtra("request", AcctElements.ADD.toString());
    	    startActivityForResult(intent, AcctElements.ADD.getNumber());
	    } else {
    		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	    ft.replace(R.id.main_container, getFragment(currentIcon, currentElement));
    	    ft.commit();
        }        
	    menuAdd.setIcon(R.drawable.ico_add_on);
	    currentMenuItem.setIcon(menuItems.get(currentMenuItem));
	    currentMenuItem = menuAdd;	    			    
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

	protected void showStartPopup() {	
		if (appState.showStartupWindow() == 1) {
			Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
			dialog.setContentView(R.layout.start_popup);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			appState.setShowStartupWindow(0); //TODO: set to 0!!
		}
	}
}