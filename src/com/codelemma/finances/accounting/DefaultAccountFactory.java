package com.codelemma.finances.accounting;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Factory(dp) for default Account objects. The objects come pre-populated with
 * checking account and other basic settings. This factory is safe in that
 * it throws no exceptions. It is also the place where default configuration
 * for account objects is stored.
 */
public class DefaultAccountFactory implements AccountFactory {
    @Override
    public Account loadAccount() {
    	Account account = new Account();
    	Calendar c = Calendar.getInstance();    	
    	InvestmentCheckAcct checkingAccount = createDefaultCheckingAccount(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
    	account.setSimulationStartYear(c.get(Calendar.YEAR));
    	account.setSimulationStartMonth(c.get(Calendar.MONTH));
    	account.setCalculationStartYear(c.get(Calendar.YEAR));
    	account.setCalculationStartMonth(c.get(Calendar.MONTH));
    	account.createDateList();
    	
		account.addInvestment(checkingAccount);
    	account.setCheckingAcct(checkingAccount);
    	account.setCheckingAcctPercontrib();
    	return account;
    }

    private InvestmentCheckAcct createDefaultCheckingAccount(int simStartYear, int simStartMonth) {
    	return InvestmentCheckAcct.create(
    			"Checking account",    // name
    			Money.ZERO,            // init_amount
    			new BigDecimal(30),    // tax_rate
                new BigDecimal("0.5"), // interest_rate
                simStartYear,          // calculation start = simulation start
                simStartMonth);		   // calculation start = simulation start	    	
    }
}