package com.codelemma.finances;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.codelemma.finances.accounting.ModifyUiVisitor;
import com.codelemma.finances.accounting.NamedValue;

public class ModifyUiListener implements OnClickListener {

	private ModifyUiVisitor modifyUiLauncher;
 
	public ModifyUiListener(ModifyUiVisitor modifyUiLauncher) {
		this.modifyUiLauncher = modifyUiLauncher;
	}
	
	@Override
	public void onClick(View view) {
		RelativeLayout layout = (RelativeLayout) view;
        layout.setBackgroundColor(0xFFE6E6E6);	        
        NamedValue value = (NamedValue) view.getTag(R.string.acct_object);
        value.launchModifyUi(modifyUiLauncher);	    	    
	}
}


