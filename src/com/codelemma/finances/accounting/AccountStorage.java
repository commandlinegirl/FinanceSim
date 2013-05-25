package com.codelemma.finances.accounting;

import java.util.HashMap;
import java.util.Map;

import com.codelemma.finances.accounting.Storage.OpenState;
import com.codelemma.finances.accounting.Storage.StorageException;

/**
 * Factory(dp) which instantiates Account objects based on data read from storage.
 * This factory may throw any exceptions related to reading from storage and
 * parsing the data read.
 */
public class AccountStorage implements AccountFactory, AccountSaver {
	// Account Storage property keys.
	private static final String INVESTMENT_PERCONTRIB = "ipc";
	private static final String ACCOUNTING_ELEMENTS_ID_LIST = "aeil";
	private static final String ACCOUNTING_ELEMENT_CLASS_TAG = "aect";
	private static final String SIMULATION_START_YEAR = "ssy";
	private static final String SIMULATION_START_MONTH = "ssm";
	private static final String CALCULATION_START_YEAR = "csy";
	private static final String CALCULATION_START_MONTH = "csm";

	private final Storage storage;
	private final Map<String, AccountingElementStorer<?>> storerByTag;
	private final Map<Class<?>, AccountingElementStorer<?>> storerByClass;

    public AccountStorage(Storage storage) {
    	this.storage = storage;
    	storerByTag = new HashMap<String, AccountingElementStorer<?>>();
    	storerByClass = new HashMap<Class<?>, AccountingElementStorer<?>>();
    	registerStorer(IncomeGeneric.class, new IncomeGenericStorer(storage));
    	registerStorer(ExpenseGeneric.class, new ExpenseGenericStorer(storage));
    	registerStorer(DebtLoan.class, new DebtLoanStorer(storage));
    	registerStorer(DebtMortgage.class, new DebtMortgageStorer(storage));
    	registerStorer(Investment401k.class, new Investment401kStorer(storage));
    	registerStorer(InvestmentCheckAcct.class, new InvestmentCheckAcctStorer(storage));
    	registerStorer(InvestmentSavAcct.class, new InvestmentSavAcctStorer(storage));
    }
    
    @Override
    public Account loadAccount() throws AccountFactoryException {
    	Account account = new Account();
    	try {
        	storage.open(OpenState.READ);
    		readAccountData(account);
    		readAccountingElements(account);
    	} catch (StorageException se) {
    		throw new AccountFactoryException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    	mendIncomeWithInvestment401k(account);
    	setCheckingAccount(account);
    	account.createDateList();
    	return account;
    }
    
    @Override
    public void saveAccount(Account account) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		storage.clear();
    		writeAccountData(account);
    		writeAccountingElements(account);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    }

