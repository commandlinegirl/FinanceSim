package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

import com.codelemma.finances.accounting.Storage;


public class DebugStorage implements Storage {
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
    private boolean open;
	
	DebugStorage(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
		editor = null;
		open = false;
	}

	public void printSharedPreferences() {
		Map<String,?> entries = preferences.getAll();
		if (entries.size() == 0) {
			Log.d("DebugStorage.printSharedPreferences()", "storage is empty");
		}
		Log.d("SharedPreferences START", "---------------------");
		for(Map.Entry<String,?> entry : entries.entrySet()) {
			Log.d("SharedPreferences", String.format("{%s}:{%s}", entry.getKey(), entry.getValue()));
		}
		Log.d("SharedPreferences END", "---------------------");
	}
	
	private void ensureOpen() throws StorageException {
		if (!open) {
			Log.d("StorageDBG.ensureOpen()", "Storage not open");
			throw new StorageException("Storage not open");
		}
	}
	
	private void ensureClosed() throws StorageException {
		if (open) {
			Log.d("StorageDBG.ensureClosed()", "Storage not closed");
			throw new StorageException("Storage not closed");
		}
	}
	
	private void ensureOpenForReading() throws StorageException {
		ensureOpen();
		if (editor != null) {
			Log.d("StorageDBG.ensureOpenForReading()", "editor is not null");
			throw new StorageException("Storage not open for reading");
		}
	}

	private void ensureOpenForWriting() throws StorageException {
		ensureOpen();
		if (editor == null) {
			Log.d("StorageDBG.ensureOpenForWriting()", "editor is null");
			throw new StorageException("Storage not open for writing");
		}
	}
	
	private static String makeStorageKey(String prefix, String key) {
		return prefix + "-" + key;
	}
	
	@Override
	public void open(OpenState openState) throws StorageException {
		Log.d("StorageDBG.open("+openState.toString()+")", "value of open: "+String.valueOf(open));
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
		Log.d("StorageDBG.close()", "value of open: "+String.valueOf(open));
		if (editor != null) {
			Log.d("storage editor commit called", "called");
			editor.commit();
			editor = null;
		}
		open = false;
	}
			
	@Override
	public void clear() throws StorageException {
		Log.d("StorageDBG.clear()", "value of open: "+String.valueOf(open));
		ensureOpenForWriting();
		editor.clear();
	}
	
	@Override
	public String getString(String prefix, String key) throws StorageException {
		Log.d("StorageDBG.getString()", "value of open: "+String.valueOf(open));
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
		Log.d("StorageDBG.putString() storageKey", storageKey);
		editor.putString(storageKey, value);
	}
	
	@Override
	public int getInt(String prefix, String key) throws StorageException {
		ensureOpenForReading();
		String storageKey = makeStorageKey(prefix, key);
		if (!preferences.contains(storageKey)) {
			Log.d("StorageDBG.putString(), Key not found in storage:", storageKey);
			throw new StorageException("Key not found in storage: " + storageKey);
		}
		return preferences.getInt(storageKey, 0);
	}
	
	@Override
	public void putInt(String prefix, String key, int value) throws StorageException {
		ensureOpenForWriting();
		String storageKey = makeStorageKey(prefix, key);
		Log.d("StorageDBG.putInt() storageKey", storageKey);
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
		Log.d("StorageDBG.remove() storageKey", storageKey);	
		editor.remove(storageKey);
	}
	
	
}