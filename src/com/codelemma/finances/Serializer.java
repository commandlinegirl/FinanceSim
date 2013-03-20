package com.codelemma.finances;

import java.util.ArrayList;
import java.util.Arrays;

public final class Serializer {

	private Serializer() {
	}
	
	public static String stringifyList(String[] list) {
        StringBuilder result = new StringBuilder();
        for(String value: list) {
        	//TODO: check for ";" in s (exception)
            result.append(value).append(";");
        }
    	return result.toString();
    }
	
    public static ArrayList<String> parseToList(String string) {
    	ArrayList<String> arrayStringList = new ArrayList<String>();
    	ArrayList<String> arrayBigDecimalList = new ArrayList<String>();
    	if (string != null) {
            String[] array = string.split(";");
    		//String[] array = string.split(",");
            arrayStringList = new ArrayList<String>(Arrays.asList(array));
            for(String stringValue: arrayStringList) {
            	arrayBigDecimalList.add(stringValue);
            }            
    	}
        return arrayBigDecimalList;
    }
    
    public static String stringifyMap(TypedContainer container) {    
    	return container.toString();
    }
              
    public static TypedContainer parseToMap(String string) throws ParseException {
    	return new TypedContainer(string);
    }
}
