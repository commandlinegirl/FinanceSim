package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public interface AccountingElement {
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
	public void setValuesBeforeCalculation();
    public void initialize();     

	public void addToAccount(Account account); // Visitor pattern to provide double-dispatch on account as well as on accounting element.
}