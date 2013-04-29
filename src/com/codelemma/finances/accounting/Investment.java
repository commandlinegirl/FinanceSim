package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Investment implements AccountingElement {
	public abstract  BigDecimal getInitAmount();
    public abstract  BigDecimal getPercontrib();
    public abstract  void initialize();     
	// TODO: Methods which are present in all four abstract classes implementing AccountingElement should be moved to AccountingElement.
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
	
	@Override
	public void addToAccount(Account account) {
		account.justAddInvestment(this);
	}
}