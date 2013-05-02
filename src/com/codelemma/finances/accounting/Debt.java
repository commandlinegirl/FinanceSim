package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Debt extends AccountingElement {
	public abstract BigDecimal getInitAmount();
	public abstract BigDecimal getMonthlyPayment();         
	public abstract void advance(int year, int month);

	@Override
	public void addToAccount(Account account) {
		account.addDebt(this);
	}
}
