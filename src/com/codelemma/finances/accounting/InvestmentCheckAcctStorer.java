package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class InvestmentCheckAcctStorer implements AccountingElementStorer<InvestmentCheckAcct> {
	// Class tag
	private static final String INVESTMENT_CHECK_ACCT = "ica";

	private static final String NAME = "n";
	private static final String INIT_AMOUNT = "ia";
	private static final String TAX_RATE = "tr";
	private static final String CAPITALIZATION = "c";
	private static final String INTEREST_RATE = "ir";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";

	private Storage storage;

	public InvestmentCheckAcctStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return INVESTMENT_CHECK_ACCT;
	}

	@Override
	public InvestmentCheckAcct load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		InvestmentCheckAcct investment = InvestmentCheckAcct.create(
				storage.getString(prefix, NAME),
				storage.getBigDecimal(prefix, INIT_AMOUNT),
				storage.getBigDecimal(prefix, TAX_RATE),
				storage.getInt(prefix, CAPITALIZATION),
				storage.getBigDecimal(prefix, INTEREST_RATE),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		return investment;
	}

	@Override
	public void save(InvestmentCheckAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.putString(prefix, NAME, investment.getName());
		storage.putBigDecimal(prefix, INIT_AMOUNT, investment.getInitAmount());
		storage.putBigDecimal(prefix, TAX_RATE, investment.getInitTaxRate());
		storage.putInt(prefix, CAPITALIZATION, investment.getCapitalization());
		storage.putBigDecimal(prefix, INTEREST_RATE, investment.getInitInterestRate());
		storage.putInt(prefix, START_YEAR, investment.getStartYear());
		storage.putInt(prefix, START_MONTH, investment.getStartMonth());
	}

	@Override
	public void remove(InvestmentCheckAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, INIT_AMOUNT);
		storage.remove(prefix, TAX_RATE);
		storage.remove(prefix, CAPITALIZATION);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
	}
}