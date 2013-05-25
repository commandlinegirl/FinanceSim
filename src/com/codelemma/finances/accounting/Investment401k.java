package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import android.util.Log;

import com.codelemma.finances.InputListingUpdater;

public class Investment401k extends Investment {
	
	private String name;	
    private BigDecimal init_amount;
    private BigDecimal init_percontrib;  
    private BigDecimal init_interest_rate;  
    private BigDecimal init_withdrawal_tax_rate;  
    private BigDecimal init_employer_match;  
    private BigDecimal percontrib; // percentage of salary
    private BigDecimal percontrib_decimal; // percentage of salary
    private int period;
    private BigDecimal capital_gain;
    private BigDecimal interest_rate;
    private BigDecimal interest_rate_decimal;
    private BigDecimal interest_rate_decimal_monthly;     
    private int interest_pay;
    private BigDecimal amount;
    private BigDecimal monthly_employee_contribution;
    private BigDecimal monthly_employer_contribution;
    private BigDecimal withdrawal_tax_rate;
    private BigDecimal withdrawal_tax_rate_decimal;
    private BigDecimal employer_match;
    private BigDecimal employer_match_decimal;
    private BigDecimal hidden_interests;
    private HistoryInvestment401k history;    
    private Income income;
    private int income_previous_id;
    private BigDecimal salary;
    
    private int counter = 0;
    private int period_months;
        
    private int start_year;
    private int start_month;
    
    private Investment401k(String name,
    		          BigDecimal init_amount,
    		          BigDecimal percontrib,
    		          int period,
    		          BigDecimal interest_rate,
    		          Income income,
    		          BigDecimal withdrawal_tax_rate,
    		          BigDecimal employer_match,
                      int start_year,
              	      int start_month) {
    	this.name = name;
        
    	this.init_amount = init_amount;
        this.amount = Money.scale(this.init_amount);
        
        this.init_percontrib =  percontrib;  
        this.init_interest_rate =  interest_rate;  
        this.init_withdrawal_tax_rate =  withdrawal_tax_rate;  
        this.init_employer_match =  employer_match;  
        
        this.period = period;
        this.period_months = period * 12;
        
        this.income = income;
        
        this.percontrib = Money.scaleRate(percontrib);  
        this.percontrib_decimal = percontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE); 
        
