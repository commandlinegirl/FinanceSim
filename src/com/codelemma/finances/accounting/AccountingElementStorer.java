package com.codelemma.finances.accounting;

public interface AccountingElementStorer<T extends AccountingElement> 
    extends AccountingElementFactory<T>, AccountingElementSaver<T> {
	public String getTag();
}
