package com.codelemma.finances.accounting;
import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class Expense implements NamedValue {
		
    private BigDecimal init_expense;
    private BigDecimal monthly_expense;
    private BigDecimal inflation_rate;
    private BigDecimal period_inflation_rate_decimal;
    private String name;
    private int frequency;
    private int frequency_counter = 1;    
    private int id;
    
    public Expense(String name,
    		BigDecimal init_expense, 
    		BigDecimal inflation_rate,
    		int frequency) {
        this.name = name;    	
        this.init_expense = Money.scale(init_expense);
        this.monthly_expense = this.init_expense;
        this.inflation_rate = Money.scaleRate(inflation_rate);        
        period_inflation_rate_decimal = this.inflation_rate.divide(new BigDecimal(1200/frequency),
                                                                    Money.RATE_DECIMALS,        		
                                                                    Money.ROUNDING_MODE);
        this.frequency = frequency;
    }

    public void initialize() { 
    	monthly_expense = init_expense;
    }
    
    public void setExpense(BigDecimal expense) {
    	this.init_expense = Money.scale(expense);
    }

    public void setInflationRate(BigDecimal infl) {
        inflation_rate = Money.scaleRate(infl);
        period_inflation_rate_decimal = inflation_rate.divide(new BigDecimal(1200/frequency),
        		                                               Money.RATE_DECIMALS,
                                                               Money.ROUNDING_MODE);
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void advance(int month) {
    	if (frequency == frequency_counter) {
    	    monthly_expense = monthly_expense.add(Money.getPercentage(monthly_expense, period_inflation_rate_decimal));
    	    frequency_counter = 1;
    	} else {
    	    frequency_counter++;
    	}
    }   

    
    public BigDecimal getMonthlyExpense() {
    	return monthly_expense;
    }
    
    public BigDecimal getInflationRate() {
    	return inflation_rate;
    }

    public int getFrequency() {
        return frequency;
    }    
   
    @Override
    public int getId() {
        return id;
    }    
    
	public void setId(int id) {
		this.id = id;		
	}
    
    @Override
    public String getName() {
        return name;
    }    
    
    @Override
    public BigDecimal getValue() {
        return init_expense;
    }
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForExpense(this);
    }   
    
	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packExpense(this);		
	}   
}
