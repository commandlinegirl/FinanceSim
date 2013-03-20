package com.codelemma.finances.accounting;
import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class ExpenseGeneric extends Expense 
                            implements NamedValue {
		
    private BigDecimal init_expense;
    private BigDecimal init_inflation_rate;
    private BigDecimal periodic_expense;
    private BigDecimal periodic_inflation_rate_decimal;
    private BigDecimal hidden_periodic_expense;    
    private String name;
    private int frequency;
    private int id;
    private HistoryExpenseGeneric history;
    private int[] monthNumbersForDisplay = {12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};    
    
    public ExpenseGeneric(String _name,
    		BigDecimal _init_expense, 
    		BigDecimal _inflation_rate,
    		int _frequency) {
        name = _name;    	
        init_expense = _init_expense;        
        init_inflation_rate = _inflation_rate;
        frequency = _frequency;
        periodic_expense = new BigDecimal(0);
        hidden_periodic_expense = init_expense;
        periodic_inflation_rate_decimal = _inflation_rate.divide(new BigDecimal(1200/frequency), //TODO: check if not divided by ZERO
                                                                    Money.RATE_DECIMALS,        		
                                                                    Money.ROUNDING_MODE);
        frequency = _frequency;
        history = new HistoryExpenseGeneric(this);
    }

    public void initialize() { 
    	periodic_expense = new BigDecimal(0);
    	hidden_periodic_expense = init_expense;
    }
        
    public void advance(int month) {
    	if ((monthNumbersForDisplay[month] % frequency) == 0 ) {  
    		hidden_periodic_expense = hidden_periodic_expense.add(
    				Money.getPercentage(hidden_periodic_expense, periodic_inflation_rate_decimal));
    		periodic_expense = hidden_periodic_expense;
    	} else {
    		periodic_expense = new BigDecimal(0);
    	}
    }   
    
    public BigDecimal getInitInflationRate() {
    	return init_inflation_rate;
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
		return saver.packExpenseGeneric(this);		
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

}