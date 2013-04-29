package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.AccountFactory.AccountCreationException;

/**
 * Delegate(dp) Account factory(dp) which wraps another Account factory and provides simple
 * error handling mechanism by implementing a fall-back to safe default factory in
 * case of a failure of the primary factory. It also provides a hook for subclasses
 * to attempt to fix each instantiation problem.
 */
public class SafeAccountFactory implements AccountFactory {
	private AccountFactory primaryFactory;
	private DefaultAccountFactory defaultAccountFactory;
	
    public SafeAccountFactory(AccountFactory factory) {
    	primaryFactory = factory;
    	defaultAccountFactory = new DefaultAccountFactory();
    }
    
    /**
     * Template method(dp) providing a hook for subclasses to attempt to fix a problem.
     */
    protected void attemptToFix(AccountCreationException exception) {
    }

    @Override
    public Account createAccount(int simStartYear, int simStartMonth) {
    	try {
    		return primaryFactory.createAccount(simStartYear, simStartMonth);
    	} catch (AccountCreationException ace) {
    		ace.printStackTrace();
    		attemptToFix(ace);
    		return defaultAccountFactory.createAccount(simStartYear, simStartMonth);
    	}
    }
}