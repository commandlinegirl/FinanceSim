package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public interface NamedValue {
	public BigDecimal getValue();
	public String getName();
	public BigDecimal getAmount();
	public int getId();	
	public HistoryNew getHistory();
	
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor);
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException; 
	public void updateInputListing(InputListingUpdater modifier);
	public int getStartYear();
	public int getStartMonth();
}