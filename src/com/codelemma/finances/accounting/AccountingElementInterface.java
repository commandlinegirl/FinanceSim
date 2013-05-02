package com.codelemma.finances.accounting;

import com.codelemma.finances.InputListingUpdater;

public interface AccountingElementInterface {
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor);
	public void updateInputListing(InputListingUpdater modifier);
}