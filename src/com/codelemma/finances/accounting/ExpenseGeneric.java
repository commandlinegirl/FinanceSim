package com.codelemma.finances.accounting;
import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;

public class ExpenseGeneric extends Expense {
		
    private BigDecimal init_expense;
    private BigDecimal init_inflation_rate;
    private BigDecimal periodic_expense;
    private BigDecimal periodic_inflation_rate_decimal;
    private BigDecimal hidden_periodic_expense;    
    private String name;
    private int frequency;
    private int[] monthNumbersForDisplay = {12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
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
        periodic_inflation_rate_decimal = inflation_rate.divide(
        		new BigDecimal(1200/frequency), //TODO: check if not divided by ZERO
                Money.RATE_DECIMALS,        		
                Money.ROUNDING_MODE);
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
    	periodic_expense = Money.scale(init_expense);
    	hidden_periodic_expense = periodic_expense;
    }
        
    @Override
    public void setValuesBeforeCalculation() {
    	periodic_expense = Money.ZERO;
    	hidden_periodic_expense = Money.ZERO;
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
    	if ((monthNumbersForDisplay[month] % frequency) == 0 ) {  
    		hidden_periodic_expense = hidden_periodic_expense.add(
    				Money.getPercentage(hidden_periodic_expense, periodic_inflation_rate_decimal));
    		periodic_expense = hidden_periodic_expense;
    	} else {
    		periodic_expense = Money.ZERO;
    	}
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
