package com.codelemma.finances.accounting;

public interface AccountFactory {
	// Perhaps these two parameters may be moved to storage rather than being determined based on current date?
	Account createAccount(int simStartYear, int simStartMonth) throws AccountCreationException;

	static class AccountCreationException extends Exception {
		public AccountCreationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}