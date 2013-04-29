package com.codelemma.finances;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberFormatter {
	
	private NumberFormat numberFormat;
	
	public NumberFormatter() {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits( 2 );
		numberFormat.setMaximumFractionDigits( 2 );
	}
	
	public String formatNumber(BigDecimal num) {		
		return numberFormat.format(num);
	}
}
