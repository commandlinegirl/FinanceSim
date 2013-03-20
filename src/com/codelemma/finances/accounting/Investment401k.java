package com.codelemma.finances.accounting;
import java.math.BigDecimal;


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
    private BigDecimal interest_rate;
    private BigDecimal interest_rate_decimal;
    private BigDecimal interest_rate_decimal_monthly;     
    private int interest_pay;
    private BigDecimal amount;
    private BigDecimal init_salary;
    private BigDecimal salary;
    private BigDecimal monthly_employee_contribution;
    private BigDecimal monthly_employer_contribution;
    private BigDecimal yearly_employee_contribution;
    private BigDecimal yearly_employer_contribution;
    private BigDecimal payrise;
    private BigDecimal withdrawal_tax_rate;
    private BigDecimal withdrawal_tax_rate_decimal;
    private BigDecimal employer_match;
    private BigDecimal employer_match_decimal;
    private BigDecimal hidden_interests = Money.scale(new BigDecimal(0));
    private HistoryInvestment401k history;

    private BigDecimal payrise_decimal;
    int counter = 0;
    int period_months;
        
    public Investment401k(String name,
    		          BigDecimal init_amount,
    		          BigDecimal percontrib,
    		          int period,
    		          BigDecimal interest_rate,
    		          BigDecimal salary,
    		          BigDecimal payrise,
    		          BigDecimal withdrawal_tax_rate,
    		          BigDecimal employer_match) {
    	this.name = name;
        
    	this.init_amount = Money.scale(init_amount);
        this.amount = this.init_amount;
        
        this.init_percontrib =  percontrib;  
        this.init_interest_rate =  interest_rate;  
        this.init_payrise =  payrise;  
        this.init_withdrawal_tax_rate =  withdrawal_tax_rate;  
        this.init_employer_match =  employer_match;  
        
        this.period = period;
        this.period_months = period * 12;
        
        this.interest_rate = Money.scaleRate(interest_rate);
        this.interest_rate_decimal = interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);  
        this.interest_rate_decimal_monthly = interest_rate_decimal.divide(new BigDecimal(12), Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        
        this.payrise = Money.scaleRate(payrise);
        this.payrise_decimal = payrise.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        this.salary = Money.scale(salary);
        this.init_salary = salary;
               
        this.percontrib = Money.scaleRate(percontrib);  
        this.percontrib_decimal = percontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE); 
        this.yearly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);
        this.monthly_employee_contribution = yearly_employee_contribution.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);

        this.employer_match = Money.scaleRate(employer_match);
        this.employer_match_decimal = employer_match.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        this.yearly_employer_contribution = Money.getPercentage(yearly_employee_contribution, employer_match_decimal);
        this.monthly_employer_contribution = yearly_employer_contribution.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
        
        this.withdrawal_tax_rate = Money.scaleRate(withdrawal_tax_rate);
        this.withdrawal_tax_rate_decimal = withdrawal_tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);   
        
        history = new HistoryInvestment401k(this);
    }   
       		
	public BigDecimal getInterestRate() {
		return interest_rate;
	}

	public int getInterestPay() {
		return interest_pay;
	}
 
	public BigDecimal getInitSalary() {
		return init_salary;
	}

	public BigDecimal getInitInterestRate() {
		return init_interest_rate;
	}
		
	public BigDecimal getInitPayrise() {
		return init_payrise;
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
	
	public BigDecimal getSalary() {
		return salary;
	}
	
	public BigDecimal getEmployerMatch() {
	    return employer_match;
	}
	
	public BigDecimal getWithdrawalTaxRate() {
		return withdrawal_tax_rate;
	}
	
	public BigDecimal getPayrise() {
		return payrise;
	}
	
	public int getPeriod() {
		return period;
	}
	                            
    public BigDecimal getInterestsTax(BigDecimal interests_gross) {        
    	return Money.getPercentage(interests_gross, withdrawal_tax_rate_decimal);
    }    
    
    public BigDecimal getInterestsNet(BigDecimal interests_gross, 
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
    public void advance(int month, BigDecimal excess_null_value) {
    	if (month == 0) {
    		/* Each January give a rise to employee */
    		BigDecimal rise = Money.getPercentage(salary, payrise_decimal);
    		salary = salary.add(rise);
    		/* Calculate monthly contribution of employee and employer */
    		yearly_employee_contribution = Money.getPercentage(salary, percontrib_decimal);
    		monthly_employee_contribution = yearly_employee_contribution.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
    		yearly_employer_contribution = Money.getPercentage(yearly_employee_contribution, employer_match_decimal);
    		monthly_employer_contribution = yearly_employer_contribution.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
    	}    	    	
    	amount = amount.add(monthly_employee_contribution);
    	amount = amount.add(monthly_employer_contribution);
    	BigDecimal interests_gross = Money.getPercentage(amount, interest_rate_decimal_monthly);
        amount = amount.add(interests_gross);
        hidden_interests = hidden_interests.add(interests_gross);
    	if (counter == period_months) {
            // withdraw money to savings acct and pay tax
    		BigDecimal tax = getInterestsTax(hidden_interests);
    		BigDecimal income_net = getInterestsNet(hidden_interests, tax);    		
    	} else {
    	    counter++;
    	}
    }
        
	@Override
	public void initialize() {
		amount = init_amount;
		salary = init_salary;		
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
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForInvestment401k(this);				
	}
}
