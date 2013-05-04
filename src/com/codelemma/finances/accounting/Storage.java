package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public interface Storage {
	public static class StorageException extends Exception {
		public StorageException(String message) {
			super(message);
		}
		
		public StorageException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public enum OpenState {
		READ,
		WRITE,
	}

	void open(OpenState openState) throws StorageException;
	void close();
	void clear() throws StorageException;

	String getString(String prefix, String key) throws StorageException;
	BigDecimal getBigDecimal(String prefix, String key) throws StorageException;
	int getInt(String prefix, String key) throws StorageException;

	void putString(String prefix, String key, String value) throws StorageException;
	void putBigDecimal(String prefix, String key, BigDecimal value) throws StorageException;
	void putInt(String prefix, String key, int value) throws StorageException;

	void remove(String prefix, String key) throws StorageException;
}