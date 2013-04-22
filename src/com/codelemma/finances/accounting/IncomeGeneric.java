package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import android.util.Log;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class IncomeGeneric extends Income
                           implements NamedValue {
	
	private int id;
    private BigDecimal init_income;
    private BigDecimal init_tax_rate;
    private BigDecimal init_rise_rate;        
    private BigDecimal taxable_income;    
    private BigDecimal gross_income;    
    private BigDecimal tax_rate_decimal;
    private BigDecimal rise_rate_decimal;
    private BigDecimal installments;
    private String name;
    private BigDecimal num_of_extras;
    private BigDecimal yearly_income;
    private HistoryIncomeGeneric history;
    private int start_year;
    private int start_month;
    private int term;
    private int term_months;
    private int counter;
    private Investment401k investment401k;         
    
    public IncomeGeneric(BigDecimal _init_income, 
    		      BigDecimal _tax_rate, 
    		      BigDecimal _rise_rate, 
    		      BigDecimal _installments,
                  String _name,
                  int _term,
                  int _start_year,
          	      int _start_month) {
        installments = _installments; // 12 or 13
        init_income = _init_income;
        init_tax_rate = _tax_rate;
        init_rise_rate = _rise_rate;
        yearly_income = _init_income;               
        tax_rate_decimal = _tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        rise_rate_decimal = _rise_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        
        name = _name;
        num_of_extras = installments.subtract(new BigDecimal(12));
        history = new HistoryIncomeGeneric(this);
        
        term = _term;
        term_months = _term * 12;
        
    	start_year = _start_year;
    	start_month = _start_month;
    	setValuesBeforeCalculation();
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
    	if (investment401k != null) {    		
    		Log.d("investment401k is not null and the investment401k.getEmployeeContribution()", investment401k.getEmployeeContribution().toString());    		
    	    taxable_income = taxable_income.subtract(investment401k.getEmployeeContribution());
    	}
		Log.d("taxable_income initialized to: ", taxable_income.toString());
		counter = 0;
    }
    
    @Override
    public void setValuesBeforeCalculation() {
    	gross_income = Money.ZERO;
    	taxable_income = Money.ZERO;
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
            } else if (month == 0) {
            	yearly_income = yearly_income.add(riseAmount());        	
            	gross_income = yearly_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);      
            	taxable_income = gross_income;
            }
            if (investment401k != null) {
                investment401k.advance(year, month, checkingAcct);
                if (month == 0 || month == 11 || (year == investment401k.getStartYear() && month == investment401k.getStartMonth())) {
                    taxable_income = taxable_income.subtract(investment401k.getEmployeeContribution());
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
        return taxable_income.subtract(getTax());
    }
    
    public BigDecimal getGrossIncome() {
        return gross_income;
    }

    @Override
    public BigDecimal getTax() {
        return Money.getPercentage(taxable_income, tax_rate_decimal);
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
    public int getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public BigDecimal getValue() {
        return init_income;
    }    
        
	public void setId(int id) {
		this.id = id;		
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
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packIncomeGeneric(this);		
	}
	
	@Override
	public String toString() {
		return name;
	}
}

