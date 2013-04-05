package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Application;
import android.util.Log;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentCheckAcct;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.Money;

public class Finances extends Application {
	      
    private int simStartMonth = -1; // not set yet
    private int simStartYear = -1;  // not set yet
    private int calcStartMonth = -1; // not set yet
    private int calcStartYear = -1;  // not set yet
	private int preCalculationLength = 0; // number of months for pre calculatoins (until simulation (for history) starts)
    private int simulationLength = 600;// 50*12;
	private int totalCalculationLength = 0; // number of months for total calculatoins (simulation + precalculations)	
    private Account account;  
    private History history;    
    private static Finances appInstance;
	private boolean needToRecalculate = true;
	private int numberOfMonthsInChart = 60; //5*12;

	
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
			//restoreIncomes(incomes);
			Log.d("Finances.setAccount()", incomes);
		} else {
			Log.d("Finances.setAccount()", "incomes is null");
		}
		
		String investments = storage.get(TypedKey.INVESTMENTS.getKeyword(), null);		
		if (investments != null) {
			//restoreInvestments(investments);			
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
			//restoreExpenses(expenses);
		}	
		
				
		String debts = storage.get(TypedKey.DEBTS.getKeyword(), null);		
		if (debts != null) {
			//restoreDebts(debts);
		}			
	}
	
	private void restoreIncomes(String incomes) throws ParseException {
		TypedContainer incomesCont = Serializer.parseToMap(incomes);		
		Iterator<Entry<TypedKey<?>, Object>> i = incomesCont.iterator();	
		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
		    BigDecimal yearly_income = tc.get(TypedKey.YEARLY_INCOME);
            BigDecimal income_tax_rate = tc.get(TypedKey.INCOME_TAX_RATE);        
    	    BigDecimal yearly_income_rise = tc.get(TypedKey.YEARLY_INCOME_RISE);
    	    BigDecimal income_installments = tc.get(TypedKey.INCOME_INSTALLMENTS);
    	    String income_name = tc.get(TypedKey.INCOME_NAME);
    	    int start_year = tc.get(TypedKey.INCOME_START_YEAR);
    	    int start_month = tc.get(TypedKey.INCOME_START_MONTH);
    	    
		    IncomeGeneric income = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, 
                income_name,
		        start_year,
		    	start_month);                 
		    account.addIncome(income);
		}
	}			
	
	private void restoreInvestments(String investments) throws ParseException {
		TypedContainer investmentsCont = Serializer.parseToMap(investments);		
		Iterator<Entry<TypedKey<?>, Object>> i = investmentsCont.iterator();		
		
		/* Restore Savings account*/
		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
			tc.get(TypedKey.getKey("id"));
    	    String name = tc.get(TypedKey.INVESTMENTSAV_NAME);
		    BigDecimal amount = tc.get(TypedKey.INVESTMENTSAV_INIT_AMOUNT);
            BigDecimal interest_rate = tc.get(TypedKey.INVESTMENTSAV_INTEREST_RATE);        
    	    BigDecimal tax_rate = tc.get(TypedKey.INVESTMENTSAV_TAX_RATE);
    	    Integer capitalization = tc.get(TypedKey.INVESTMENTSAV_CAPITALIZATION);
    	    BigDecimal percontrib = tc.get(TypedKey.INVESTMENTSAV_PERCONTRIB);    	    
    	    Integer start_year = tc.get(TypedKey.INVESTMENTSAV_START_YEAR);
    	    Integer start_month = tc.get(TypedKey.INVESTMENTSAV_START_MONTH);
    	        	    
        	InvestmentSavAcct investment = new InvestmentSavAcct(name,
        			amount,
        			tax_rate,
        			percontrib,
                    capitalization,
                    interest_rate,
                    start_year,
                    start_month);
            account.addInvestment(investment);
		}
		
		/* Restore 401k account*/

		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
			tc.get(TypedKey.getKey("id"));
    	    String name = tc.get(TypedKey.INVESTMENT401K_NAME);
		    BigDecimal amount = tc.get(TypedKey.INVESTMENT401K_INIT_AMOUNT);
    	    BigDecimal percontrib = tc.get(TypedKey.INVESTMENT401K_PERCONTRIB);    	    
    	    Integer term = tc.get(TypedKey.INVESTMENT401K_PERIOD);
            BigDecimal interest_rate = tc.get(TypedKey.INVESTMENT401K_INTEREST_RATE);
            Integer income_id = tc.get(TypedKey.INVESTMENT401K_INCOMEID);
    	    BigDecimal withdrawal_tax_rate = tc.get(TypedKey.INVESTMENT401K_WITHDRAWAL_TAX_RATE);   	    
    	    BigDecimal employer_match = tc.get(TypedKey.INVESTMENT401K_EMPLOYER_MATCH);
    	    Integer start_year = tc.get(TypedKey.INVESTMENT401K_START_YEAR);
    	    Integer start_month = tc.get(TypedKey.INVESTMENT401K_START_MONTH);
    	        	        	    
            Income income = account.getIncomeById(income_id); //TODO: if income == null???
    	    
    	    Investment401k investment = new Investment401k(name,
        			amount,
        			percontrib,
        			term,
        			interest_rate,
        			income,
        			withdrawal_tax_rate,        			
        			employer_match,                    
                    start_year,
                    start_month);
            account.addInvestment(investment);
            
            
            
		}
		

	
	/* Restore checking account*/

	
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
			tc.get(TypedKey.getKey("id"));
    	    String name = tc.get(TypedKey.INVESTMENTCHECK_NAME);
		    BigDecimal amount = tc.get(TypedKey.INVESTMENTCHECK_INIT_AMOUNT);
            BigDecimal interest_rate = tc.get(TypedKey.INVESTMENTCHECK_INTEREST_RATE);        
    	    BigDecimal tax_rate = tc.get(TypedKey.INVESTMENTCHECK_TAX_RATE);
    	    Integer capitalization = tc.get(TypedKey.INVESTMENTCHECK_CAPITALIZATION);
    	    Integer start_year = tc.get(TypedKey.INVESTMENTCHECK_START_YEAR);
    	    Integer start_month = tc.get(TypedKey.INVESTMENTCHECK_START_MONTH);    	    
    	    
        	InvestmentCheckAcct investment = new InvestmentCheckAcct(name,
        			amount,
        			tax_rate,
                    capitalization,
                    interest_rate,
                    start_year,
                    start_month);
            account.addInvestment(investment);
            //if (investment.isCheckingAcct()) {
	    	//    account.setCheckingAcct(investment);	    			
	        //}	
		}
		
	}	
	
	private void restoreExpenses(String expenses) throws ParseException {
		TypedContainer expensesCont = Serializer.parseToMap(expenses);		
		Iterator<Entry<TypedKey<?>, Object>> i = expensesCont.iterator();	
		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
    	    String expense_name = tc.get(TypedKey.EXPENSE_NAME);			
		    BigDecimal init_expense = tc.get(TypedKey.INIT_EXPENSE);
            BigDecimal inflation_rate = tc.get(TypedKey.INFLATION_RATE);        
    	    int frequency = tc.get(TypedKey.EXPENSE_FREQUENCY);
    	    int start_year = tc.get(TypedKey.EXPENSE_START_YEAR);
    	    int start_month = tc.get(TypedKey.EXPENSE_START_MONTH);
    	    
        	ExpenseGeneric expense = new ExpenseGeneric(expense_name,
        			                      init_expense, 
                                          inflation_rate,                                           
                                          frequency,
                                          start_year,
                                          start_month);
            account.addExpense(expense);
		}		
	}	

	private void restoreDebts(String debts) throws ParseException {
		TypedContainer debtsCont = Serializer.parseToMap(debts);		
		Iterator<Entry<TypedKey<?>, Object>> i = debtsCont.iterator();	
		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());		    
    	    String debt_name = tc.get(TypedKey.DEBTLOAN_NAME);
		    BigDecimal debt_amount = tc.get(TypedKey.DEBTLOAN_AMOUNT);
		    BigDecimal interest_rate = tc.get(TypedKey.DEBTLOAN_INTEREST_RATE);
		    Integer term = tc.get(TypedKey.DEBTLOAN_TERM);
		    BigDecimal extra_payment = tc.get(TypedKey.DEBTLOAN_EXTRA_PAYMENT);
    	    int start_year = tc.get(TypedKey.DEBTLOAN_START_YEAR);
    	    int start_month = tc.get(TypedKey.DEBTLOAN_START_MONTH);
    	    
    		DebtLoan debt = new DebtLoan(debt_name, 
		                         debt_amount,
		                         interest_rate,
		                         term,
		                         extra_payment,
		                         start_year,
		                         start_month);
            account.addDebt(debt);	   		
		}
		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());		    
    	    String debt_name = tc.get(TypedKey.DEBTMORTGAGE_NAME);
		    BigDecimal purchase_price = tc.get(TypedKey.DEBTMORTGAGE_PURCHASE_PRICE);
		    BigDecimal downpayment = tc.get(TypedKey.DEBTMORTGAGE_DOWNPAYMENT);		    
		    BigDecimal interest_rate = tc.get(TypedKey.DEBTMORTGAGE_INTEREST_RATE);
		    Integer term = tc.get(TypedKey.DEBTMORTGAGE_TERM);
		    BigDecimal insurance = tc.get(TypedKey.DEBTMORTGAGE_PROPERTY_INSURANCE);
		    BigDecimal property_tax = tc.get(TypedKey.DEBTMORTGAGE_PROPERTY_TAX);
		    BigDecimal pmi = tc.get(TypedKey.DEBTMORTGAGE_PMI);		    
    	    int start_year = tc.get(TypedKey.DEBTMORTGAGE_START_YEAR);
    	    int start_month = tc.get(TypedKey.DEBTMORTGAGE_START_MONTH);
    	        	    
    		DebtMortgage debt = new DebtMortgage(debt_name, 
		                         purchase_price,
		                         downpayment,
		                         interest_rate,
		                         term,
		                         insurance,
		                         property_tax,
		                         pmi,
		                         start_year,
		                         start_month);    		   		    		
            account.addDebt(debt);
		}
		
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
}
