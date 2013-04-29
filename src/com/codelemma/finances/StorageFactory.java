package com.codelemma.finances;

import com.codelemma.finances.accounting.Storage;

import android.content.SharedPreferences;

public class StorageFactory {
	public static Storage create(SharedPreferences sharedPreferences) {
		return new SharedPreferencesStorage(sharedPreferences);
	}
}
