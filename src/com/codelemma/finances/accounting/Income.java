package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public abstract class Income implements NamedValue {
   
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int month) {}
	public abstract BigDecimal getNetIncome();     	
}
