package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class InvestmentCheckAcctStorer implements AccountingElementStorer<InvestmentCheckAcct> {
	// Class tag
	private static final String INVESTMENT_CHECK_ACCT = "ica";

	private static final String NAME = "n";
	private static final String INIT_AMOUNT = "ia";
	private static final String TAX_RATE = "tr";
	private static final String INTEREST_RATE = "ir";

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
				storage.getBigDecimal(prefix, INTEREST_RATE));
		return investment;
	}

	@Override
	public void save(InvestmentCheckAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.putString(prefix, NAME, investment.getName());
		storage.putBigDecimal(prefix, INIT_AMOUNT, investment.getInitAmount());
		storage.putBigDecimal(prefix, TAX_RATE, investment.getInitTaxRate());
		storage.putBigDecimal(prefix, INTEREST_RATE, investment.getInitInterestRate());
	}

	@Override
	public void remove(InvestmentCheckAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, INIT_AMOUNT);
		storage.remove(prefix, TAX_RATE);
		storage.remove(prefix, INTEREST_RATE);
	}
}