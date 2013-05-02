package com.codelemma.finances.accounting;

import android.util.Log;

import com.codelemma.finances.accounting.Storage.StorageException;

public class Investment401kStorer implements AccountingElementStorer<Investment401k> {
	// Class tag
	private static final String INVESTMENT_401K = "i4";

	private static final String NAME = "n";
	private static final String INIT_AMOUNT = "ia";
	private static final String PERCONTRIB = "pc";
	private static final String PERIOD = "p";
	private static final String INTEREST_RATE = "ir";
	private static final String INCOME_ID = "ii";
	private static final String WITHDRAWAL_TAX_RATE = "wtr";
	private static final String EMPLOYER_MATCH = "em";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";

	private Storage storage;

	public Investment401kStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return INVESTMENT_401K;
	}

	@Override
	public Investment401k load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		Investment401k investment = Investment401k.create(
				storage.getString(prefix, NAME),
				storage.getBigDecimal(prefix, INIT_AMOUNT),
				storage.getBigDecimal(prefix, PERCONTRIB),
				storage.getInt(prefix, PERIOD),
				storage.getBigDecimal(prefix, INTEREST_RATE),
				null, // after Account has been loaded, Income needs to be set for Investment401k 
				storage.getBigDecimal(prefix, WITHDRAWAL_TAX_RATE),
				storage.getBigDecimal(prefix, EMPLOYER_MATCH),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		investment.setId(id);
		Log.d("income id of 401k", String.valueOf(storage.getInt(prefix, INCOME_ID)));
		investment.setIncomeId(storage.getInt(prefix, INCOME_ID));
		Log.d("investment.getStoredIncomeId()", String.valueOf(investment.getStoredIncomeId()));
		return investment;
	}

	@Override
	public void save(Investment401k investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.putString(prefix, NAME, investment.getName());
		storage.putBigDecimal(prefix, INIT_AMOUNT, investment.getInitAmount());
		storage.putBigDecimal(prefix, PERCONTRIB, investment.getInitPercontrib());
		storage.putInt(prefix, PERIOD, investment.getPeriod());
		storage.putBigDecimal(prefix, INTEREST_RATE, investment.getInitInterestRate());
		storage.putInt(prefix, INCOME_ID, investment.getIncome().getId()); //TODO: handle nullpointerexception when no income attached to investment401k 
		storage.putBigDecimal(prefix, WITHDRAWAL_TAX_RATE, investment.getInitWithdrawalTaxRate());
		storage.putBigDecimal(prefix, EMPLOYER_MATCH, investment.getInitEmployerMatch());
		storage.putInt(prefix, START_YEAR, investment.getStartYear());
		storage.putInt(prefix, START_MONTH, investment.getStartMonth());
	}

	@Override
	public void remove(Investment401k investment) throws StorageException {
		String prefix = Integer.toString(investment.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, INIT_AMOUNT);
		storage.remove(prefix, PERCONTRIB);
		storage.remove(prefix, PERIOD);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, INCOME_ID); 
		storage.remove(prefix, WITHDRAWAL_TAX_RATE);
		storage.remove(prefix, EMPLOYER_MATCH);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
	}
	
	public String getIncomeIdTag() {
		return INCOME_ID;
	}
}