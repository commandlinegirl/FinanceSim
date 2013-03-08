package com.codelemma.finances.accounting;
import java.math.BigDecimal;

import com.codelemma.finances.ParseException;
import com.codelemma.finances.TypedContainer;
import com.codelemma.finances.TypedKey;

public class Debt implements NamedValue {
		
    private BigDecimal debt;
    private BigDecimal monthly_debt;
    private BigDecimal interest_rate;
    private BigDecimal pay_rate;
    private String name;
    private int id;
    
    public Debt(String name, BigDecimal debt) {
    	this.name = name;
    	this.debt = Money.scale(debt);
    	this.monthly_debt = this.debt;
    }
    
    public void setInitDebt() {
    	debt = monthly_debt;
    }
    
    public void setDebt(String debt) {
    	this.debt = Money.scale(new BigDecimal(debt));
    }
    
    public void setName(String name) {
    	this.name = name;
    }
     
    public void advance(int month) {
        monthly_debt = monthly_debt;        
    }

    
    public BigDecimal getMonthlyDebt() {
    	return monthly_debt;
    }
    
    public void setId(int id) {
        this.id = id;
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
	public TypedContainer getFieldContainer(PackToContainerVisitor saver) throws ParseException {
		return saver.packDebt(this);		
	}
    
    @Override
    public BigDecimal getValue() {
    	return debt;
    }
    
    @Override
    public void launchModifyUi(ModifyUiVisitor modifyUiVisitor) {
    	modifyUiVisitor.launchModifyUiForDebt(this);
    }
    

}