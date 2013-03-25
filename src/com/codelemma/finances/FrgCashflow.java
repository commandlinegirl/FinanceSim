package com.codelemma.finances;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.Account;


public class FrgCashflow extends SherlockFragment {
	
	private Account account;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment    	
        View frView = inflater.inflate(R.layout.frg_cashflows, container, false);
        return frView;
	}
	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        // Inflate the layout for this fragment 
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
		

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("FrgExpense.onStart()", "called");
		Finances appState = Finances.getInstance();
		account = appState.getAccount();	
	
	}


}
