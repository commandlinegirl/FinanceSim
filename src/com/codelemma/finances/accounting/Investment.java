package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Investment implements AccountingElement {
	
	private static int next_id = 0;
	private final int id = next_id;
	
	public Investment() {
		next_id++;		
	}
		
	public abstract  BigDecimal getInitAmount();
    public abstract  BigDecimal getPercontrib();
    public abstract boolean isCheckingAcct();
    public abstract boolean isPreTax();
    public abstract BigDecimal getInterestsNet();
    public void advance(int year, int month, 
    		InvestmentCheckAcct checkingAcct) {};  
    public void advance(int year, int month, 
    		BigDecimal excess) {};  
    public void advance(int year, int month, 
    		BigDecimal excess, 
    		BigDecimal percontrib) {};  
    public void advance(int month, int month2, 
    		BigDecimal excess, 
    		BigDecimal checkingAcctPercontrib, 
    		InvestmentCheckAcct checkingAcct) {};
    public Income getIncome() {return null;};
    public BigDecimal getEmployeeContribution() {return null;}
	
	@Override
	public void addToAccount(Account account) {
		account.justAddInvestment(this);
	}
	
	@Override 
	public int getId() {
		return id;
	}	
}