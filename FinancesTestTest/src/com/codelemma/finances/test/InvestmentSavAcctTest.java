package com.codelemma.finances.test;

import java.math.BigDecimal;

import junit.framework.Assert;

import com.codelemma.finances.accounting.InvestmentSavAcct;

import android.test.AndroidTestCase;
import android.util.Log;

public class InvestmentSavAcctTest extends AndroidTestCase {

	public InvestmentSavAcctTest() {
		super();
	}
	
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	public void testInvestmentSavAcctCapitalization1() {
		String name = "Savings account1";
        BigDecimal init_amount = new BigDecimal("5029.85");
        BigDecimal interest_rate = new BigDecimal("3.5");
        BigDecimal tax_rate = new BigDecimal("19");
        BigDecimal percontrib = new BigDecimal("0");
        int capitalization = 1;
        		
	    InvestmentSavAcct sav_acct = new InvestmentSavAcct(name,
		           init_amount,
		           tax_rate,
		           percontrib,
		           capitalization,
		           interest_rate);
	    
	    BigDecimal excess = new BigDecimal("0");	    
	    sav_acct.advance(0, excess);
	    
	    Log.d("sav_acct.getAmount()", sav_acct.getAmount().toString());
	    Log.d("sav_acct.getInterestsGross()", sav_acct.getInterestsGross().toString());
	    Log.d("sav_acct.getTax()", sav_acct.getTax().toString());
	    Log.d("sav_acct.getInterestsNet()", sav_acct.getInterestsNet().toString());
	    	    	    
	    Assert.assertTrue(sav_acct.getAmount().compareTo(new BigDecimal("5041.98")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsGross().compareTo(new BigDecimal("14.97")) == 0);
	    Assert.assertTrue(sav_acct.getTax().compareTo(new BigDecimal("2.84")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsNet().compareTo(new BigDecimal("12.13")) == 0);
	    
	}
	
	
	public void testInvestmentSavAcctCapitalization2() {
		String name = "Savings account2";
        BigDecimal init_amount = new BigDecimal("5029.85");
        BigDecimal interest_rate = new BigDecimal("3.5");
        BigDecimal tax_rate = new BigDecimal("19");
        BigDecimal percontrib = new BigDecimal("0");
        int capitalization = 2;
        		
	    InvestmentSavAcct sav_acct = new InvestmentSavAcct(name,
		           init_amount,
		           tax_rate,
		           percontrib,
		           capitalization,
		           interest_rate);
	    
	    BigDecimal excess = new BigDecimal("0");	    
	    sav_acct.advance(0, excess);
	    
	    Log.d("sav_acct.getAmount()", sav_acct.getAmount().toString());
	    Log.d("sav_acct.getInterestsGross()", sav_acct.getInterestsGross().toString());
	    Log.d("sav_acct.getTax()", sav_acct.getTax().toString());
	    Log.d("sav_acct.getInterestsNet()", sav_acct.getInterestsNet().toString());
	    	    	    
	    Assert.assertTrue(sav_acct.getAmount().compareTo(new BigDecimal("5029.85")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsGross().compareTo(new BigDecimal("0")) == 0);
	    Assert.assertTrue(sav_acct.getTax().compareTo(new BigDecimal("0")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsNet().compareTo(new BigDecimal("0")) == 0);
	    	    
	    sav_acct.advance(1, excess);	 
	    
	    Log.d("sav_acct.getAmount()", sav_acct.getAmount().toString());
	    Log.d("sav_acct.getInterestsGross()", sav_acct.getInterestsGross().toString());
	    Log.d("sav_acct.getTax()", sav_acct.getTax().toString());
	    Log.d("sav_acct.getInterestsNet()", sav_acct.getInterestsNet().toString());
	    	    	    
	    Assert.assertTrue(sav_acct.getAmount().compareTo(new BigDecimal("5052.96")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsGross().compareTo(new BigDecimal("28.53")) == 0);
	    Assert.assertTrue(sav_acct.getTax().compareTo(new BigDecimal("5.42")) == 0);
	    Assert.assertTrue(sav_acct.getInterestsNet().compareTo(new BigDecimal("23.11")) == 0);	    	    
	}	
}
