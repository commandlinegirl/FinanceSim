package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class DebtLoan extends Debt 
                         implements NamedValue {
	
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
    private int id;    
    private int start_year;
    private int start_month;
    
    private HistoryDebtLoan history;
    
    public DebtLoan(String _name, 
    		BigDecimal _amount,
    		BigDecimal _interest_rate,
    		int _term,
    		BigDecimal _extra_payment,
    		int _start_year,
    		int _start_month) {
    	name = _name;
    	init_amount = _amount;
    	remaining_amount = Money.scale(_amount);
    	init_interest_rate = _interest_rate;  
        interest_rate_decimal_monthly = _interest_rate.divide(new BigDecimal(1200), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
    	extra_payment = Money.scale(_extra_payment);
    	
    	term = _term; 
    	term_months = _term * 12; 
    	
    	start_year = _start_year;
    	start_month = _start_month;
    	
    	monthly_payment = calculateMonthlyPayment();
    	history = new HistoryDebtLoan(this);
    	setValuesBeforeCalculation();
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
          
    public void setId(int id) {
        this.id = id;
    }
    
 
   
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }  
    
    @Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packDebtLoan(this);		
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
