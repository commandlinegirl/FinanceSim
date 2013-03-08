package com.codelemma.finances;

enum AcctElements {
	BALANCE(100),
	DEBT(101),
    INCOME(102),
    INVESTMENT(103),    
    EXPENSE(104),
    INVESTMENTSAV(105),
    INVESTMENT401K(106),
    INVESTMENTBOND(107),
    INVESTMENTSTOCK(108),
	ADD(200),
	UPDATE(201),
	DELETE(202);
	
    private int index;
    
	private AcctElements(int index) {
		this.index = index;
	}
	
	public int getNumber() {
		return index; 
	}
		
}
