package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal ONE = new BigDecimal(1);
    

    // Number of decimals to retain, ie. "scale".
    public static final int DECIMALS = 2;
    public static final int RATE_DECIMALS = 10;
    private static final int ZERO = 0;
    

    public static BigDecimal getPercentage(BigDecimal base, BigDecimal perc) {
        return scale(base.multiply(perc)); 
    }

    public static BigDecimal scale(BigDecimal num){
        return num.setScale(DECIMALS, ROUNDING_MODE);
    }

    public static BigDecimal scaleZero(BigDecimal num){
        return num.setScale(ZERO, ROUNDING_MODE);
    }    
    
    public static BigDecimal scaleRate(BigDecimal num){
        return num.setScale(RATE_DECIMALS, ROUNDING_MODE);
    }
}

