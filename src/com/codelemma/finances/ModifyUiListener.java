package com.codelemma.finances;

import com.codelemma.finances.accounting.AccountingElementInterface;
import com.codelemma.finances.accounting.ModifyUiVisitor;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class ModifyUiListener implements OnClickListener {

	private ModifyUiVisitor modifyUiLauncher;
 
	public ModifyUiListener(ModifyUiVisitor modifyUiLauncher) {
		this.modifyUiLauncher = modifyUiLauncher;
	}

	@Override
	public void onClick(View view) {
		RelativeLayout layout = (RelativeLayout) view;
        layout.setBackgroundColor(0xFFE6E6E6);
        AccountingElementInterface value = (AccountingElementInterface) view.getTag(R.string.acct_object);
        value.launchModifyUi(modifyUiLauncher);	    	    
	}
}