package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import java.util.NoSuchElementException;

public class InvestmentCheckAcct extends Investment {
	
	private String name;	
    private BigDecimal init_amount;
    private BigDecimal init_tax_rate;
    private BigDecimal init_interest_rate;
    
    private BigDecimal percontrib;
    private BigDecimal percontrib_decimal;
    
    private BigDecimal tax_rate;    
    private BigDecimal tax_rate_decimal;     
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
    private HistoryInvestmentCheckAcct history;
    private BigDecimal withdrawal401k = Money.ZERO;

    private int start_year;
    private int start_month;

    private InvestmentCheckAcct(String name,
    		          BigDecimal init_amount,
    		          BigDecimal tax_rate,
    		          BigDecimal interest_rate,
                      int start_year,
              	      int start_month) {
    	this.name = name;
        this.init_amount = init_amount;
        this.init_interest_rate = interest_rate;
        this.init_tax_rate = tax_rate;      
        this.tax_rate = Money.scaleRate(tax_rate);
        this.tax_rate_decimal = tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        this.interest_rate = Money.scaleRate(interest_rate);
        this.interest_rate_decimal = this.interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);  
        comp_factor_28 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 28.0/365));
        comp_factor_30 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 30.0/365));
        comp_factor_31 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 31.0/365));        	

    	this.start_year = start_year;
    	this.start_month = start_month;
    	history = new HistoryInvestmentCheckAcct(this);
    	setValuesBeforeCalculation();
    }   

	public static InvestmentCheckAcct create(
			String name,
	          BigDecimal init_amount,
	          BigDecimal tax_rate,
	          BigDecimal interest_rate,
              int start_year,
    	      int start_month) {
        return new InvestmentCheckAcct(
        	name,
		    init_amount,
		    tax_rate,
            interest_rate,
            start_year,
            start_month);
    }
    
	@Override
    public boolean isCheckingAcct() {
    	return true;
    }
    
	@Override
    public boolean isPreTax() {
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
    	return tax_on_interests; //I added scale here
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
    
    public void add401kWithdrawal(BigDecimal withdrawal) {
    	withdrawal401k = withdrawal;
    }
    
    @Override
    public BigDecimal getValue() {
        return init_amount; // init_amount or amount?
    }   

    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForInvestmentCheckAcct(this);
    }
        
    @Override
    public void advance(int year, int month, BigDecimal excess, BigDecimal checkingAcctPercontrib) {

    	if ((year < start_year) || (year == start_year && month < start_month)) {
    		setValuesBeforeCalculation();
    	} else if (year == start_year && month == start_month) {
    		percontrib = checkingAcctPercontrib;
    		percontrib_decimal = checkingAcctPercontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
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
 		amount = init_amount;
 		hidden_amount = init_amount;  
 		contribution = Money.ZERO;
 	}
    
    private void advanceValues(int month, BigDecimal excess) {
    	
        interests_gross = Money.ZERO;
        tax_on_interests = Money.ZERO;
        interests_net = Money.ZERO;
    	        
                
     	/* Add monthly contribution (a given percentage of excess money) to the hidden_amount
     	 * if excess > 0. */       
        contribution = Money.getPercentage(excess, percontrib_decimal).add(withdrawal401k);                  
        withdrawal401k = Money.ZERO; // after adding withdrawn money, init it back to zero for next advancements
        
        amount = amount.add(contribution);                     
        hidden_amount = hidden_amount.add(contribution);
        
     	/* Calculates the interests of hidden_amount (and adds to principal)  */
     	hidden_amount = Money.scale(hidden_amount.multiply(getCompoundingFactor(months[month])));// i removed scale..
     	
     	/* calculate and subtract tax on interests */
 		interests_gross = hidden_amount.subtract(amount);
 		tax_on_interests = Money.getPercentage(interests_gross, tax_rate_decimal); // I removed scale here
 		/* Add interests from previous period to the amount */  
 		interests_gross = interests_gross.max(Money.ZERO);
 		tax_on_interests = tax_on_interests.max(Money.ZERO);
 		
 		if (interests_gross.compareTo(Money.ZERO) != 0) {
 		    hidden_amount = hidden_amount.subtract(tax_on_interests);     		            
            interests_net = interests_gross.subtract(tax_on_interests);                 
            amount = hidden_amount; // hidden amount contains interests!!!
 		}	
    }
	
	@Override
	public HistoryInvestmentCheckAcct getHistory() {
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
		modifier.updateInputListingForInvestmentCheckAcct(this);
	}


}