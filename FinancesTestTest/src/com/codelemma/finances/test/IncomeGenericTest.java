package com.codelemma.finances.test;

import java.math.BigDecimal;

import com.codelemma.finances.accounting.IncomeGeneric;

import android.test.AndroidTestCase;

public class IncomeGenericTest extends AndroidTestCase {

	public IncomeGenericTest() {
		super();
	}
		
	public void testInvestmen401kForJanuary() {
		String name = "Salary";
        BigDecimal init_income = new BigDecimal("5000");
        BigDecimal tax_rate = new BigDecimal("8");
        BigDecimal rise_rate = new BigDecimal("10");
        
        BigDecimal installments = new BigDecimal("50000");
        int start_year = 2013;
        int start_month = 2;

        		
    	IncomeGeneric inv = new IncomeGeneric(init_income,
    			tax_rate,
                rise_rate,
                installments,
                name,
			       start_year,
			       start_month); 
	    inv.advance(0);
  
	}

}