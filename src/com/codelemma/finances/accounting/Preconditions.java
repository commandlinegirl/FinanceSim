package com.codelemma.finances.accounting;
import java.lang.IllegalArgumentException;
import java.math.BigDecimal;

public class Preconditions {
    // Utility only. Disallow construction
    private Preconditions() {}

    public static void check(Boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkInBounds(int value, int min, int max, String message) {
        check((min <= value) && (value <= max), message);
    }

    public static void checkInBounds(BigDecimal value, BigDecimal min, BigDecimal max, String message) {
        check((value.compareTo(min) >= 0) && (value.compareTo(max) <= 0), message);
    }

    public static void checkNotNull(Object value, String message) {
        check(value != null, message);
    }
}
