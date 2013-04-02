package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Debt implements NamedValue {
    
	public abstract  BigDecimal getInitAmount();
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public abstract BigDecimal getMonthlyPayment();         
    public abstract void advance(int year, int month);

}
