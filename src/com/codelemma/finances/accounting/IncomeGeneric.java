package com.codelemma.finances.accounting;

import java.math.BigDecimal;
import com.codelemma.finances.InputListingUpdater;

public class IncomeGeneric extends Income {
	
    private BigDecimal init_income;
    private BigDecimal init_tax_rate;
    private BigDecimal init_rise_rate;        
    private BigDecimal taxable_income;    
    private BigDecimal gross_income;    
    private BigDecimal tax_rate_decimal;
    private BigDecimal rise_rate_decimal;
    private BigDecimal installments;
    private BigDecimal tax;
    private String name;
    private BigDecimal num_of_extras;
    private BigDecimal yearly_income;
    private int start_year;
    private int start_month;
    private int term;
    private int term_months;
    private int counter;
    private int previous_id;
    private HistoryIncomeGeneric history;
    private Investment401k investment401k;         
        
    private IncomeGeneric(BigDecimal init_income, 
    		      BigDecimal tax_rate, 
    		      BigDecimal rise_rate, 
    		      BigDecimal installments,
                  String name,
                  int term,
                  int start_year,
          	      int start_month) {
        this.installments = installments; // 12 or 13
        this.init_income = init_income;
        this.init_tax_rate = tax_rate;
        this.init_rise_rate = rise_rate;
        this.yearly_income = init_income;
        this.tax_rate_decimal = tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        this.rise_rate_decimal = rise_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        this.name = name;
        this.term = term;
        this.term_months = term * 12;        
        this.start_year = start_year;
        this.start_month = start_month;
        num_of_extras = installments.subtract(new BigDecimal(12));
        history = new HistoryIncomeGeneric(this);
    	setValuesBeforeCalculation();
    }

    public static IncomeGeneric create(
    		BigDecimal init_income, 
		      BigDecimal tax_rate, 
		      BigDecimal rise_rate, 
		      BigDecimal installments,
              String name,
              int term,
              int start_year,
  	          int start_month) {
  	  return new IncomeGeneric(
  			  init_income, 
              tax_rate, 
              rise_rate,
              installments, 
              name,
              term,
		      start_year,
		      start_month);
    }
    
	public Investment401k getInvestment401k() {
    	return investment401k;
    }
    
    public void setInvestment401k(Investment401k inv) {
    	investment401k = inv;
    }
    
    public void initialize() {
    	/* To recalculate set those values that are incremented each month/year to init */
    	yearly_income = init_income;
    	gross_income = init_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);
    	taxable_income = gross_income;
    	tax = Money.getPercentage(taxable_income, tax_rate_decimal);
    	if (investment401k != null) {    		
    	    taxable_income = taxable_income.subtract(investment401k.getEmployeeContribution());
    	    tax = Money.getPercentage(taxable_income, tax_rate_decimal);
    	}
		counter = 0;
    }
    
    @Override
    public void setValuesBeforeCalculation() {
    	gross_income = Money.ZERO;
    	taxable_income = Money.ZERO;
    	tax = Money.ZERO;
    }
    
    @Override
    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {
    	if (year == start_year && month == start_month) {
    		initialize();
    		advanceValues(year, month, checkingAcct);
    	} else if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(year, month, checkingAcct);
    	}
    }

    public void advanceValues(int year, int month, InvestmentCheckAcct checkingAcct) {
        /* 13th salary paid in December;
         * salary rise in January
         */
    	
    	if (counter < term_months) {
    		if (month == 11) {
            	BigDecimal extra_money = Money.scale(taxable_income.multiply(num_of_extras));        	
                gross_income = gross_income.add(extra_money);
                taxable_income = gross_income;
                tax = Money.getPercentage(taxable_income, tax_rate_decimal);
            } else if (month == 0) {
            	yearly_income = yearly_income.add(riseAmount());        	
            	gross_income = yearly_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);             	
            	taxable_income = gross_income;
            	tax = Money.getPercentage(taxable_income, tax_rate_decimal);
            }
            if (investment401k != null) {
                investment401k.advance(year, month, checkingAcct);
                if (month == 0 || month == 11 || (year == investment401k.getStartYear() && month == investment401k.getStartMonth())) {
                    taxable_income = taxable_income.subtract(investment401k.getEmployeeContribution());
                    tax = Money.getPercentage(taxable_income, tax_rate_decimal);
                }		        		    	    
            }
            counter++;
    	} else if (counter == term_months) {
    		setValuesBeforeCalculation();
    		counter++;
    	}
    }
    
    @Override
    public BigDecimal getNetIncome() {
        return taxable_income.subtract(tax);
    }
    
    public BigDecimal getGrossIncome() {
        return gross_income;
    }

    @Override
    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal riseAmount() {
    	return Money.getPercentage(yearly_income, rise_rate_decimal);
    }
    
    public int getTerm() {
    	return term;
    }
    
    public BigDecimal getInitTaxRate() {
    	return init_tax_rate;
    }

    @Override
    public BigDecimal getInitRiseRate() {
        return init_rise_rate;
    }
    
    public BigDecimal getInitInstallments() {
        return installments;
    }
        
	@Override
	public BigDecimal getAmount() {
		return getNetIncome();
	}  
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public BigDecimal getValue() {
        return init_income;
    }    

    @Override
    public void setPreviousId(int id) {
    	previous_id = id;
    }

    @Override
    public int getPreviousId() {
    	return previous_id;
    }
    
	public HistoryIncomeGeneric getHistory() {
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
		modifier.updateInputListingForIncomeGeneric(this);		
	}
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForIncome(this);
    }

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void setInvestment401k(Investment investment) { 
		investment401k = (Investment401k) investment; //TODO
	}
}