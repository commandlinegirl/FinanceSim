package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public interface AccountingElementSaver<T extends AccountingElement> {
	public void save(T accountingElement) throws StorageException;
	public void remove(T accoutingElement) throws StorageException;
}
