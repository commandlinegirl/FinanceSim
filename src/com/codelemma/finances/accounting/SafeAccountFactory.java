package com.codelemma.finances.accounting;

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
    
    @Override
    public Account loadAccount() {
    	try {
    		return primaryFactory.loadAccount();
    	} catch (AccountFactoryException ace) {
    		ace.printStackTrace();
    		return defaultAccountFactory.loadAccount();
    	}
    }
}