    @Override
	public void saveAccountingElement(AccountingElement accountingElement) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		writeAccountingElement(accountingElement);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
	}
	
    @Override
	public void clear() throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
        	storage.clear();
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
	}

    @Override
    public void removeAccountingElement(AccountingElement accountingElement) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		deleteAccountingElement(accountingElement);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    }

    private void mendIncomeWithInvestment401k(Account account) {
    	for(Investment investment : account.getInvestments()) {
			int income_previous_id = investment.getStoredIncomePreviousId();
			Income income = account.getIncomeByPreviousId(income_previous_id);
			if (income != null) {
	    		investment.setIncome(income);
	    		income.setInvestment401k(investment);
			}
    	}
    }
    
    private void setCheckingAccount(Account account) {
    	for(Investment investment : account.getInvestments()) {
    		if (investment.isCheckingAcct()) {
    	        account.setCheckingAcct((InvestmentCheckAcct) investment);
    	        account.setCheckingAcctPercontrib();
    		}
    	}
    }

    private int[] readIds() throws StorageException {
    	String idsAsString = storage.getString("", ACCOUNTING_ELEMENTS_ID_LIST);
    	String[] idsAsStrings = idsAsString.split(",");
    	int[] ids = new int[idsAsStrings.length];
    	try {
    		for (int i = 0; i < ids.length; i++) {
    			ids[i] = Integer.parseInt(idsAsStrings[i]);
    		}
    	} catch (NumberFormatException nfe) {
    		throw new StorageException("Could not parse ID", nfe);
    	}
    	return ids;
    }

    private void writeIds(Account account) throws StorageException {
    	StringBuilder idsBuilder = new StringBuilder();
    	for (AccountingElement accountingElement : account.getAccountingElements()) {
    		idsBuilder.append(Integer.toString(accountingElement.getId()));
    		idsBuilder.append(',');
    	}
    	idsBuilder.deleteCharAt(idsBuilder.length() - 1);
    	storage.putString("", ACCOUNTING_ELEMENTS_ID_LIST, idsBuilder.toString());
    }

    private void readAccountData(Account account) throws StorageException {
    	account.setSimulationStartYear(storage.getInt("", SIMULATION_START_YEAR));
    	account.setSimulationStartMonth(storage.getInt("", SIMULATION_START_MONTH));
    	account.setCalculationStartYear(storage.getInt("", CALCULATION_START_YEAR));
    	account.setCalculationStartMonth(storage.getInt("", CALCULATION_START_MONTH));    	
    	account.setInvestmentsPercontrib(storage.getBigDecimal("", INVESTMENT_PERCONTRIB));
    	account.setCheckingAcctPercontrib();
    }

    private void writeAccountData(Account account) throws StorageException {
    	storage.putInt("", SIMULATION_START_YEAR, account.getSimulationStartYear());
    	storage.putInt("", SIMULATION_START_MONTH, account.getSimulationStartMonth());
    	storage.putInt("", CALCULATION_START_YEAR, account.getCalculationStartYear());
    	storage.putInt("", CALCULATION_START_MONTH, account.getCalculationStartMonth());
    	storage.putBigDecimal("", INVESTMENT_PERCONTRIB, account.getInvestmentsPercontrib());
    }

    private void readAccountingElements(Account account) throws StorageException {
    	for (int id : readIds()) {
    		account.addAccountingElement(readAccountingElement(id));
    	}
    }

    private void writeAccountingElements(Account account) throws StorageException {
    	writeIds(account);
    	for (AccountingElement accountingElement : account.getAccountingElements()) {
    		writeAccountingElement(accountingElement);
    	}
    }

    private AccountingElement readAccountingElement(int id) throws StorageException {
    	String prefix = Integer.toString(id);
    	String tag = storage.getString(prefix, ACCOUNTING_ELEMENT_CLASS_TAG);
    	AccountingElementStorer<? extends AccountingElement> storer = storerByTag.get(tag);
    	return storer.load(id);
    }

    /**
     * Safety of this method is guaranteed by registerStorer(): under key class K storerByClass
     * holds a storer which accepts instances of K to save().
     */
    @SuppressWarnings("unchecked")
	private void writeAccountingElement(AccountingElement accountingElement) throws StorageException {
    	AccountingElementStorer<? super AccountingElement> storer = (AccountingElementStorer<? super AccountingElement>) storerByClass.get(accountingElement.getClass());
    	String prefix = Integer.toString(accountingElement.getId());
    	storage.putString(prefix, ACCOUNTING_ELEMENT_CLASS_TAG, storer.getTag());
    	storer.save(accountingElement);
    }

    /**
     * Safety of this method is guaranteed by registerStorer(): under key class K storerByClass
     * holds a storer which accepts instances of K to save().
     */
    @SuppressWarnings("unchecked")
	private void deleteAccountingElement(AccountingElement accountingElement) throws StorageException {
    	AccountingElementStorer<? super AccountingElement> storer = (AccountingElementStorer<? super AccountingElement>) storerByClass.get(accountingElement.getClass());
    	String prefix = Integer.toString(accountingElement.getId());
    	storage.remove(prefix, ACCOUNTING_ELEMENT_CLASS_TAG);
    	storer.remove(accountingElement);
    }

    private <T extends AccountingElement> void registerStorer(Class<T> accountingElementClass, AccountingElementStorer<T> storer) {
    	storerByTag.put(storer.getTag(), storer);
    	storerByClass.put(accountingElementClass, storer);
    }
}