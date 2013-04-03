package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Application;
import android.util.Log;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.Money;

public class Finances extends Application {
	      
    private int simStartMonth = -1; // not set yet
    private int simStartYear = -1;  // not set yet
    private int calcStartMonth = -1; // not set yet
    private int calcStartYear = -1;  // not set yet
	private int preCalculationLength = 0; // number of months for pre calculatoins (until simulation (for history) starts)
    private int simulationLength = 360;// 30*12;
	private int totalCalculationLength = 0; // number of months for total calculatoins (simulation + precalculations)	
    private Account account;  
    private History history;    
    private static Finances appInstance;
	private boolean needToRecalculate = true;
	private int currentMenuItem;
	private int numberOfMonthsInChart = 5*12;

    @Override
    public void onCreate() {        
        super.onCreate();
        appInstance = this;
    }
    
    public static Finances getInstance() {
         return appInstance;
    }
    
	public void setNumberOfMonthsInChart(int months) {
		numberOfMonthsInChart = months;
	}

	public int getNumberOfMonthsInChart() {
		return numberOfMonthsInChart;
	}
	
	public void setSimulationStartYear(int simStartYear){
	    this.simStartYear = simStartYear;
	}		

	public void setSimulationStartMonth(int simStartMonth){
	    this.simStartMonth = simStartMonth;
	}			
	
	public int getSimulationStartYear(){
	    return simStartYear;
	}		
	
	public int getSimulationStartMonth(){
	    return simStartMonth;
	}	
	
	public void setCalculationStartYear(int calcStartYear){
	    this.calcStartYear = calcStartYear;
	}		

	public void setCalculationStartMonth(int calcStartMonth){
	    this.calcStartMonth = calcStartMonth;
	}			
	
	public int getCalculationStartYear(){
	    return calcStartYear;
	}		
	
	public int getCalculationStartMonth(){
	    return calcStartMonth;
	}	
	
	public int getTotalCalculationLength() {
		return totalCalculationLength;
	}

	public int getPreCalculationLength() {
		return preCalculationLength;
	}
	
	public void computeCalculationLength() {
		/* Get the total number of months the calculation (ie. advance iteration in Account)
		 * needs to proceed for. It is the sum of simulation time (eg. 30 years) and 
		 * precalculation time (in months) */
        int _preCalculationLength = 0;
        
        int year = calcStartYear;
        int month = calcStartMonth;
        while ((year < simStartYear) || (year == simStartYear && month < simStartMonth)) {  
        	_preCalculationLength++;
        	if (month == 11) {
                month = 0;
                year += 1;
            } else {
                month++;
            }
    	} 
        preCalculationLength = _preCalculationLength;        
        totalCalculationLength =  simulationLength + preCalculationLength;
	}
	
	public boolean needToRecalculate() {
		return needToRecalculate;
	}

	public void needToRecalculate(boolean p) {
		needToRecalculate = p;
	}

	
	public void setAccount(Storage storage) throws ParseException {
		account = new Account(simStartYear, simStartMonth);
		
		String incomes = storage.get(TypedKey.INCOMES.getKeyword(), null);
		
		if (incomes != null) {
			restoreIncomes(incomes);
			Log.d("Finances.setAccount()", incomes);
		} else {
			Log.d("Finances.setAccount()", "incomes is null");
		}
		
		String investments = storage.get(TypedKey.INVESTMENTS.getKeyword(), null);		
		if (investments != null) {
			restoreInvestments(investments);			
		} else {			
	    	InvestmentCheckAcct investment = new InvestmentCheckAcct(
	    			"Checking account", // name
	    			Money.ZERO,         // init_amount
	    			new BigDecimal(30), // tax_rate
	                1,                  // interest capitalization (1 = monthly)
	                new BigDecimal("0.5"), // interest_rate
	                simStartYear,       // calculation start = simulation start
	                simStartMonth);		// calculation start = simulation start	    	
			account.addInvestment(investment);
	    	account.setCheckingAcct(investment);
	    	account.setCheckingAcctPercontrib();
		}				
		
		String expenses = storage.get(TypedKey.EXPENSES.getKeyword(), null);		
		if (investments != null) {
			restoreExpenses(expenses);
		}	
		
		String debts = storage.get(TypedKey.DEBTS.getKeyword(), null);		
		if (debts != null) {
			restoreDebts(debts);
		}			
	}
	
