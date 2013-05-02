package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

public class DebtMortgageStorer implements AccountingElementStorer<DebtMortgage> {
	// Class tag
	private static final String DEBT_MORTGAGE = "dm";
	
	private static final String NAME = "n";
	private static final String PURCHASE_PRICE = "pp";
	private static final String DOWNPAYMENT = "dp";
	private static final String INTEREST_RATE = "ir";
	private static final String TERM = "t";
	private static final String PROPERTY_INSURANCE = "pi";
	private static final String PROPERTY_TAX = "pt";
	private static final String PMI = "p";
	private static final String START_YEAR = "sy";
	private static final String START_MONTH = "sm";
	
	private Storage storage;
	
	public DebtMortgageStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return DEBT_MORTGAGE;
	}
	
	@Override
	public DebtMortgage load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		DebtMortgage mortgage = DebtMortgage.create(
				storage.getString(prefix, NAME),
				storage.getBigDecimal(prefix, PURCHASE_PRICE),
				storage.getBigDecimal(prefix, DOWNPAYMENT),
				storage.getBigDecimal(prefix, INTEREST_RATE),
				storage.getInt(prefix, TERM),
				storage.getBigDecimal(prefix, PROPERTY_INSURANCE),
				storage.getBigDecimal(prefix, PROPERTY_TAX),
				storage.getBigDecimal(prefix, PMI),
				storage.getInt(prefix, START_YEAR),
				storage.getInt(prefix, START_MONTH));
		mortgage.setId(id);
		return mortgage;
	}
	
	@Override
	public void save(DebtMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.putString(prefix, NAME, mortgage.getName());
		storage.putBigDecimal(prefix, PURCHASE_PRICE, mortgage.getPurchasePrice());
		storage.putBigDecimal(prefix, DOWNPAYMENT, mortgage.getDownpayment());
		storage.putBigDecimal(prefix, INTEREST_RATE, mortgage.getInterestRate());
		storage.putInt(prefix, TERM, mortgage.getTerm());
		storage.putBigDecimal(prefix, PROPERTY_INSURANCE, mortgage.getPropertyInsurance());
		storage.putBigDecimal(prefix, PROPERTY_TAX, mortgage.getPropertyTax());
		storage.putBigDecimal(prefix, PMI, mortgage.getPMI());
		storage.putInt(prefix, START_YEAR, mortgage.getStartYear());
		storage.putInt(prefix, START_MONTH, mortgage.getStartMonth());
	}

	@Override
	public void remove(DebtMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.remove(prefix, NAME);
		storage.remove(prefix, PURCHASE_PRICE);
		storage.remove(prefix, DOWNPAYMENT);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, TERM);
		storage.remove(prefix, PROPERTY_INSURANCE);
		storage.remove(prefix, PROPERTY_TAX);
		storage.remove(prefix, PMI);
		storage.remove(prefix, START_YEAR);
		storage.remove(prefix, START_MONTH);
	}
}
