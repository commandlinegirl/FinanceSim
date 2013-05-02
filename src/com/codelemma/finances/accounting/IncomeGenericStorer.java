package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class IncomeGenericStorer implements AccountingElementStorer<IncomeGeneric> {
	// Class tag
	private static final String INCOME_GENERIC = "ig";

	private static final String INIT_INCOME = "ii";
	private static final String TAX_RATE = "tr";
	private static final String RISE_RATE = "rr";
	private static final String INSTALLMENTS = "i";
	private static final String NAME = "n";
	private static final String TERM = "t";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";

	private Storage storage;

	public IncomeGenericStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return INCOME_GENERIC;
	}

	@Override
	public IncomeGeneric load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		IncomeGeneric income = IncomeGeneric.create(
				storage.getBigDecimal(prefix, INIT_INCOME),
				storage.getBigDecimal(prefix, TAX_RATE),
				storage.getBigDecimal(prefix, RISE_RATE),
				storage.getBigDecimal(prefix, INSTALLMENTS),
				storage.getString(prefix, NAME),
				storage.getInt(prefix, TERM),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		income.setId(id);
		return income;
	}

	@Override
	public void save(IncomeGeneric income) throws StorageException {
		String prefix = Integer.toString(income.getId());
		storage.putBigDecimal(prefix, INIT_INCOME, income.getValue());
		storage.putBigDecimal(prefix, TAX_RATE, income.getInitTaxRate());
		storage.putBigDecimal(prefix, RISE_RATE, income.getInitRiseRate());
		storage.putBigDecimal(prefix, INSTALLMENTS, income.getInitInstallments());
		storage.putString(prefix, NAME, income.getName());
		storage.putInt(prefix, TERM, income.getTerm());
		storage.putInt(prefix, START_YEAR, income.getStartYear());
		storage.putInt(prefix, START_MONTH, income.getStartMonth());
    }

	@Override
	public void remove(IncomeGeneric income) throws StorageException {
		String prefix = Integer.toString(income.getId());
		storage.remove(prefix, INIT_INCOME);
		storage.remove(prefix, TAX_RATE);
		storage.remove(prefix, RISE_RATE);
		storage.remove(prefix, INSTALLMENTS);
		storage.remove(prefix, NAME);
		storage.remove(prefix, TERM);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
    }
}