        this.employer_match = Money.scaleRate(employer_match);
        this.employer_match_decimal = employer_match.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        
        this.interest_rate = Money.scaleRate(interest_rate);
        this.interest_rate_decimal = interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);  
        this.interest_rate_decimal_monthly = interest_rate_decimal.divide(new BigDecimal(12), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
                       
        this.withdrawal_tax_rate = Money.scaleRate(withdrawal_tax_rate);
        this.withdrawal_tax_rate_decimal = withdrawal_tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);   
        
    	this.start_year = start_year;
    	this.start_month = start_month;
    	capital_gain = Money.ZERO;
        history = new HistoryInvestment401k(this);
    	setValuesBeforeCalculation();
    }   

	public static Investment401k create(String name,
	          BigDecimal init_amount,
	          BigDecimal percontrib,
	          int period,
	          BigDecimal interest_rate,
	          Income income,
	          BigDecimal withdrawal_tax_rate,
	          BigDecimal employer_match,
              int start_year,
  	          int start_month) {
		return new Investment401k(
				name,
				init_amount,
	            percontrib,
	            period,
	            interest_rate, 
	            income,
	            withdrawal_tax_rate,
	            employer_match,
		        start_year,
		    	start_month);
	}
	
	@Override
    public boolean isPreTax() {
    	return true;
    }
    
	@Override
    public boolean isCheckingAcct() {
    	return false;
    }
	
	@Override
	public BigDecimal getInterestsNet() {
		return capital_gain;
	}
	
	public BigDecimal getInterestRate() {
		return interest_rate;
	}

	public Income getIncome() {
		return income;
	}

	public void setIncome(Income inc) {
		income = inc;
		salary = inc.getGrossIncome();
		income_previous_id = inc.getId();
	}

	public void setIncomePreviousId(int id) {
		income_previous_id = id;
	}

	@Override
	public int getStoredIncomePreviousId() {
		return income_previous_id;
	}	

	public int getInterestPay() {
		return interest_pay;
	}

	public BigDecimal getInitInterestRate() {
		return init_interest_rate;
	}
		
	public BigDecimal getSalary() {
		return salary;
	}
	
	public BigDecimal getInitPercontrib() {
		return init_percontrib;
	}
	
	public BigDecimal getInitWithdrawalTaxRate() {
		return init_withdrawal_tax_rate;
	}
	
	public BigDecimal getInitEmployerMatch() {
		return init_employer_match;
	}
	
	public BigDecimal getEmployerMatch() {
	    return employer_match;
	}
	
	public BigDecimal getWithdrawalTaxRate() {
		return withdrawal_tax_rate;
	}
		
	public int getPeriod() {
		return period;
	}
	                            
    public BigDecimal getInterestsTax(BigDecimal interests_gross) {        
    	return Money.getPercentage(interests_gross, withdrawal_tax_rate_decimal);
    }    
    
    public BigDecimal calculateInterestsNet(BigDecimal interests_gross, 
    		                          BigDecimal interests_gross_tax) {
    	return interests_gross.subtract(interests_gross_tax);
    }      
      
	@Override
	public BigDecimal getInitAmount() {
		return init_amount;
	}
	
	@Override
	public BigDecimal getAmount() {
		return amount;
	}
	
	@Override
	public BigDecimal getPercontrib() {
		return percontrib;
	}
	
	@Override
	public BigDecimal getEmployeeContribution() {
		return monthly_employee_contribution;
	}
	
	public BigDecimal getEmployerContribution() {
		return monthly_employer_contribution;
	}
	
    @Override
    public String getName() {
        return name;
    }    
    
    @Override
    public BigDecimal getValue() {
        return init_amount; // init_amount or amount
    }   

    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForInvestment401k(this);
    }
	          
    @Override
    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {

    	if (year == start_year && month == start_month) { 		
    		initialize();    		
    		advanceValues(month, checkingAcct);    		
    	} else if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(month, checkingAcct);
    	}       	
    }
    
    @Override
    public void setValuesBeforeCalculation() {
		amount = Money.ZERO;
		salary = Money.ZERO;	
		hidden_interests = Money.ZERO;
		monthly_employee_contribution = Money.ZERO;
		monthly_employer_contribution = Money.ZERO;
    }    
    
    private void advanceValues(int month, InvestmentCheckAcct checkingAcct) {	   
    	if (counter < period_months) {
    		salary = income.getGrossIncome();
        	/* Calculate monthly contribution of employee and employer */    		
        	monthly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);
        	monthly_employer_contribution = Money.getPercentage(monthly_employee_contribution, employer_match_decimal);   	    	
        	amount = amount.add(monthly_employee_contribution).add(monthly_employer_contribution);        	
        	BigDecimal interests_gross = Money.getPercentage(amount, interest_rate_decimal_monthly);
            amount = amount.add(interests_gross);
            hidden_interests = hidden_interests.add(interests_gross);
            counter++;
        } else if (counter == period_months) {
            // withdraw money to savings acct and pay tax
    		BigDecimal tax = getInterestsTax(hidden_interests);
    		capital_gain = calculateInterestsNet(hidden_interests, tax);    	
    		amount = amount.subtract(tax); // pay tax for the interests gained
    		checkingAcct.add401kWithdrawal(amount); // add net 401(k) gathered amount to checking account
    		setValuesBeforeCalculation();
    		counter++;
        }
    }
        
	public BigDecimal getAccumulatedSavings() {
		return amount;
	}
    
	@Override
	public void initialize() {
		Log.d("444 Investment401k.initialize called", "called");
		amount = Money.scale(init_amount);
		salary = income.getGrossIncome();
		hidden_interests = Money.ZERO;
		counter = 0;	
        monthly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);
        employer_match = Money.scaleRate(employer_match);
        employer_match_decimal = employer_match.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        monthly_employer_contribution = Money.getPercentage(monthly_employee_contribution, employer_match_decimal);
	}

	public HistoryInvestment401k getHistory() {
		return history;
	}
	
	@Override
	public int getStartYear() {
		return start_year;
	}
	
	@Override
	public int getStartMonth() {
		return start_month;
	}
	
	@Override
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForInvestment401k(this);				
	}
}
