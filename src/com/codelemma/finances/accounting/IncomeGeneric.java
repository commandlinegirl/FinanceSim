package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class IncomeGeneric extends Income
                           implements NamedValue {
	
	private int id;
    private BigDecimal init_income;
    private BigDecimal init_tax_rate;
    private BigDecimal init_rise_rate;    
    private BigDecimal income_monthly;
    private BigDecimal tax_rate_decimal;
    private BigDecimal rise_rate_decimal;
    private BigDecimal installments;
    private String name;
    private BigDecimal num_of_extras;
    private BigDecimal yearly_income;
    private HistoryIncomeGeneric history;
    private int start_year;
    private int start_month;
    
    public IncomeGeneric(BigDecimal _init_income, 
    		      BigDecimal _tax_rate, 
    		      BigDecimal _rise_rate, 
    		      BigDecimal _installments,
                  String _name,
                  int _start_year,
          	      int _start_month) {
        installments = _installments; // 12 or 13
        init_income = _init_income;
        init_tax_rate = _tax_rate;
        init_rise_rate = _rise_rate;
        yearly_income = _init_income;               
        income_monthly = yearly_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);
        tax_rate_decimal = _tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);        
        rise_rate_decimal = _rise_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        
        name = _name;
        num_of_extras = installments.subtract(new BigDecimal(12));
        history = new HistoryIncomeGeneric(this);
        
    	start_year = _start_year;
    	start_month = _start_month;
    } 

    public void initialize() {
    	/* To recalculate set those values that are incremented each month/year to init */
    	yearly_income = init_income;
    	income_monthly = init_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);   	    	
    }

    
    public void advance(int month) {    
        // 13th salary paid in December
    	// salary rise in January only
        if (month == 11) {
        	BigDecimal extra_money = Money.scale(income_monthly.multiply(num_of_extras));        	
            income_monthly = income_monthly.add(extra_money);
        } else if (month == 0) {
        	yearly_income = yearly_income.add(riseAmount());        	
        	income_monthly = yearly_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);   	
        }
    }
    
    @Override
    public BigDecimal getNetIncome() {
        return income_monthly.subtract(getTax());
    }
    
    public BigDecimal getGrossIncome() {
        return income_monthly;
    }

    public BigDecimal getTax() {
        return Money.getPercentage(income_monthly, tax_rate_decimal);
    }

    public BigDecimal riseAmount() {
    	return Money.getPercentage(yearly_income, rise_rate_decimal);
    }
    
    public BigDecimal getInitAmount() {
    	return init_income;
    }
    
    public BigDecimal getInitTaxRate() {
    	return init_tax_rate;
    }
    
    public BigDecimal getInitRiseRate() {
        return init_rise_rate;
    }
    
    public BigDecimal getInitInstallments() {
        return installments;
    }
    
    
	@Override
	public BigDecimal getAmount() {
		return income_monthly;
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
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForIncome(this);
    }

	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packIncomeGeneric(this);		
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

}

