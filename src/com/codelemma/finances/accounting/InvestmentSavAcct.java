package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import java.util.NoSuchElementException;

public class InvestmentSavAcct extends Investment {
	
	private String name;	
    private BigDecimal init_amount;
    private BigDecimal init_tax_rate;
    private BigDecimal init_percontrib;
    private BigDecimal init_interest_rate;
    
    private BigDecimal tax_rate;    
    private BigDecimal tax_rate_decimal;     
    private BigDecimal percontrib; // percentage of excess money
    private BigDecimal percontrib_decimal;
    private BigDecimal contribution;
    
    private BigDecimal interest_rate;
    private BigDecimal amount;
    private BigDecimal hidden_amount;
    private BigDecimal interest_rate_decimal;
    
    private BigDecimal interests_gross = Money.ZERO;
    private BigDecimal tax_on_interests = Money.ZERO;
    private BigDecimal interests_net = Money.ZERO;
    
    private BigDecimal comp_factor_28;
    private BigDecimal comp_factor_30;
    private BigDecimal comp_factor_31;
    private int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private HistoryInvestmentSavAcct history;
    
    private int start_year;
    private int start_month;
    
    private InvestmentSavAcct(String name,
    		          BigDecimal init_amount,
    		          BigDecimal tax_rate,
    		          BigDecimal percontrib,
    		          BigDecimal interest_rate,
                      int start_year,
              	      int start_month) {
    	this.name = name;
        this.init_amount = init_amount;
        this.init_tax_rate = tax_rate;     
        this.tax_rate = Money.scaleRate(tax_rate);
        this.tax_rate_decimal = tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);     
        this.init_percontrib = percontrib;
        this.percontrib = Money.scaleRate(percontrib);
        percontrib_decimal = this.percontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        this.init_interest_rate = interest_rate;
        this.interest_rate = interest_rate;
        this.interest_rate_decimal = Money.scaleRate(this.interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE));  
        comp_factor_28 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 28.0/365));
        comp_factor_30 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 30.0/365));
        comp_factor_31 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 31.0/365));        	
        
    	this.start_year = start_year;
    	this.start_month = start_month;
        history = new HistoryInvestmentSavAcct(this);
    	setValuesBeforeCalculation();
    }
    
	public static InvestmentSavAcct create(
			String name,
	          BigDecimal init_amount,
	          BigDecimal tax_rate,
	          BigDecimal percontrib,
	          BigDecimal interest_rate,
              int start_year,
  	          int start_month) {
		return new InvestmentSavAcct(
				name,
				init_amount,
				tax_rate,                
			    percontrib,
			    interest_rate,
			    start_year,
				start_month);
	}

    @Override
    public boolean isPreTax() {
    	return false;
    }
       
	@Override
    public boolean isCheckingAcct() {
    	return false;
    }
	
	public BigDecimal getTaxRate() {
		return tax_rate;
	}
		
	public BigDecimal getInterestRate() {
		return interest_rate;
	}
 
    private BigDecimal getCompoundingFactor(int month_length) {   
    	switch(month_length) {
    	case 30:
            return comp_factor_30;            	
    	case 31:
        	return comp_factor_31;
    	case 28:
        	return comp_factor_28;
        default:
        	throw new NoSuchElementException("Only factor 28, 30 or 31 is possible.");
        }
    }  
        
    public BigDecimal getTax() {    
    	return tax_on_interests;
    }    
        
	@Override
    public BigDecimal getInterestsNet() {
    	return interests_net;
    }   
    
    public BigDecimal getInterestsGross() {
        return interests_gross;
    } 

	public BigDecimal getInitTaxRate() {
		return init_tax_rate;
	}
	
	public BigDecimal getInitPercontrib() {
		return init_percontrib;
	}
	
	public BigDecimal getInitInterestRate() {
		return init_interest_rate;
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

	public BigDecimal getContribution() {
		return contribution;
	}
	
	public BigDecimal getAccumulatedSavings() {
		return amount;
	}
	
    @Override
    public String getName() {
        return name;
    }    
    
    @Override
    public BigDecimal getValue() {
        return init_amount; // init_amount or amount?
    }   

    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForInvestmentSavAcct(this);
    }
        
    @Override
    public void advance(int year, int month, BigDecimal excess, BigDecimal checkingAcctPercontrib) {
    	if (year == start_year && month == start_month) {
    		initialize();
    		advanceValues(month, excess);
    	} else if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(month, excess);
    	}       	
    }
    
    @Override
    public void setValuesBeforeCalculation() {
        amount = Money.ZERO;	
        hidden_amount = Money.ZERO;
        contribution = Money.ZERO;
    }
	
	@Override
	public void initialize() {
		amount = Money.scale(init_amount);
		hidden_amount = init_amount;  
		contribution = Money.ZERO;
	}
    
    private void advanceValues(int month, BigDecimal excess) {
    	
        interests_gross = Money.ZERO;
        tax_on_interests = Money.ZERO;
        interests_net = Money.ZERO;
    	         
        if (excess.compareTo(Money.ZERO) == 1) {
        	/* Add monthly contribution (a given percentage of excess money) to the hidden_amount
     	     * if excess > 0. */
            amount = amount.add(Money.getPercentage(excess, percontrib_decimal));
            contribution = Money.getPercentage(excess, percontrib_decimal);              
         	hidden_amount = hidden_amount.add(contribution);   
        }
     	/* Calculate the interests of hidden_amount (and add to principal)  */
     	hidden_amount = Money.scale(hidden_amount.multiply(getCompoundingFactor(months[month])));
     	
     	/* calculate and subtract tax on interests */
 		interests_gross = hidden_amount.subtract(amount);
 		tax_on_interests = Money.getPercentage(interests_gross, tax_rate_decimal);
 		hidden_amount = hidden_amount.subtract(tax_on_interests);
        interests_net = interests_gross.subtract(tax_on_interests);            
        amount = hidden_amount;	
    }       

	@Override
	public HistoryInvestmentSavAcct getHistory() {
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
		modifier.updateInputListingForInvestmentSavAcct(this);						
	}
}