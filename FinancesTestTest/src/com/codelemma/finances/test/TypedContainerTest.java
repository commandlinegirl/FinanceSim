package com.codelemma.finances.test;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;

import junit.framework.Assert;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.Serializer;
import com.codelemma.finances.TypedContainer;
import com.codelemma.finances.TypedKey;

import android.test.AndroidTestCase;

public class TypedContainerTest extends AndroidTestCase {

	public TypedContainerTest() {
		super();
	}
	
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	public void testTypedContainer() throws ParseException {
        String sample_income = "{1={yearly_income=3.00;income_installments=6.00;income_tax_rate=5.0000000;income_name=Dary;yearly_income_rise=4.0000000;};0={yearly_income=100000.00;income_installments=12.00;income_tax_rate=16.0000000;income_name=Salary;yearly_income_rise=5.0000000;};}";
//        Log.d("typedContainer.toString()", tc.toString());
        
        TypedContainer incomesCont = Serializer.parseToMap(sample_income);		
		Iterator<Entry<TypedKey<?>, Object>> i = incomesCont.iterator();		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());       
            String yearly_income = tc.get(TypedKey.YEARLY_INCOME).toString();
            String income_tax_rate = tc.get(TypedKey.INCOME_TAX_RATE).toString();        
            String yearly_income_rise = tc.get(TypedKey.YEARLY_INCOME_RISE).toString();
            String income_installments = tc.get(TypedKey.INCOME_INSTALLMENTS).toString();
	        String income_name = tc.get(TypedKey.INCOME_NAME).toString();
        
	        if (income_name.equals("Dary")) {
                Assert.assertTrue(yearly_income.equals("3.00"));
                Assert.assertTrue(income_tax_rate.equals("5.0000000"));
                Assert.assertTrue(yearly_income_rise.equals("4.0000000"));
                Assert.assertTrue(income_installments.equals("6.00"));
                Assert.assertTrue(income_name.equals("Dary"));
	        } else if (income_name.equals("Salary")) {
                Assert.assertTrue(yearly_income.equals("100000.00"));
                Assert.assertTrue(income_tax_rate.equals("16.0000000"));
                Assert.assertTrue(yearly_income_rise.equals("5.0000000"));
                Assert.assertTrue(income_installments.equals("12.00"));
                Assert.assertTrue(income_name.equals("Salary"));	        	
	        }
		}
	}
	
	
	public void testTypedContainerSettingOneByOne() throws ParseException { 
		TypedContainer container = new TypedContainer();
		container.put(TypedKey.INVESTMENTSAV_NAME, "Test_name");    	
	    container.put(TypedKey.INVESTMENTSAV_INIT_AMOUNT,  new BigDecimal("1000"));
	    container.put(TypedKey.INVESTMENTSAV_TAX_RATE, new BigDecimal("20.00"));
	    container.put(TypedKey.INVESTMENTSAV_PERCONTRIB, new BigDecimal("10"));
	    container.put(TypedKey.INVESTMENTSAV_CAPITALIZATION, 1);
	    container.put(TypedKey.INVESTMENTSAV_INTEREST_RATE, new BigDecimal("5.25"));
	    
	    String cont_str = container.toString();	    
	    TypedContainer c_parsed = new TypedContainer(cont_str);
	    	    	   
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_NAME).equals("Test_name"));
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_INIT_AMOUNT).toString().equals("1000"));
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_TAX_RATE).toString().equals("20.00"));
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_PERCONTRIB).toString().equals("10"));
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_CAPITALIZATION).toString().equals("1"));
	    Assert.assertTrue(c_parsed.get(TypedKey.INVESTMENTSAV_INTEREST_RATE).toString().equals("5.25"));
	}	
}