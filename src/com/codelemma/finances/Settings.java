package com.codelemma.finances;

import java.util.Calendar;

import com.codelemma.finances.accounting.Months;
import com.codelemma.finances.accounting.Storage.StorageException;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

	private Months[] months = Months.values();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		try {
			setupSimplePreferencesScreen();
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() throws StorageException {
		addPreferencesFromResource(R.xml.pref_general);
		DatePickerPreference dataPref = (DatePickerPreference) findPreference("simulation_start_date");
		Calendar calendar = dataPref.getDate();
		dataPref.setSummary(months[calendar.get(Calendar.MONTH)]+"/"+calendar.get(Calendar.YEAR));
	}
}
