package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class DebtLoanStorer implements AccountingElementStorer<DebtLoan> {
	// Class tag
	private static final String DEBT_LOAN = "dl";
	
	private static final String NAME = "n";
	private static final String AMOUNT = "a";
	private static final String INTEREST_RATE = "ir";
	private static final String TERM = "t";
	private static final String EXTRA_PAYMENT = "ep";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";
	
	private Storage storage;
	
	public DebtLoanStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return DEBT_LOAN;
	}
	
	/**
	 * Instantiates a debt representing a loan using data read from storage
	 * keyed with a given prefix. Assumes storage has been opened for reading.
	 */
	@Override
	public DebtLoan load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		DebtLoan loan = DebtLoan.create(
				storage.getString(prefix, NAME),
	    		storage.getBigDecimal(prefix, AMOUNT),
	    		storage.getBigDecimal(prefix, INTEREST_RATE),
	    		storage.getInt(prefix, TERM),
	    		storage.getBigDecimal(prefix, EXTRA_PAYMENT),
	    		storage.getInt(prefix, START_YEAR),
	    		storage.getInt(prefix, START_MONTH));
		return loan;
	}
	
	/**
	 * Saves a debt representing a loan by writing its data to storage under
	 * keys with a given prefix. Assumes storage has been opened for writing.
	 */
	@Override
	public void save(DebtLoan loan) throws StorageException {
		String prefix = Integer.toString(loan.getId());
		storage.putString(prefix, NAME, loan.getName());
		storage.putBigDecimal(prefix, AMOUNT, loan.getInitAmount());
		storage.putBigDecimal(prefix, INTEREST_RATE, loan.getInterestRate());
		storage.putInt(prefix, TERM, loan.getTerm());
		storage.putBigDecimal(prefix, EXTRA_PAYMENT, loan.getExtraPayment());
		storage.putInt(prefix, START_YEAR, loan.getStartYear());
		storage.putInt(prefix, START_MONTH, loan.getStartMonth());
	}
	
	/**
	 * Removes a debt representing a loan and keyed with a given prefix from
	 * storage. Assumes storage has been opened for writing.
	 */
	@Override
	public void remove(DebtLoan loan) throws StorageException {
		String prefix = Integer.toString(loan.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, AMOUNT);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, TERM);
		storage.remove(prefix, EXTRA_PAYMENT);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
	}
}