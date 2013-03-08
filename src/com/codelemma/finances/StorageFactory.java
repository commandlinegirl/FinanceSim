package com.codelemma.finances;

import android.content.SharedPreferences;

public class StorageFactory {
	public static Storage create(SharedPreferences sharedPreferences) {
		return new SharedPreferencesStorage(sharedPreferences);
	}
}
