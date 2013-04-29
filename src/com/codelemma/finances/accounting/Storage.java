package com.codelemma.finances.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.codelemma.finances.ParseException;

public interface Storage {
	public static class Constants {
		// Storage property keys.
		public final static String INVESTMENT_PERCONTRIB = "ipc";
		public final static String ACCOUNTING_ELEMENT_COUNT = "aec";
		public final static String ACCOUNTING_ELEMENT_CLASS_KEY = "aeck";
		
		// Legal values of Storage property value for class key.
		public final static String INCOME_GENERIC = "ig";
		public final static String EXPENSE_GENERIC = "eg";
		public final static String DEBT_LOAN = "dl";
		public final static String DEBT_MORTGAGE = "dm";
		public final static String INVESTMENT_CHECKING_ACCOUNT = "ica";
		public final static String INVESTMENT_SAVINGS_ACCOUNT = "isa";
		public final static String INVESTMENT_401K = "i4";
		
		public final static Map<String, Class<?>> CLASS_KEY_TO_CLASS;
		
		static {
			Map<String, Class<?>> tmp = new HashMap<String, Class<?>>();
			tmp.put(INCOME_GENERIC, IncomeGenericFactory.class);
			tmp.put(EXPENSE_GENERIC, ExpenseGenericFactory.class);
			tmp.put(DEBT_LOAN, DebtLoanFactory.class);
			tmp.put(DEBT_MORTGAGE, DebtMortgageFactory.class);
			tmp.put(INVESTMENT_CHECKING_ACCOUNT, InvestmentCheckAcctFactory.class);
			tmp.put(INVESTMENT_SAVINGS_ACCOUNT, InvestmentSavAcctFactory.class);
			tmp.put(INVESTMENT_401K, Investment401kFactory.class);
			CLASS_KEY_TO_CLASS = Collections.unmodifiableMap(tmp);
		}
	}
		
	void saveAccount(Account account) throws ParseException;	
	
	String getString(String key) throws StorageException;
	void setString(String key, String value) throws StorageException;
	
	int getInt(String key) throws StorageException;
	void setInt(String key, int value) throws StorageException;
	
	BigDecimal getBigDecimal(String key) throws StorageException;
	void setBigDecimal(String key, BigDecimal value) throws StorageException;
	
	void clear() throws StorageException;
	
	static class StorageException extends Exception {
		public StorageException(String message) {
			super(message);
		}
		
		public StorageException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
