package com.codelemma.finances.accounting;

import java.math.BigDecimal;

public class HistoryInvestmentStock implements HistoryNew {

	private BigDecimal[] amount;
	private int listSize = 360; //TODO: take from
	private String name;
	
	public HistoryInvestmentStock(InvestmentStock investmentstock) {		
		amount = new BigDecimal[listSize];

		name = investmentstock.getName();
	}
	
	@Override
	public void add(int index, Object acctElement) {
		// TODO Auto-generated method stub

	}

}
