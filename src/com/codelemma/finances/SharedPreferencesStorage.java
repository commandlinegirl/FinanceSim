package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;

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
		ensureClosed();
		if (openState == OpenState.WRITE) {
			editor = preferences.edit();
			if (editor == null) {
				throw new StorageException("Could not open storage for writing (edit() returned null)");
			}
		}
		open = true;
	}

	@Override
	public void close() {
		if (editor != null) {
			editor.commit();
			editor = null;
		}
		open = false;
	}

	@Override
	public void clear() throws StorageException {
		ensureOpenForWriting();
		Map<String,?> all_preferences = preferences.getAll();
		for(Map.Entry<String,?> entry : all_preferences.entrySet()) {
			String key = entry.getKey();
			if (!key.startsWith("#")) {
				editor.remove(key);
			}
		}
		editor.commit();
		//editor.clear();
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