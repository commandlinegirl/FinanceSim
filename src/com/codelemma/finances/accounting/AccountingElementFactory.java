package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public interface AccountingElementFactory<T extends AccountingElement> {
	public T load(int id) throws StorageException;
}