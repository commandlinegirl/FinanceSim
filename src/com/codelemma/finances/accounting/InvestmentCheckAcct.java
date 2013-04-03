package com.codelemma.finances.accounting;
import java.math.BigDecimal;


import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;
import java.util.NoSuchElementException;

public class InvestmentCheckAcct extends Investment 
                               implements NamedValue {
	
	private int id;
	private String name;	
    private BigDecimal init_amount;
    private BigDecimal init_tax_rate;
    private BigDecimal init_percontrib;
    private BigDecimal init_interest_rate;
    private BigDecimal percontrib;
    
    private BigDecimal tax_rate;    
    private BigDecimal tax_rate_decimal;     
    private BigDecimal contribution;
    
    private BigDecimal interest_rate;
    private int capitalization;
    private BigDecimal amount;
    private BigDecimal hidden_amount;
    private int capitalization_counter = 1;
    private BigDecimal interest_rate_decimal;
    
    private BigDecimal interests_gross = Money.ZERO;
    private BigDecimal tax_on_interests = Money.ZERO;
    private BigDecimal interests_net = Money.ZERO;
    
    private BigDecimal comp_factor_28;
    private BigDecimal comp_factor_30;
    private BigDecimal comp_factor_31;
    private int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private HistoryInvestmentCheckAcct history;


    private int start_year;
    private int start_month;
    
    public InvestmentCheckAcct(String name,
    		          BigDecimal init_amount,
    		          BigDecimal tax_rate,
    		          int capitalization,
    		          BigDecimal interest_rate,
                      int _start_year,
              	      int _start_month) {
    	this.name = name;
        this.init_amount = Money.scale(init_amount);
        this.init_interest_rate = interest_rate;
        this.init_tax_rate = tax_rate;
        this.amount = this.init_amount;
        this.hidden_amount = this.init_amount;        
        this.tax_rate = Money.scaleRate(tax_rate);
        this.tax_rate_decimal = tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        this.capitalization = capitalization;
        this.interest_rate = Money.scaleRate(interest_rate);
        this.interest_rate_decimal = this.interest_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);  
        comp_factor_28 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 28.0/365));
        comp_factor_30 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 30.0/365));
        comp_factor_31 = new BigDecimal(Math.exp(this.interest_rate_decimal.doubleValue() * 31.0/365));        	
        
        history = new HistoryInvestmentCheckAcct(this);
    	start_year = _start_year;
    	start_month = _start_month;
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
	
	public int getCapitalization() {
		return capitalization;
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
    
    @Override
    public int getId() {
        return id;
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
    	modifyUiVisitor.launchModifyUiForInvestmentCheckAcct(this);
    }
        
    @Override
    public void advance(int year, int month, BigDecimal excess, BigDecimal checkingAcctPercontrib) {


    	if ((year < start_year) || (year == start_year && month < start_month)) {
    		setValuesBeforeCalculation();
    	}        
    	   	
    	if (year == start_year && month == start_month) {
    		initizeValues();
    		advanceValues(month, excess, checkingAcctPercontrib);
    	}      	
    	
    	if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(month, excess, checkingAcctPercontrib);
    	}       	
    }
    
    /* event methods*/
    private void setValuesBeforeCalculation() {
        amount = Money.ZERO;	
        hidden_amount = Money.ZERO;
        contribution = Money.ZERO;
    }
    
    private void initizeValues() {
		amount = init_amount;
		capitalization_counter = 1;
		hidden_amount = init_amount;  
		contribution = Money.ZERO;
    }
    
    private void advanceValues(int month, BigDecimal excess, BigDecimal checkingAcctPercontrib) {
    	
        interests_gross = Money.ZERO;
        tax_on_interests = Money.ZERO;
        interests_net = Money.ZERO;
    	        
        BigDecimal checkingAcctPercontribDecimal = checkingAcctPercontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        percontrib = checkingAcctPercontrib;
     	/* Add monthly contribution (a given percentage of excess money) to the hidden_amount
     	 * if excess > 0. */       
        amount = amount.add(Money.getPercentage(excess, checkingAcctPercontribDecimal));
        contribution = Money.getPercentage(excess, checkingAcctPercontribDecimal);              
        hidden_amount = hidden_amount.add(contribution);  
        
     	/* Calculate the interests of hidden_amount (and add to principal)  */
     	hidden_amount = Money.scale(hidden_amount.multiply(getCompoundingFactor(months[month])));
     	
     	if (capitalization == capitalization_counter) {
     		/* calculate and subtract tax on interests */
     		BigDecimal hidden_interests_gross = hidden_amount.subtract(amount);
     		BigDecimal hidden_interests_gross_tax = Money.getPercentage(hidden_interests_gross, tax_rate_decimal);
     		hidden_amount = hidden_amount.subtract(hidden_interests_gross_tax);
            interests_gross = hidden_interests_gross;
            tax_on_interests = hidden_interests_gross_tax;
            interests_net = interests_gross.subtract(tax_on_interests);                 
            amount = hidden_amount;
         	capitalization_counter = 1;        	
     	} else {
     		capitalization_counter++;
     	}
    }       
    
    
    private void setValuesAfterCalculation() {
        
    }    
   
	@Override
	public void initialize() {
		amount = init_amount;
		capitalization_counter = 1;
		hidden_amount = init_amount;  
		contribution = Money.ZERO;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packInvestmentCheckAcct(this);		
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