package com.codelemma.finances;

public final class Serializer {

	private Serializer() {
	}
	              
    public static TypedContainer parseToMap(String string) throws ParseException {
    	/* Called in Finances to restore input values */
    	return new TypedContainer(string);
    }
}
