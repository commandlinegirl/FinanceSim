package com.codelemma.finances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.NamedValue;
import com.codelemma.finances.accounting.PackToContainerVisitor;

class SharedPreferencesStorage implements Storage {
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String inc = "history_incomes";
	private String inv = "history_netGainInvestments";
	private String exp = "history_expenses";
	private String deb = "history_debts";
	private String bal = "history_balances";
	private PackToContainerVisitor packToContainerVisitor;
	
	SharedPreferencesStorage(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
        editor = preferences.edit();		
	}
	
	private void saveList(String key, String[] list) {
	  	editor.putString(key, Serializer.stringifyList(list));
	  	editor.commit();
	}
		
	private void saveMap(HashMap<String,String> map) {
		for (Map.Entry<String,String> entry: map.entrySet()) {
			editor.putString(entry.getKey(), entry.getValue());
		}		
		editor.commit();
	}
	
	@Override
	public void saveHistory(History history) {
		//saveList(inc, history.getIncomes());                        
		//saveList(inv, history.getNetInvestmentsGain()); 
		//saveList(exp, history.getExpenses());
		//saveList(deb, history.getDebts());
		//saveList(bal, history.getBalances()); 
	}		
		
	private String saveElements(Iterable<? extends NamedValue> element) throws ParseException {
		PackToContainerVisitor packToContainerVisitor = new PackToContainerLauncher();
		TypedContainer container = new TypedContainer();		
		for(NamedValue value : element) {
			container.put(TypedKey.getNumericKey(value.getId()), value.getFieldContainer(packToContainerVisitor));
		}		
        return container.toString();		
	}
	
	@Override
	public void saveAccount(Account account) throws ParseException {
				
		Iterable<? extends NamedValue> incomes = account.getIncomes();
	    String incomesStr = saveElements(incomes); //TODO: check size!
		
		Iterable<? extends NamedValue> investments = (Iterable<? extends NamedValue>) account.getInvestments();		
		String investmentsStr = saveElements(investments);

		Iterable<? extends NamedValue> expenses = account.getExpenses();		
		String expensesStr = saveElements(expenses);
		
		Iterable<? extends NamedValue> debts = account.getDebts();		
		String debtsStr = saveElements(debts);
		
	  	editor.putString(TypedKey.INCOMES.getKeyword(), incomesStr);
	  	editor.putString(TypedKey.INVESTMENTS.getKeyword(), investmentsStr);
	  	editor.putString(TypedKey.EXPENSES.getKeyword(), expensesStr);
	  	editor.putString(TypedKey.DEBTS.getKeyword(), debtsStr);	  	
	  	editor.commit();	  	
	}
	
	@Override
	public void clearHistory() {
		editor.remove(inc);                        
		editor.remove(inv); 
		editor.remove(exp);
		editor.remove(deb);
		editor.remove(bal); 
	}	
	    
	@Override
	public void saveInput(HashMap<String,String> input) {
		saveMap(input);	
	}

	@Override
	public HashMap<String,String> getInput(ArrayList<String> keys) {    	
		HashMap<String,String> input = new HashMap<String, String>();
		for(String s : keys) {
			input.put(s, preferences.getString(s, null));
		}
		return input;
	}

	@Override
	public HashMap<String,String> getInputMap(String key) {    	
		HashMap<String,String> input = new HashMap<String, String>();
		input.put(key, preferences.getString(key, null));
		return input;
	}	
	
	@Override
	public ArrayList<String> getNetIncomes() {    	 
		return Serializer.parseToList(preferences.getString(inc, ""));		
	}

	@Override
	public ArrayList<String> getNetInvestmentsGain() {    	 
		return Serializer.parseToList(preferences.getString(inv, ""));
	}

	@Override
	public ArrayList<String> getExpenses() {    	 
		return Serializer.parseToList(preferences.getString(exp, ""));
	}    

	@Override
	public ArrayList<String> getDebts() {    	 
		return Serializer.parseToList(preferences.getString(deb, ""));
	}
	
	@Override
	public ArrayList<String> getBalances() {    	 
		return Serializer.parseToList(preferences.getString(bal, ""));
	}
	
	@Override
	public void remove(String key) {
		editor.remove(key);
	}

	@Override
	public void clear() {
		editor.clear();
		editor.commit();
	}	
	
	@Override
	public String get(String key, String defaultVal) {
		return preferences.getString(key, defaultVal);
	}
	
	@Override
	public void put(String key, String val) {
		editor.putString(key, val);
		editor.commit();
	}
	
	@Override
	public Map <String,?> getAll() {
		return preferences.getAll();
	}

}
 
