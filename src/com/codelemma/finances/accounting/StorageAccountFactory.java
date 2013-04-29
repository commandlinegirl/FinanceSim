package com.codelemma.finances.accounting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.codelemma.finances.accounting.Storage.StorageException;

/**
 * Factory(dp) which instantiates Account objects based on data read from storage.
 * This factory may throw any exceptions related to reading from storage and
 * parsing the data read.
 */
public class StorageAccountFactory implements AccountFactory {
    private Storage storage;
    
    public StorageAccountFactory(Storage storage) {
    	this.storage = storage;
    }
    
    @Override
    public Account createAccount(int simStartYear, int simStartMonth) throws AccountCreationException {
    	Account account = new Account(simStartYear, simStartMonth);
    	try {
    		configureAccountFromStorage(account);
    		populateAccountWithAccountingElements(account);
    	} catch (StorageException se) {
    		throw new AccountCreationException("Storage exception occured", se);
    	}
    	return account;
    }
    
    private void configureAccountFromStorage(Account account) throws StorageException {
    	account.setInvestmentsPercontrib(storage.getBigDecimal(Storage.Constants.INVESTMENT_PERCONTRIB));
    	account.setCheckingAcctPercontrib();
    }
    
    private void populateAccountWithAccountingElements(Account account) throws StorageException {
    	int elementCount = storage.getInt(Storage.Constants.ACCOUNTING_ELEMENT_COUNT);
    	for (int i = 0; i < elementCount; i++) {
    		account.addAccountingElement(createAccountingElementFromStorage(i));
    	}
    }
    
    private AccountingElement createAccountingElementFromStorage(int num) throws StorageException {
		String elementFactoryClassKey = storage.getString(Storage.Constants.ACCOUNTING_ELEMENT_CLASS_KEY);
		Class<?> elementFactoryClass = Storage.Constants.CLASS_KEY_TO_CLASS.get(elementFactoryClassKey);
		if (elementFactoryClass == null) {
			throw new StorageException("Invalid factory class key " + elementFactoryClassKey);
		}
		Method elementCreateMethod;
		try {
		    elementCreateMethod = elementFactoryClass.getMethod("create", Storage.class);
		} catch (NoSuchMethodException nsme) {
			throw new StorageException("Factory method not found for key " + elementFactoryClassKey, nsme);
		}
		AccountingElement element;
		try {
    	    element = (AccountingElement) elementCreateMethod.invoke(null, storage);
		} catch (InvocationTargetException ite) {
			throw new StorageException("Exception thrown by factory for key " + elementFactoryClassKey, ite);
		} catch (IllegalAccessException iae) {
			throw new StorageException("Factory inaccessible for key " + elementFactoryClassKey, iae);
		} catch (ClassCastException cce) {
			throw new StorageException("Factory returned object of unexpected class for key " + elementFactoryClassKey, cce);
		}
		return element;
    }
}