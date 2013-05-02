package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;

public class DebtMortgage extends Debt {
 
    private BigDecimal purchase_price;
    private BigDecimal init_interest_rate;
    private BigDecimal interest_rate_decimal_monthly;
    private int term; // in months
    private int term_months; // in months
    private BigDecimal downpayment;
    private BigDecimal base_monthly_payment;
    private BigDecimal monthly_payment;
    private BigDecimal property_insurance;
    private BigDecimal property_tax;
    private BigDecimal pmi;
    private BigDecimal pmi_amount;    
    private BigDecimal insurance_amount;  
    private BigDecimal tax_amount;  
    private BigDecimal additional_cost = Money.ZERO; //tax & insurance
    private BigDecimal additional_cost_with_pmi; 
    private BigDecimal additional_cost_without_pmi; 
    private BigDecimal total_additional_cost = Money.ZERO;
  
    private BigDecimal principal_paid = Money.ZERO;
    private BigDecimal interests_paid = Money.ZERO;
    private BigDecimal total_interests = Money.ZERO;
    private BigDecimal total_principal = Money.ZERO;
    
    private BigDecimal property_insurance_decimal_monthly;
    private BigDecimal property_tax_decimal_monthly;
    private BigDecimal pmi_decimal_monthly;
    
    private BigDecimal outstanding_loan;   
    private BigDecimal loan_amount;
    private int month_counter = 1;
    private String name;
       
    private HistoryDebtMortgage history;
    
    private int start_year;
    private int start_month;
    
    private DebtMortgage(String name, 
    		BigDecimal purchase_price,
    		BigDecimal downpayment,
    		BigDecimal interest_rate,
    		int term,
    		BigDecimal property_insurance,
    		BigDecimal property_tax,
    		BigDecimal pmi,
            int start_year,
    	    int start_month) {
    	this.name = name;
    	this.purchase_price = purchase_price;
    	this.downpayment = Money.scale(downpayment);
    	this.loan_amount = Money.scale(purchase_price.subtract(downpayment));
    	this.outstanding_loan = loan_amount; //TODO check if not < 0!
    	
    	this.init_interest_rate = interest_rate;  
    	this.interest_rate_decimal_monthly = interest_rate.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	
    	this.property_insurance = property_insurance;
    	this.property_insurance_decimal_monthly = property_insurance.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	this.insurance_amount = Money.getPercentage(purchase_price, property_insurance_decimal_monthly);
    	
    	this.property_tax = property_tax;
    	this.property_tax_decimal_monthly = property_tax.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	this.tax_amount = Money.getPercentage(purchase_price, property_tax_decimal_monthly);
    	
    	this.pmi = pmi;
    	this.pmi_decimal_monthly = pmi.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	this.pmi_amount = Money.getPercentage(loan_amount,  pmi_decimal_monthly);
    	
    	this.term = term; 
    	this.term_months = term * 12; 

    	this.additional_cost_without_pmi = insurance_amount.add(tax_amount);
    	this.additional_cost_with_pmi = insurance_amount.add(tax_amount).add(pmi_amount);
    	    	
    	this.base_monthly_payment = calculateMonthlyPayment();
    	this.monthly_payment = base_monthly_payment;
    	
    	this.start_year = start_year;
    	this.start_month = start_month;
    	history = new HistoryDebtMortgage(this);    	
    }
    
    public static DebtMortgage create(String name, 
    		BigDecimal purchase_price,
    		BigDecimal downpayment,
    		BigDecimal interest_rate,
    		int term,
    		BigDecimal property_insurance,
    		BigDecimal property_tax,
    		BigDecimal pmi,
            int start_year,
    	    int start_month) {
    	return new DebtMortgage(name, 
    			 purchase_price,
	    		 downpayment,
	    		 interest_rate,
	    		 term,
	    		 property_insurance,
	    		 property_tax,
	    		 pmi,
		         start_year,
		    	 start_month);
    }
    
