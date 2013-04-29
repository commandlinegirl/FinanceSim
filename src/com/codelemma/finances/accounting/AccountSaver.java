package com.codelemma.finances.accounting;

public interface AccountSaver {
	void saveAccountConfiguration(Account account) throws AccountSavingException;
	void saveAccountingElement(AccountingElement accountElement) throws AccountSavingException;
	
	static class AccountSavingException extends Exception {
		public AccountSavingException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
