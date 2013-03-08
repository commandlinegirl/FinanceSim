package com.codelemma.finances;


public class Preconditions {
    // Utility only. Disallow construction
    private Preconditions() {}

    public static void check(Boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkContainerNotEmpty(TypedContainer container, String message) {
        check((container.size() > 0), message);
    }

    public static void checkNotNull(Object value, String message) {
        check(value != null, message);
    }
    
    public static void checkStringConstructor(Class<?> clazz) throws NoSuchMethodException {
        if (clazz.getDeclaredConstructor(String.class) == null) {
        	throw new RuntimeException("Constructor does not accept String");
        	//throw new NoSuchMethodException("Constructor does not accept String");
	    }
    }
}