    private BigDecimal calculateMonthlyPayment() {
    	BigDecimal factor = (interest_rate_decimal_monthly.add(Money.ONE)).pow(term_months);
    	BigDecimal factor_minus_one = factor.subtract(Money.ONE);
    	try {
    		factor.divide(factor_minus_one, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	} catch (IllegalArgumentException iae) {
    		iae.printStackTrace();
    	}
    	   	
    	BigDecimal monthly_payment = Money.scale(interest_rate_decimal_monthly.multiply(
    			loan_amount.multiply(factor.divide(factor_minus_one, Money.RATE_DECIMALS, Money.ROUNDING_MODE))));
    	return monthly_payment; 
    }
    
    @Override
    public void advance(int year, int month) {

	    if (year == start_year && month == start_month) {
	    	initialize();
		    advanceValues(month);
	    } else if ((year > start_year) || (year == start_year && month > start_month)) {
	    	advanceValues(month);
	    }       	    	
    }
    
   
    public void advanceValues(int month) {
    	if (month_counter <= term_months) {    		
                		
        	//Log.d("outstanding_loan", outstanding_loan.toString());
            interests_paid = Money.scale(outstanding_loan.multiply(interest_rate_decimal_monthly));
        	//Log.d("interests_paid", interests_paid.toString());

            total_interests = total_interests.add(interests_paid);
        	//Log.d("total_interests", total_interests.toString());
            
            principal_paid = base_monthly_payment.subtract(interests_paid);
        	//Log.d("principal_paid", principal_paid.toString());
            
            total_principal = total_principal.add(principal_paid);
        	//Log.d("total_principal", total_principal.toString());
            
            outstanding_loan = outstanding_loan.subtract(principal_paid);
        	//Log.d("outstanding_loan", outstanding_loan.toString());
           
        	
            /* If loan-to-value (LTV) ratio (the ratio of a loan to a value of an asset purchased)
             * is < 20%, add pmi to the monthly payment */       
        	
            BigDecimal ltv = (total_principal.add(downpayment)).divide(purchase_price, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        	
                		
            if (ltv.compareTo(new BigDecimal(0.2)) == -1) {
            	additional_cost = additional_cost_with_pmi;            	 
            	//Log.d("monthly_payment with pmi", monthly_payment.toString());

            } else {
            	additional_cost = additional_cost_without_pmi;
            	//Log.d("monthly_payment without pmi", monthly_payment.toString());            	
            }
            
        	monthly_payment = base_monthly_payment.add(additional_cost);    
        	total_additional_cost = total_additional_cost.add(additional_cost);            
            month_counter++;
    	} else {
    		monthly_payment = Money.ZERO;
    		interests_paid = Money.ZERO;
    		principal_paid = Money.ZERO;
    		additional_cost = Money.ZERO;
    		outstanding_loan = Money.ZERO;    	    		
    	}      
    }

	@Override
	public void initialize() {
		outstanding_loan = loan_amount;
		month_counter = 1;
    	monthly_payment = base_monthly_payment;
	    principal_paid = Money.ZERO;
	    interests_paid = Money.ZERO;
	    total_interests = Money.ZERO;
	    total_principal = Money.ZERO;
	    total_additional_cost = Money.ZERO;
	}
    
	@Override
	public void setValuesBeforeCalculation() {
		outstanding_loan = Money.ZERO;
		month_counter = 1;
    	base_monthly_payment = calculateMonthlyPayment();
    	monthly_payment = Money.ZERO;
	    principal_paid = Money.ZERO;
	    interests_paid = Money.ZERO;
	    total_interests = Money.ZERO;
	    total_principal = Money.ZERO;
	    total_additional_cost = Money.ZERO;
	}
	
	@Override
    public BigDecimal getMonthlyPayment() {
    	return monthly_payment;
    }
    
    public BigDecimal getInterestPaid() {
    	return interests_paid;
    }
    
    public BigDecimal getPrincipalPaid() {
        return principal_paid;
    }
    
    public BigDecimal getTotalInterestPaid() {
    	return total_interests;
    }
    
    
    public BigDecimal getAdditionalCost() {
    	return additional_cost;
    }
    
    public BigDecimal getTotalAdditionalCost() {
    	return total_additional_cost;
    }
    
    public BigDecimal getRemainingAmount() {
    	return outstanding_loan;
    }
    
    public int getTerm() {
    	return term;
    }
    
    public BigDecimal getPropertyInsurance() {
    	return property_insurance;
    }

    public BigDecimal getPropertyTax() {
    	return property_tax;
    }

    public BigDecimal getPMI() {
    	return pmi;
    }

    public BigDecimal getPropertyInsuranceAmount() {
    	return insurance_amount;
    }

    public BigDecimal getPropertyTaxAmount() {
    	return tax_amount;
    }

    public BigDecimal getPMIAmount() {
    	return pmi_amount;
    }

    public BigDecimal getTotalPayment() {
    	return total_additional_cost.add(loan_amount).add(total_interests);
    }

    
    public BigDecimal getDownpayment() {
    	return downpayment;
    }
    
    public BigDecimal getInterestRate() {
    	return init_interest_rate; //TODO:?
    }
    
    @Override
    public String getName() {
        return name;
    }  
    
    @Override
    public BigDecimal getValue() {
    	return purchase_price;
    }
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForDebtMortgage(this);
    }

	@Override
	public BigDecimal getAmount() {
		return monthly_payment;
	}
		
	public BigDecimal getLoanAmount() {
		return loan_amount;
	}

	@Override
	public HistoryDebtMortgage getHistory() {
		return history;
	}

	public BigDecimal getPurchasePrice() {
		return purchase_price;
	}

	@Override
	public BigDecimal getInitAmount() {
		return purchase_price;
	}

	@Override
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForDebtMortgage(this);
	}

	@Override
	public int getStartYear() {
		return start_year;
	}
	
	@Override
	public int getStartMonth() {
		return start_month;
	}
}