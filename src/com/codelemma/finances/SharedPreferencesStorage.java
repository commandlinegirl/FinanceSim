package com.codelemma.finances;

import java.math.BigDecimal;

import android.content.SharedPreferences;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.AccountingElement;
import com.codelemma.finances.accounting.PackToContainerVisitor;
import com.codelemma.finances.accounting.Storage;

class SharedPreferencesStorage implements Storage {
	private SharedPreferences preferences;
	
	SharedPreferencesStorage(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
	}		
		
	private String saveElements(Iterable<? extends AccountingElement> element) throws ParseException {
		PackToContainerVisitor packToContainerVisitor = new PackToContainerLauncher();
		TypedContainer container = new TypedContainer();		
		for(AccountingElement value : element) {
			container.put(TypedKey.getNumericKey(value.getId()), value.getFieldContainer(packToContainerVisitor));
		}
        return container.toString();		
	}
	
	@Override
	public void saveAccount(Account account) throws ParseException {
		SharedPreferences.Editor editor = preferences.edit();
		Iterable<? extends AccountingElement> incomes = (Iterable<? extends AccountingElement>) account.getIncomes();
	    String incomesStr = saveElements(incomes); //TODO: check size!
		
		Iterable<? extends AccountingElement> investments = (Iterable<? extends AccountingElement>) account.getInvestments();		
		String investmentsStr = saveElements(investments);

		Iterable<? extends AccountingElement> expenses = (Iterable<? extends AccountingElement>) account.getExpenses();		
		String expensesStr = saveElements(expenses);
		
		Iterable<? extends AccountingElement> debts = (Iterable<? extends AccountingElement>) account.getDebts();		
		String debtsStr = saveElements(debts);
		
	  	editor.putString(TypedKey.INCOMES.getKeyword(), incomesStr);
	  	editor.putString(TypedKey.INVESTMENTS.getKeyword(), investmentsStr);
	  	editor.putString(TypedKey.EXPENSES.getKeyword(), expensesStr);
	  	editor.putString(TypedKey.DEBTS.getKeyword(), debtsStr);	  	
	  	editor.commit();	  	
	}	
	
	@Override
	public String getString(String key) throws StorageException {
		if (!preferences.contains(key)) {
			throw new StorageException("Key not found in storage: " + key);
		}
		return preferences.getString(key, null);
	}
	
	@Override
	public void setString(String key, String value) throws StorageException {
		//TODO
	}
	
	@Override
	public int getInt(String key) throws StorageException {
		if (!preferences.contains(key)) {
			throw new StorageException("Key not found in storage: " + key);
		}
		return preferences.getInt(key, 0);
	}
	
	@Override
	public void setInt(String key, int value) throws StorageException {
		//TODO
	}

	@Override
	public BigDecimal getBigDecimal(String key) throws StorageException {
		if (!preferences.contains(key)) {
			throw new StorageException("Key not found in storage: " + key);
		}
		return new BigDecimal(preferences.getString(key, null));
	}
	
	@Override
	public void setBigDecimal(String key, BigDecimal value) throws StorageException {
		//TODO
	}

	@Override
	public void clear() throws StorageException {
		// TODO Auto-generated method stub	
	}
}
 
