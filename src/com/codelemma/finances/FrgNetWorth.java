package com.codelemma.finances;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.actionbarsherlock.app.SherlockFragment;

public class FrgNetWorth extends SherlockFragment {
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {   	
        View frView = inflater.inflate(R.layout.frg_networth, container, false);
        return frView;
	}
}
