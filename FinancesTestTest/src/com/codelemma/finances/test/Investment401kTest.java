package com.codelemma.finances.test;

import java.math.BigDecimal;

import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;

import junit.framework.Assert;



import android.test.AndroidTestCase;
import android.util.Log;

public class Investment401kTest extends AndroidTestCase {

	public Investment401kTest() {
		super();
	}
	
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
	}
	
	public void testInvestmen401kForJanuary() {
		String name = "401k 1";
        BigDecimal init_amount = new BigDecimal("5000");
        BigDecimal interest_rate = new BigDecimal("8");
        BigDecimal percontrib = new BigDecimal("10");
        int period = 10;
        BigDecimal salary = new BigDecimal("50000");
        BigDecimal payrise = new BigDecimal("5");
        BigDecimal withdrawal_tax_rate = new BigDecimal("30");
        BigDecimal employer_match = new BigDecimal("50");
        int start_year = 2013;
        int start_month = 2;

        IncomeGeneric income = new IncomeGeneric(
        		new BigDecimal("10000"),
        		new BigDecimal("16"),
        		new BigDecimal("5"),
        		new BigDecimal("12"),
        		"Salary",
        		2,
        		2013);
        
        
        		
    	Investment401k inv = new Investment401k(name,
    			init_amount,
                percontrib,
                period,
                interest_rate, 
                income,
                withdrawal_tax_rate,
                employer_match,
			       start_year,
			       start_month); 
    
	    inv.advance(2013, 0, null);
	    
	    Log.d("inv.getAmount()", inv.getAmount().toString());
	    Log.d("inv.getSalary()", inv.getSalary().toString());
	    Log.d("inv.getEmployeeContribution()", inv.getEmployeeContribution().toString());
	    Log.d("inv.getEmployerContribution()", inv.getEmployerContribution().toString());
	    	    	    
	    
	    Assert.assertTrue(inv.getAmount().compareTo(new BigDecimal("5693.96")) == 0);
	    Assert.assertTrue(inv.getSalary().compareTo(new BigDecimal("52500")) == 0);
	    Assert.assertTrue(inv.getEmployeeContribution().compareTo(new BigDecimal("437.50")) == 0);
	    Assert.assertTrue(inv.getEmployerContribution().compareTo(new BigDecimal("218.75")) == 0);	    
	}

	public void testInvestmen401kForNotJanuary() {
		String name = "401k 2";
        BigDecimal init_amount = new BigDecimal("5000");
        BigDecimal interest_rate = new BigDecimal("8");
        BigDecimal percontrib = new BigDecimal("10");
        int period = 10;
        BigDecimal salary = new BigDecimal("50000");
        BigDecimal payrise = new BigDecimal("5");
        BigDecimal withdrawal_tax_rate = new BigDecimal("30");
        BigDecimal employer_match = new BigDecimal("50");
        int start_year = 2013;
        int start_month = 2;

        IncomeGeneric income = new IncomeGeneric(
        		new BigDecimal("10000"),
        		new BigDecimal("16"),
        		new BigDecimal("5"),
        		new BigDecimal("12"),
        		"Salary",
        		2,
        		2013);
        
        
    	Investment401k inv = new Investment401k(name,
    			init_amount,
                percontrib,
                period,
                interest_rate, 
                income,
                withdrawal_tax_rate,
                employer_match,
			       start_year,
			       start_month); 
    
	    inv.advance(2013, 1, null);
	    
	    Log.d("inv.getAmount()", inv.getAmount().toString());
	    Log.d("inv.getSalary()", inv.getSalary().toString());
	    Log.d("inv.getEmployeeContribution()", inv.getEmployeeContribution().toString());
	    Log.d("inv.getEmployerContribution()", inv.getEmployerContribution().toString());
	    	    	    
	    Assert.assertTrue(inv.getAmount().compareTo(new BigDecimal("5662.50")) == 0);
	    Assert.assertTrue(inv.getSalary().compareTo(new BigDecimal("50000")) == 0);
	    Assert.assertTrue(inv.getEmployeeContribution().compareTo(new BigDecimal("416.67")) == 0);
	    Assert.assertTrue(inv.getEmployerContribution().compareTo(new BigDecimal("208.33")) == 0);	    
	}
}