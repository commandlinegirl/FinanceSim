package com.codelemma.finances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;

public interface Storage {
	void saveHistory(History history);
	void saveAccount(Account account) throws ParseException;	
	void saveInput(HashMap<String,String> input);
	HashMap<String,String> getInput(ArrayList<String> keys);
	HashMap<String,String> getInputMap(String key);		
	ArrayList<String> getNetIncomes();
	ArrayList<String> getNetInvestmentsGain();
	ArrayList<String> getExpenses();
	ArrayList<String> getDebts();
	ArrayList<String> getBalances();
	String get(String key, String defaultVal);
	void put(String key, String val);
	void remove(String key);
	void clear();
	void clearHistory();
	Map <String,?> getAll();
}