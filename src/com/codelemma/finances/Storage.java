package com.codelemma.finances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.codelemma.finances.accounting.Account;

public interface Storage {
	void saveAccount(Account account) throws ParseException;	
	void saveInput(HashMap<String,String> input);
	HashMap<String,String> getInput(ArrayList<String> keys);
	HashMap<String,String> getInputMap(String key);		
	String get(String key, String defaultVal);
	void put(String key, String val);
	void remove(String key);
	void clear();
	Map <String,?> getAll();
}
