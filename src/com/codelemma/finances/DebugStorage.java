package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

import com.codelemma.finances.accounting.Storage;

public class DebugStorage implements Storage {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	DebugStorage(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
		editor = null;
	}

	public void printSharedPreferences() {
		Map<String,?> entries = preferences.getAll();
		if (entries.size() == 0) {
			Log.d("DebugStorage.printSharedPreferences()", "storage is empty");
		}
		for(Map.Entry<String,?> entry : entries.entrySet()) {
			Log.d("SharedPreferences START", "---------------------");
			Log.d("SharedPreferences", String.format("{%s}:{%s}", entry.getKey(), entry.getValue()));
			Log.d("SharedPreferences END", "---------------------");
		}
	}

	@Override
	public void open(OpenState openState) throws StorageException {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getString(String prefix, String key) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String prefix, String key)
			throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInt(String prefix, String key) throws StorageException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putString(String prefix, String key, String value)
			throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putBigDecimal(String prefix, String key, BigDecimal value)
			throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putInt(String prefix, String key, int value)
			throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String prefix, String key) throws StorageException {
		// TODO Auto-generated method stub

	}

}
