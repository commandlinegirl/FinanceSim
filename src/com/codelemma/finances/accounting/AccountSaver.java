package com.codelemma.finances.accounting;

public interface AccountSaver {
	void saveAccount(Account account) throws AccountSaverException;
	void saveAccountingElement(AccountingElement accountingElement) throws AccountSaverException;	
	void clear() throws AccountSaverException;
	void removeAccountingElement(AccountingElement accountingElement) throws AccountSaverException;

	static class AccountSaverException extends Exception {
		public AccountSaverException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
