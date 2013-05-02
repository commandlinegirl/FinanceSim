package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Investment extends AccountingElement {

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
    public int getStoredIncomeId() {return -1;};
    public void setIncome(Income income) {};
    public BigDecimal getEmployeeContribution() {return null;}	
    
	@Override
	public void addToAccount(Account account) {
		account.addInvestment(this);
	}
}