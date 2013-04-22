package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Investment implements NamedValue {
	             
	public abstract  BigDecimal getInitAmount();
    public abstract  BigDecimal getPercontrib();
    public abstract  void initialize();     
    public abstract  void setId(int id);
    public abstract boolean isCheckingAcct();
    public abstract boolean isPreTax();
    public abstract BigDecimal getInterestsNet();
    public void advance(int year, int month, InvestmentCheckAcct checkingAcct) {};  
    public void advance(int year, int month, BigDecimal excess) {};  
    public void advance(int year, int month, BigDecimal excess, BigDecimal percontrib) {};  
    public void advance(int month, int month2, BigDecimal excess, BigDecimal checkingAcctPercontrib, 
    		InvestmentCheckAcct checkingAcct) {};
    public Income getIncome() {return null;};
    public BigDecimal getEmployeeContribution() {return null;}
	public abstract void setValuesBeforeCalculation();
}