	public void restoreIncomes(String incomes) throws ParseException {
		TypedContainer incomesCont = Serializer.parseToMap(incomes);		
		Iterator<Entry<TypedKey<?>, Object>> i = incomesCont.iterator();	
		/*
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
		    BigDecimal yearly_income = tc.get(TypedKey.YEARLY_INCOME);
            BigDecimal income_tax_rate = tc.get(TypedKey.INCOME_TAX_RATE);        
    	    BigDecimal yearly_income_rise = tc.get(TypedKey.YEARLY_INCOME_RISE);
    	    BigDecimal income_installments = tc.get(TypedKey.INCOME_INSTALLMENTS);
    	    String income_name = tc.get(TypedKey.INCOME_NAME);
    	    
		    IncomeGeneric income = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, //TODO: cannot be ZERO! DivisionByzeroException
                income_name,
		        start_year,
		    	start_month);                 
		    account.addIncome(income);
		}
		*/
	}
	
	
	
	
	public void restoreInvestments(String investments) throws ParseException {
		TypedContainer investmentsCont = Serializer.parseToMap(investments);		
		Iterator<Entry<TypedKey<?>, Object>> i = investmentsCont.iterator();				
		/*while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
			tc.get(TypedKey.getKey("id"));
		    BigDecimal principal = tc.get(TypedKey.INVESTMENT_AMOUNT);
            BigDecimal interest_rate = tc.get(TypedKey.INVESTMENT_INTEREST_RATE);        
    	    BigDecimal tax_rate = tc.get(TypedKey.INVESTMENT_TAX_RATE);
    	    BigDecimal compounding = tc.get(TypedKey.INVESTMENT_COMPOUNDING);
    	    BigDecimal period = tc.get(TypedKey.INVESTMENT_PERIOD);
    	    String name = tc.get(TypedKey.INVESTMENT_NAME);
    	    
        	InvestmentSavAcct investment = new InvestmentSavAcct(principal,
        			interest_rate, 
                    tax_rate,
                    compounding,
                    period,
                    name);
            account.addInvestment(investment);
            if (investment.isCheckingAcct()) {
	    	    account.setCheckingAcct(investment);	    			
	    	}	
		}*/
	}	
	
	public void restoreExpenses(String expenses) throws ParseException {
		TypedContainer expensesCont = Serializer.parseToMap(expenses);		
		Iterator<Entry<TypedKey<?>, Object>> i = expensesCont.iterator();	
		/*
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
    	    String expense_name = tc.get(TypedKey.EXPENSE_NAME);			
		    BigDecimal init_expense = tc.get(TypedKey.INIT_EXPENSE);
            BigDecimal inflation_rate = tc.get(TypedKey.INFLATION_RATE);        
    	    int frequency = tc.get(TypedKey.EXPENSE_FREQUENCY);
    	    
        	ExpenseGeneric expense = new ExpenseGeneric(expense_name,
        			                      init_expense, 
                                          inflation_rate,                                           
                                          frequency);
            account.addExpense(expense);
		}
		*/
	}	

	public void restoreDebts(String debts) throws ParseException {
		TypedContainer debtsCont = Serializer.parseToMap(debts);		
		Iterator<Entry<TypedKey<?>, Object>> i = debtsCont.iterator();	
		/*
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
		    BigDecimal debt_amount = tc.get(TypedKey.DEBT_AMOUNT);
    	    String debt_name = tc.get(TypedKey.DEBT_NAME);
    	    
    		Debt debt = new Debt(debt_name, 
		                         debt_amount);
            account.addDebt(debt);	   		
		}
		*/
	}	

	
	public void setHistory() {
		history = new History(simStartYear, simStartMonth, simulationLength);
	}
	
	
		

	public int getSimulationLength() {
		return simulationLength;
	}
	
	public Account getAccount(){
	    return account;
	}		
	
	public History getHistory(){
	    return history;
	}				
	
	public void setCurrentMenuItem(int id) {
		currentMenuItem = id;
	}
}
