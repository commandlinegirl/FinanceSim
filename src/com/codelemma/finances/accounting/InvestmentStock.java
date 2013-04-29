package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.InputListingUpdater;
import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class InvestmentStock extends Investment 
                             implements AccountingElement {

	private int id;
	private String name;
	private BigDecimal init_amount;
	private BigDecimal amount;
    private BigDecimal percontrib; // percentage of excess money
    private BigDecimal percontrib_decimal; // percentage of excess money
    private BigDecimal tax_rate;
    private BigDecimal tax_rate_decimal;
    private BigDecimal appreciation;
    private BigDecimal dividend;
    private HistoryInvestmentStock history;
    
    public InvestmentStock(String name,
	          BigDecimal init_amount,
	          BigDecimal percontrib,
	          BigDecimal tax_rate,
	          BigDecimal dividend,
	          BigDecimal appreciation) {
        this.name = name;
        this.init_amount = Money.scale(init_amount);
        this.amount = init_amount;
        
        this.percontrib = Money.scaleRate(percontrib);  
        this.percontrib_decimal = percontrib.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE); 
 
        this.tax_rate = tax_rate;
        this.tax_rate_decimal = tax_rate.divide(Money.HUNDRED, Money.RATE_DECIMALS, Money.ROUNDING_MODE);
        
        this.appreciation = appreciation;
        
        this.dividend = dividend;
        
        history = new HistoryInvestmentStock(this);
    }
    
    public boolean isPreTax() {
    	return false;
    }
        
	@Override
	public BigDecimal getValue() {
		return init_amount;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
    public boolean isCheckingAcct() {
    	return false;
    }
	
    @Override
    public void advance(int year, int month, BigDecimal excess) {
    	         
    	/* Add monthly contribution (a given percentage of excess money) to the hidden_amount  */
        amount = amount.add(Money.getPercentage(excess, percontrib));
        // cont.
        
    }
    

	@Override
	public BigDecimal getInitAmount() {
		return init_amount;
	}

	@Override
	public BigDecimal getPercontrib() {
		return percontrib;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getTaxRate() {
		return tax_rate;
	}
	
	
	public BigDecimal getAppreciation() {
		return appreciation;
	}
	
	public BigDecimal getDividends() {
		return dividend;
	}
	
	public BigDecimal getTax() {
		return new BigDecimal(0); // return tax
	}
	
	
	public BigDecimal getCapitalGainYield() {
		return new BigDecimal(0); // return cgy
	}
	
	
	@Override
	public void initialize() {
		amount = init_amount;		
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;		
	}

	@Override
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForInvestmentStock(this);		
	}
	
	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver)
			throws ParseException {
		return saver.packInvestmentStock(this);
	}

	@Override
	public HistoryInvestmentStock getHistory() {
		return history;
	}

	@Override
	public void updateInputListing(InputListingUpdater modifier) {
		modifier.updateInputListingForInvestmentStock(this);						
	}

	@Override
	public int getStartYear() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartMonth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getInterestsNet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValuesBeforeCalculation() {
		// TODO Auto-generated method stub
		
	}

}
