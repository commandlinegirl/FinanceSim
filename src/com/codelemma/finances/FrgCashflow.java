package com.codelemma.finances;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FrgCashflow extends SherlockFragment {
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    	
        return inflater.inflate(R.layout.frg_cashflows, container, false);
	}
}
