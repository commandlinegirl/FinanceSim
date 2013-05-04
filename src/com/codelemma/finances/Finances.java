package com.codelemma.finances;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.AccountSaver;
import com.codelemma.finances.accounting.AccountSaver.AccountSaverException;
import com.codelemma.finances.accounting.AccountStorage;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.SafeAccountFactory;
import com.codelemma.finances.accounting.Storage;

public class Finances extends Application {
    private static Finances appInstance;
	private SafeAccountFactory accountFactory;
	private AccountSaver accountSaver;
    private Account account;
    private History history;    
	private boolean needToRecalculate = true;
	private int numberOfMonthsInChart = 60; //5*12;
	//needed to save the user's spinner setting across chart & table fragments	 
    private int spinnerPosition = 0;     
	
    @Override
    public void onCreate() {
        super.onCreate();
        Preconditions.check(appInstance == null, "appInstance already set");
        appInstance = this;
		Storage storage = StorageFactory.create(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
		// TODO: remove debugger when finished with application
		DebugStorage debugStorage = new DebugStorage(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
		debugStorage.printSharedPreferences();
		//AccountStorage accountStorage = new AccountStorage(storage);
		AccountStorage accountStorage = new AccountStorage(storage);
		accountSaver = accountStorage;
        accountFactory = new SafeAccountFactory(accountStorage);
    }

    public static Finances getInstance() {
         return appInstance;
    }
    
	public void setNumberOfMonthsInChart(int months) {
		numberOfMonthsInChart = months;
	}

	public int getNumberOfMonthsInChart() {
		return numberOfMonthsInChart;
	}

	public boolean needToRecalculate() {
		return needToRecalculate;
	}

	public void needToRecalculate(boolean p) {
		needToRecalculate = p;
	}

	public void setAccount() {
		account = accountFactory.loadAccount();
	}

	public void saveAccount() {
		try {
			Log.d("Finances.saveAccount()", "Saving Account");
			accountSaver.saveAccount(account);
		} catch (AccountSaverException se) {
			// TODO for Ola
			Log.d("Finances.saveAccount()", "Cannot save account");
			se.printStackTrace();
		}
	}
 
	public void setHistory() {
		history = new History();
	}
	
	public int getSpinnerPosition() {
		return spinnerPosition;
	}
	
	public void setSpinnerPosition(int pos) {
		spinnerPosition = pos;
	}

	public Account getAccount(){
	    return account;
	}		

	public History getHistory(){
	    return history;
	}
}