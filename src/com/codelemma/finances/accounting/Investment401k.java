package com.codelemma.finances.accounting;
import java.math.BigDecimal;


import android.util.Log;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class Investment401k extends Investment 
                            implements NamedValue {
	
	private int id;
	private String name;	
    private BigDecimal init_amount;
    private BigDecimal init_percontrib;  
    private BigDecimal init_interest_rate;  
    private BigDecimal init_payrise;  
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
    private BigDecimal hidden_interests = Money.ZERO;
    private HistoryInvestment401k history;    
    private Income income;
    private BigDecimal salary;

    int counter = 0;
    int period_months;
        
    private int start_year;
    private int start_month;
    
    public Investment401k(String name,
    		          BigDecimal init_amount,
    		          BigDecimal percontrib,
    		          int period,
    		          BigDecimal interest_rate,
    		          Income income,
    		          BigDecimal withdrawal_tax_rate,
    		          BigDecimal employer_match,
                      int _start_year,
              	      int _start_month) {
    	this.name = name;
        
    	this.init_amount = Money.scale(init_amount);
        this.amount = this.init_amount;
        
        this.init_percontrib =  percontrib;  
        this.init_interest_rate =  interest_rate;  
        this.init_withdrawal_tax_rate =  withdrawal_tax_rate;  
        this.init_employer_match =  employer_match;  
        
        this.period = period;
        this.period_months = period * 12;
        
        this.income = income;
		this.salary = income.getGrossIncome();
        
        this.percontrib = Money.scaleRate(percontrib);  
        this.percontrib_decimal = percontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE); 
        this.monthly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);

        
        this.employer_match = Money.scaleRate(employer_match);
        this.employer_match_decimal = employer_match.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        this.monthly_employer_contribution = Money.getPercentage(monthly_employee_contribution, employer_match_decimal);
        
        this.interest_rate = Money.scaleRate(interest_rate);
        this.interest_rate_decimal = interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);  
        this.interest_rate_decimal_monthly = interest_rate_decimal.divide(new BigDecimal(12), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
                       

        this.withdrawal_tax_rate = Money.scaleRate(withdrawal_tax_rate);
        this.withdrawal_tax_rate_decimal = withdrawal_tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);   
        
        history = new HistoryInvestment401k(this);
    	start_year = _start_year;
    	start_month = _start_month;
    	capital_gain = Money.ZERO;
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
	}
	
	public int getInterestPay() {
		return interest_pay;
	}

	public BigDecimal getInitInterestRate() {
		return init_interest_rate;
	}
		
	public BigDecimal getInitPayrise() {
		return init_payrise;
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
    public int getId() {
        return id;
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
    public void advance(int year, int month, BigDecimal excess_null_value) {

        //interests_gross = new BigDecimal(0);
        //tax_on_interests = new BigDecimal(0);
        //interests_net = new BigDecimal(0);

    	if ((year < start_year) || (year == start_year && month < start_month)) {
    		setValuesBeforeCalculation();    		
    	}        
    	   	
    	if (year == start_year && month == start_month) {
    		initialize();    		
    		advanceValues(month);    		
    	}      	
    	
    	if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(month);
    	}       	
    }
    
    /* event methods*/
    private void setValuesBeforeCalculation() {
		amount = Money.ZERO;
		salary = Money.ZERO;	
		hidden_interests = Money.ZERO;
		monthly_employee_contribution = Money.ZERO;
		monthly_employer_contribution = Money.ZERO;
    }    
    
    private void advanceValues(int month) {	   	
    	salary = income.getGrossIncome();

    	/* Calculate monthly contribution of employee and employer */    		
    	monthly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);
    	monthly_employer_contribution = Money.getPercentage(monthly_employee_contribution, employer_match_decimal);   	    	
    	amount = amount.add(monthly_employee_contribution);
    	amount = amount.add(monthly_employer_contribution);
    	BigDecimal interests_gross = Money.getPercentage(amount, interest_rate_decimal_monthly);
        amount = amount.add(interests_gross);
        hidden_interests = hidden_interests.add(interests_gross);
    	if (counter == period_months) {
            // withdraw money to savings acct and pay tax
    		BigDecimal tax = getInterestsTax(hidden_interests);
    		capital_gain = calculateInterestsNet(hidden_interests, tax);    		
    	} else {
    	    counter++;
    	}
    }
        
	public BigDecimal getAccumulatedSavings() {
		return amount;
	}
    
	@Override
	public void initialize() {
		amount = Money.scale(init_amount);
		salary = income.getGrossIncome();	
		hidden_interests = Money.ZERO;
		counter = 0;
				
        monthly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);

        employer_match = Money.scaleRate(employer_match);
        employer_match_decimal = employer_match.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        monthly_employer_contribution = Money.getPercentage(monthly_employee_contribution, employer_match_decimal);
	}

	@Override
	public void setId(int id) {
		this.id = id;		
	}
	
	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packInvestment401k(this);		
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
