package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class Income implements NamedValue {
	
	private int id;
    private BigDecimal init_income;
    private BigDecimal income_monthly;
    private BigDecimal tax_rate;
    private BigDecimal tax_rate_decimal;
    private BigDecimal rise_rate;
    private BigDecimal rise_rate_decimal;
    private BigDecimal installments;
    private String name;
    private BigDecimal num_of_extras;
    private BigDecimal yearly_income;
    
    public Income(BigDecimal init_income, 
    		      BigDecimal tax_rate, 
    		      BigDecimal rise_rate, 
    		      BigDecimal installments,
                  String name) {
        this.installments = installments; // 12 or 13
        this.init_income = Money.scale(init_income);
        this.yearly_income = init_income;
        income_monthly = this.init_income.divide(installments, Money.ROUNDING_MODE);
        this.tax_rate = Money.scaleRate(tax_rate);
        tax_rate_decimal = Money.scaleRate(tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE));        
        this.rise_rate = Money.scaleRate(rise_rate);
        rise_rate_decimal = Money.scaleRate(rise_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE));
        this.name = name;
        num_of_extras = installments.subtract(new BigDecimal(12));
    }

    public void initialize() {
    	/* To recalculate set those values that are incremented each month/year to init */
    	this.income_monthly = this.init_income.divide(this.installments, Money.ROUNDING_MODE);   	
    	yearly_income = init_income;
    }
    
    public void setIncome(BigDecimal inc) {
    	init_income = Money.scale(inc);
    	yearly_income = init_income;
    	income_monthly = init_income.divide(installments, Money.ROUNDING_MODE); 
    }
    
    public void setTaxRate(BigDecimal tax_rate) {
        this.tax_rate = Money.scaleRate(tax_rate);
        tax_rate_decimal = Money.scaleRate(tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE));         
    }
    
    public void setRiseRate(BigDecimal rise_rate) {
    	this.rise_rate = Money.scaleRate(rise_rate);
    	rise_rate_decimal = Money.scaleRate(rise_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE));
    }
    
    public void setInstallments(BigDecimal installments) { 
    	this.installments = Money.scale(installments);
    	income_monthly = init_income.divide(this.installments, Money.ROUNDING_MODE);
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public BigDecimal getNetIncome() {
        return income_monthly.subtract(Money.getPercentage(income_monthly, tax_rate_decimal));
    }

    public void advance(int month) {    
        // 13th salary paid in December
    	// salary rise in January only
        if (month == 11) {
        	BigDecimal extra_money = Money.scale(income_monthly.multiply(num_of_extras));        	
            income_monthly = income_monthly.add(extra_money);
        } else if (month == 0) {
        	yearly_income = Money.getPercentage(yearly_income, rise_rate_decimal).add(yearly_income);        	
        	income_monthly = yearly_income.divide(installments, Money.DECIMALS, Money.ROUNDING_MODE);   	
        }
    }
    
    public BigDecimal getTaxRate() {
    	return tax_rate;
    }
    
    public BigDecimal getRiseRate() {
        return rise_rate;
    }
    
    public BigDecimal getInstallments() {
        return installments;
    }
    
    public BigDecimal getMonthlyIncome() {
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
		return saver.packIncome(this);		
	}        
}

