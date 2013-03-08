package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class InvestmentStock extends Investment 
                             implements NamedValue {

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
    public void advance(int month, BigDecimal excess) {
    	         
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
	
	public BigDecimal getDividend() {
		return dividend;
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
	public HistoryNew createHistory() {
	    return new HistoryInvestmentStock(this);
	}
	
	@Override
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForInvestment(this);		
	}
	
	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver)
			throws ParseException {
		return saver.packInvestmentStock(this);
	}

}
