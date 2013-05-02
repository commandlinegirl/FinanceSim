package com.codelemma.finances;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import com.codelemma.finances.accounting.Storage;

class SharedPreferencesStorage implements Storage {
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private boolean open;
	
	SharedPreferencesStorage(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
		editor = null;
		open = false;
	}		

	private void ensureOpen() throws StorageException {
		if (!open) {
			throw new StorageException("Storage not open");
		}
	}
	
	private void ensureClosed() throws StorageException {
		if (open) {
			throw new StorageException("Storage not closed");
		}
	}
	
	private void ensureOpenForReading() throws StorageException {
		ensureOpen();
		if (editor != null) {
			throw new StorageException("Storage not open for reading");
		}
	}

	private void ensureOpenForWriting() throws StorageException {
		ensureOpen();
		if (editor == null) {
			throw new StorageException("Storage not open for writing");
		}
	}
	
	private static String makeStorageKey(String prefix, String key) {
		return prefix + "-" + key;
	}
	
	@Override
	@SuppressLint("CommitPrefEdits")
	public void open(OpenState openState) throws StorageException {
		Log.d("open is: ", String.valueOf(open));
		//ensureClosed();
		if (openState == OpenState.WRITE) {
			editor = preferences.edit();
			if (editor == null) {
				throw new StorageException("Could not open storage for writing (edit() returned null)");
			}
		}
		Log.d("Setting open to true", "called");
		open = true;
	}

	@Override
	public void close() throws StorageException {
		//ensureOpen();
		if (editor != null) {
			Log.d("storage editor commit called", "called");
			editor.commit();
			editor = null;
		}
		open = false;
	}

	@Override
	public void clear() throws StorageException {
		ensureOpenForWriting();
		editor.clear();
	}
	
	@Override
	public String getString(String prefix, String key) throws StorageException {
		ensureOpenForReading();
		String storageKey = makeStorageKey(prefix, key);
		if (!preferences.contains(storageKey)) {
			throw new StorageException("Key not found in storage: " + storageKey);
		}
		return preferences.getString(storageKey, null);
	}
	
	@Override
	public void putString(String prefix, String key, String value) throws StorageException {
		ensureOpenForWriting();
		String storageKey = makeStorageKey(prefix, key);
		editor.putString(storageKey, value);
	}
	
	@Override
	public int getInt(String prefix, String key) throws StorageException {
		ensureOpenForReading();
		String storageKey = makeStorageKey(prefix, key);
		if (!preferences.contains(storageKey)) {
			throw new StorageException("Key not found in storage: " + storageKey);
		}
		return preferences.getInt(storageKey, 0);
	}
	
	@Override
	public void putInt(String prefix, String key, int value) throws StorageException {
		ensureOpenForWriting();
		String storageKey = makeStorageKey(prefix, key);
		editor.putInt(storageKey, value);
	}

	@Override
	public BigDecimal getBigDecimal(String prefix, String key) throws StorageException {
		ensureOpenForReading();
		String storageKey = makeStorageKey(prefix, key);
		if (!preferences.contains(storageKey)) {
			throw new StorageException("Key not found in storage: " + storageKey);
		}
		return new BigDecimal(preferences.getString(storageKey, null));
	}
	
	@Override
	public void putBigDecimal(String prefix, String key, BigDecimal value) throws StorageException {
		ensureOpenForWriting();
		String storageKey = makeStorageKey(prefix, key);
		editor.putString(storageKey, value.toString());
	}

	@Override
	public void remove(String prefix, String key) throws StorageException {
		ensureOpenForWriting();
		String storageKey = makeStorageKey(prefix, key);
		editor.remove(storageKey);
	}
}