package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class InvestmentSavAcctStorer implements AccountingElementStorer<InvestmentSavAcct> {
	// Class tag
	private static final String INVESTMENT_SAV_ACCT = "isa";
	
	private static final String NAME = "n";
	private static final String INIT_AMOUNT = "ia";
	private static final String TAX_RATE = "tr";
	private static final String PERCONTRIB = "pc";
	private static final String INTEREST_RATE = "ir";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";

	private Storage storage;
	
	public InvestmentSavAcctStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return INVESTMENT_SAV_ACCT;
	}

	@Override
	public InvestmentSavAcct load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		InvestmentSavAcct investment = InvestmentSavAcct.create(
				storage.getString(prefix, NAME),
				storage.getBigDecimal(prefix, INIT_AMOUNT),
				storage.getBigDecimal(prefix, TAX_RATE),
				storage.getBigDecimal(prefix, PERCONTRIB),
				storage.getBigDecimal(prefix, INTEREST_RATE),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		return investment;
	}

	@Override
	public void save(InvestmentSavAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.putString(prefix, NAME, investment.getName());
		storage.putBigDecimal(prefix, INIT_AMOUNT, investment.getInitAmount());
		storage.putBigDecimal(prefix, TAX_RATE, investment.getInitTaxRate());
		storage.putBigDecimal(prefix, PERCONTRIB, investment.getInitPercontrib());
		storage.putBigDecimal(prefix, INTEREST_RATE, investment.getInterestRate());
		storage.putInt(prefix, START_YEAR, investment.getStartYear());
		storage.putInt(prefix, START_MONTH, investment.getStartMonth());
	}

	@Override
	public void remove(InvestmentSavAcct investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, INIT_AMOUNT);
		storage.remove(prefix, TAX_RATE);
		storage.remove(prefix, PERCONTRIB);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
	}
}