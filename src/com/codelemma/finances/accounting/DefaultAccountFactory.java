package com.codelemma.finances.accounting;

import java.math.BigDecimal;

/**
 * Factory(dp) for default Account objects. The objects come pre-populated with
 * checking account and other basic settings. This factory is safe in that
 * it throws no exceptions. It is also the place where default configuration
 * for account objects is stored.
 */
public class DefaultAccountFactory implements AccountFactory {
    @Override
    public Account createAccount(int simStartYear, int simStartMonth) {
    	Account account = new Account(simStartYear, simStartMonth);
    	InvestmentCheckAcct checkingAccount = createDefaultCheckingAccount(simStartYear, simStartMonth);
		account.addInvestment(checkingAccount);
    	account.setCheckingAcct(checkingAccount);
    	account.setCheckingAcctPercontrib();
    	return account;
    }

    private InvestmentCheckAcct createDefaultCheckingAccount(int simStartYear, int simStartMonth) {
    	return new InvestmentCheckAcct(
    			"Checking account",    // name
    			Money.ZERO,            // init_amount
    			new BigDecimal(30),    // tax_rate
                1,                     // interest capitalization (1 = monthly)
                new BigDecimal("0.5"), // interest_rate
                simStartYear,          // calculation start = simulation start
                simStartMonth);		   // calculation start = simulation start	    	
    }
}