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
import com.codelemma.finances.accounting.Storage.StorageException;

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
			accountSaver.saveAccount(account);
		} catch (AccountSaverException se) {
			// TODO for Ola
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

	public void setShowStartupWindow(int i) {
		Storage storage = StorageFactory.create(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
		try {
        	storage.open(Storage.OpenState.WRITE);
        	storage.putInt("#", "start_popup_window", i);
        } catch (StorageException e) {
			e.printStackTrace();
		} finally {
            storage.close();
		}
	}

	public int showStartupWindow() {
		Storage storage = StorageFactory.create(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
		int i = 0;
		try {
        	storage.open(Storage.OpenState.READ);
        	i = storage.getInt("#", "start_popup_window");   
        } catch (StorageException e) {
        	i = 1;
			e.printStackTrace();
		} finally {
            storage.close();
		}
		return i;
	}
}