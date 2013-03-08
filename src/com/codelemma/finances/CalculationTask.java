package com.codelemma.finances;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;

import android.os.AsyncTask;

public class CalculationTask extends AsyncTask<Finances, Void, Void> {
  
	private History history;

	@Override
	protected Void doInBackground(Finances... params) {
		// TODO Auto-generated method stub
		
		Finances appState = params[0];
        int month = appState.getMonth();
        int year = appState.getYear();
        Account account = appState.getAccount();
        history = appState.getHistory();
				
        for (int i = 0; i < appState.getListSize(); i++) { 
        	account.advance(i, month); //TODO: this call should be done in BKG
            if (month == 11) {
                month = 0;
                year += 1;
            } else {
                month++;
            }
            if (isCancelled()) break;
        }
              			
		return null;
	}



	
}
