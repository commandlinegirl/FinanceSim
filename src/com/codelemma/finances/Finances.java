package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Application;
import android.preference.PreferenceManager;

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
import com.codelemma.finances.accounting.SafeAccountFactory;
import com.codelemma.finances.accounting.SafeStorageAccountFactory;
import com.codelemma.finances.accounting.Storage;

public class Finances extends Application {
    private static Finances appInstance;
	private SafeAccountFactory accountFactory;
    private Account account;
    private History history;    
	private boolean needToRecalculate = true;
	private int numberOfMonthsInChart = 60; //5*12;
	//needed to save the user's spinner setting across chart & table fragments	 
    private int spinnerPosition = 0;     
	
    @Override
    public void onCreate() {        
        super.onCreate();
        Preconditions.check(appInstance == null, "appInstance already set");
        appInstance = this;
		Storage storage = StorageFactory.create(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
        accountFactory = new SafeStorageAccountFactory(storage);
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
	
	public boolean needToRecalculate() {
		return needToRecalculate;
	}

	public void needToRecalculate(boolean p) {
		needToRecalculate = p;
	}

	public void setAccount() {
		// account = accountFactory.createAccount(simStartYear, simStartMonth);
		// AleZ: removed simStartYear, simStartMonth
		account = accountFactory.createAccount();
	}
	
	public void saveAccount() {
		//TODO
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
    	    int income_term = tc.get(TypedKey.INCOME_TERM);
    	    int start_year = tc.get(TypedKey.INCOME_START_YEAR);
    	    int start_month = tc.get(TypedKey.INCOME_START_MONTH);

		    IncomeGeneric income = new IncomeGeneric(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, 
                income_name,
                income_term,
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
		history = new History(account.getSimulationStartYear(),
				account.getSimulationStartMonth(),
				account.getSimulationLength());
	}
	
	public int getSpinnerPosition() {
		return spinnerPosition;
	}
	
	public void setSpinnerPosition(int pos) {
		spinnerPosition = pos;
	}

	public Account getAccount(){
	    return account;
	}		

	public History getHistory(){
	    return history;
	}
}
