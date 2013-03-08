package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public interface NamedValue {
	public BigDecimal getValue();
	public String getName();
	public int getId();	
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor);
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException; 
}