package com.codelemma.finances.accounting;
import java.math.BigDecimal;

import android.util.Log;

import com.codelemma.finances.InputListingUpdater;

public class ExpenseGeneric extends Expense {

    private BigDecimal init_expense;
    private BigDecimal init_inflation_rate;
    private BigDecimal periodic_expense;
    private BigDecimal periodic_inflation_rate_decimal;
    private String name;
    private int frequency;
    private int start_year;
    private int start_month;
    private HistoryExpenseGeneric history;

    private ExpenseGeneric(String name,
    		BigDecimal init_expense, 
    		BigDecimal inflation_rate,
    		int frequency,
            int start_year,
    	    int start_month) {
        this.name = name;
        this.init_expense = init_expense;
        this.init_inflation_rate = inflation_rate;
        this.frequency = frequency;        
        this.start_year = start_year;
        this.start_month = start_month;
        periodic_inflation_rate_decimal = Money.scale(new BigDecimal(Math.pow(inflation_rate.doubleValue()/100 + 1, 1.0/12.0) - 1));
        history = new HistoryExpenseGeneric(this);
    	setValuesBeforeCalculation();
    }
    
    public static ExpenseGeneric create(
    		String name,
    		BigDecimal init_expense, 
    		BigDecimal inflation_rate,
    		int frequency,
            int start_year,
    	    int start_month) {
    	return new ExpenseGeneric(
    			name,
                init_expense, 
                inflation_rate, 
                frequency,
		        start_year,
		    	start_month);
    }

    @Override
    public void initialize() {
    	periodic_expense = init_expense.divide(new BigDecimal(frequency), Money.DECIMALS, Money.ROUNDING_MODE);
    }
        
    @Override
    public void setValuesBeforeCalculation() {
    	periodic_expense = Money.ZERO;
    }

    @Override
    public void advance(int year, int month) {
    	if (year == start_year && month == start_month) {
    		initialize();
    	} else if ((year > start_year) || (year == start_year && month > start_month)) {
    		advanceValues(year, month);
    	}
    }

    public void advanceValues(int year, int month) {
    	Log.d("periodic_inflation_rate_decimal", periodic_inflation_rate_decimal.toString());
    	periodic_expense = periodic_expense.add(Money.getPercentage(periodic_expense, periodic_inflation_rate_decimal));
    }

    public BigDecimal getInitInflationRate() {
    	return init_inflation_rate;
    }

    public int getFrequency() {
        return frequency;
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
	public BigDecimal getAmount() {
		return periodic_expense;
	}   
	
	public HistoryExpenseGeneric getHistory() {
		return history;
	}

	@Override
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForExpenseGeneric(this);				
		
	}
	
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForExpense(this);
    }   
    
	@Override
	public int getStartYear() {
		return start_year;
	}
	
	@Override
	public int getStartMonth() {
		return start_month;
	}
}
