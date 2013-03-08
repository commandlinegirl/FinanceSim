package com.codelemma.finances.accounting;

import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;

public class InvestmentBond extends Investment 
                            implements NamedValue {

	private int id;
	private String name;
	private BigDecimal init_amount;
	private BigDecimal amount;
	private BigDecimal tax_rate;
	
    public InvestmentBond(String name,
	          BigDecimal init_amount,
	          BigDecimal percontrib,
	          BigDecimal tax_rate) {
        this.name = name;
        this.init_amount = Money.scale(init_amount);
        this.amount = init_amount;
        this.tax_rate = tax_rate;
    }
    
	
	@Override
	public BigDecimal getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TypedContainer getFieldContainer(PackToContainerVisitor saver)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getInitAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getPercontrib() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getAmount() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public BigDecimal getTaxRate() {
		return tax_rate;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HistoryNew createHistory() {
		// TODO Auto-generated method stub
		return null;
	}

}
