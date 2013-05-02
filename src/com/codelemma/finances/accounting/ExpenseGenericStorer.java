package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class ExpenseGenericStorer implements AccountingElementStorer<ExpenseGeneric> {
	// Class tag
	private static final String EXPENSE_GENERIC = "eg";

	private static final String NAME = "n";
	private static final String INIT_EXPENSE = "ie";
	private static final String INFLATION_RATE = "ir";
	private static final String FREQUENCY = "f";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";

	private Storage storage;

	public ExpenseGenericStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return EXPENSE_GENERIC;
	}

	@Override
	public ExpenseGeneric load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		ExpenseGeneric expense = ExpenseGeneric.create(
				storage.getString(prefix, NAME),
				storage.getBigDecimal(prefix, INIT_EXPENSE),
				storage.getBigDecimal(prefix, INFLATION_RATE),
				storage.getInt(prefix, FREQUENCY),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		expense.setId(id);
		return expense;
	}

	@Override
	public void save(ExpenseGeneric expense) throws StorageException {
		String prefix = Integer.toString(expense.getId());
		storage.putString(prefix, NAME, expense.getName());
		storage.putBigDecimal(prefix, INIT_EXPENSE, expense.getValue());
		storage.putBigDecimal(prefix, INFLATION_RATE, expense.getInitInflationRate());
		storage.putInt(prefix, FREQUENCY, expense.getFrequency());
		storage.putInt(prefix, START_YEAR, expense.getStartYear());
		storage.putInt(prefix, START_MONTH, expense.getStartMonth());
	}

	@Override
	public void remove(ExpenseGeneric expense) throws StorageException {
		String prefix = Integer.toString(expense.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, INIT_EXPENSE);
		storage.remove(prefix, INFLATION_RATE);
		storage.remove(prefix, FREQUENCY);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);	}
}