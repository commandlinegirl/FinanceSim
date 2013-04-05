package com.codelemma.finances.accounting;

public abstract class Expense implements NamedValue {
    
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int year, int month) {}
	public abstract void setValuesBeforeCalculation();   
}