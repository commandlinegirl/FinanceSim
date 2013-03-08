package com.codelemma.finances;

public class ParseException extends Exception {

	private static final long serialVersionUID = 3966315019811411402L;

	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(Throwable cause) {
		super(cause);
	}
}
