package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;

public class DebtLoan extends Debt {
	
    private BigDecimal init_amount;
    private BigDecimal init_interest_rate;
    private BigDecimal interest_rate_decimal_monthly;
    private int term; // in months
    private int term_months; // in months
    private BigDecimal extra_payment;
    private BigDecimal monthly_payment;
    private BigDecimal principal_paid = Money.ZERO;
    private BigDecimal interests_paid = Money.ZERO;
    private BigDecimal total_interests = Money.ZERO;
    private BigDecimal remaining_amount;   
    private int month_counter = 1;
    private String name;
    private int start_year;
    private int start_month;
    private HistoryDebtLoan history;
    
    private DebtLoan(String name, 
    		BigDecimal amount,
    		BigDecimal interest_rate,
    		int term,
    		BigDecimal extra_payment,
    		int start_year,
    		int start_month) {
    	this.name = name;
    	this.init_amount = amount;
    	this.remaining_amount = Money.scale(amount);
    	this.init_interest_rate = interest_rate;  
    	this.interest_rate_decimal_monthly = interest_rate.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	this.extra_payment = Money.scale(extra_payment);
    	
    	this.term = term; 
    	this.term_months = term * 12; 
    	
    	this.start_year = start_year;
    	this.start_month = start_month;
    	
    	monthly_payment = calculateMonthlyPayment();
    	history = new HistoryDebtLoan(this);
    	setValuesBeforeCalculation();
    }
    
    public static DebtLoan create(
    		String name,
    		BigDecimal amount,
    		BigDecimal interest_rate,
    		int term,
    		BigDecimal extra_payment,
    		int start_year,
    		int start_month) {
       	Preconditions.checkInBounds(interest_rate, BigDecimal.ZERO, 
       			Money.HUNDRED, "Debt loan interest rate must be in 0..100");
    	Preconditions.checkInBounds(term, 0, 
    			100, "Debt loan term must be higher than 0 and lower than 100");
    	return new DebtLoan(name,
    			amount,
    			interest_rate,
    			term, 
    			extra_payment,
    			start_year,
    			start_month);
    }
    
    public void setInitAmount() {
    	remaining_amount = init_amount;
    }
    
    public BigDecimal calculateMonthlyPayment() {
    	BigDecimal factor = (interest_rate_decimal_monthly.add(Money.ONE)).pow(term_months);
    	BigDecimal factor_minus_one = factor.subtract(Money.ONE);
    	try {
    		factor.divide(factor_minus_one, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	} catch (IllegalArgumentException iae) {
    		iae.printStackTrace();
    	}
    	BigDecimal monthly_payment = Money.scale(interest_rate_decimal_monthly.multiply(
    			init_amount.multiply(factor.divide(factor_minus_one, Money.RATE_DECIMALS, Money.ROUNDING_MODE)))
    			.add(extra_payment));
    	return monthly_payment; 
    }

    /* event methods*/
    public void setValuesBeforeCalculation() {
		monthly_payment = Money.ZERO;
		interests_paid = Money.ZERO;
		principal_paid = Money.ZERO;
		total_interests = Money.ZERO;
		remaining_amount = Money.ZERO;
    }

    private void setValuesAfterCalculation() {
		monthly_payment = Money.ZERO;
		interests_paid = Money.ZERO;
		principal_paid = Money.ZERO;
		remaining_amount = Money.ZERO;    
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

    private void advanceValues(int month) {
    	if (month_counter <= term_months && remaining_amount.compareTo(Money.ZERO) == 1) {
		    interests_paid = Money.scale(remaining_amount.multiply(interest_rate_decimal_monthly));
        
            if (remaining_amount.compareTo(monthly_payment) == -1) {
            	monthly_payment = remaining_amount.add(interests_paid);
            }
          
            principal_paid = monthly_payment.subtract(interests_paid);
            total_interests = total_interests.add(interests_paid);
            remaining_amount = remaining_amount.subtract(principal_paid);
		    month_counter++;
        } else {
        	setValuesAfterCalculation();
        }
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
    
    public BigDecimal getRemainingAmount() {
    	return remaining_amount;
    }
    
    public int getTerm() {
    	return term;
    }

    public BigDecimal getInterestRate() {
    	return init_interest_rate;
    }

    public BigDecimal getExtraPayment() {
    	return extra_payment;
    }
          
    @Override
    public String getName() {
        return name;
    }  
    
    @Override
    public BigDecimal getValue() {
    	return init_amount;
    }
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForDebtLoan(this);
    }

	@Override
	public BigDecimal getAmount() {
		return monthly_payment;
	}

	@Override
	public HistoryDebtLoan getHistory() {
		return history;
	}

	@Override
	public BigDecimal getInitAmount() {
		return init_amount;
	}

	@Override
	public void initialize() {
		remaining_amount = init_amount;
		monthly_payment = calculateMonthlyPayment();
		month_counter = 1;
	    principal_paid = Money.ZERO;
	    interests_paid = Money.ZERO;
	    total_interests = Money.ZERO;
	}

	@Override
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForDebtLoan(this);				
		
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
