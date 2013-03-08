package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Application;
import android.util.Log;

import com.codelemma.finances.accounting.Account;
import com.codelemma.finances.accounting.Debt;
import com.codelemma.finances.accounting.Expense;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.Income;
import com.codelemma.finances.accounting.InvestmentSavAcct;

public class Finances extends Application {
	      
    private int month = -1; // not set yet
    private int year = -1;  // not set yet
    private Account account;  
    private History history;    
    private int listSize = 360;// 30*12;
    private static Finances appInstance;
	private boolean needToRecalculate = true;

    @Override
    public void onCreate() {        
        super.onCreate();
        appInstance = this;
    }
    
    public static Finances getInstance() {
         return appInstance;
    }
       
	public void setYear(int year){
	    this.year = year;
	}		

	public void setMonth(int month){
	    this.month = month;
	}			
	
	public boolean needToRecalculate() {
		return needToRecalculate;
	}

	public void needToRecalculate(boolean p) {
		needToRecalculate = p;
	}

	
	public void setAccount(Storage storage) throws ParseException {
		account = new Account();
		
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
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
		    BigDecimal yearly_income = tc.get(TypedKey.YEARLY_INCOME);
            BigDecimal income_tax_rate = tc.get(TypedKey.INCOME_TAX_RATE);        
    	    BigDecimal yearly_income_rise = tc.get(TypedKey.YEARLY_INCOME_RISE);
    	    BigDecimal income_installments = tc.get(TypedKey.INCOME_INSTALLMENTS);
    	    String income_name = tc.get(TypedKey.INCOME_NAME);
    	    
		    Income income = new Income(yearly_income, 
                income_tax_rate, 
                yearly_income_rise,
                income_installments, //TODO: cannot be ZERO! DivisionByzeroException
                income_name);
		    account.addIncome(income);
		}
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
		}*/
	}	
	
	public void restoreExpenses(String expenses) throws ParseException {
		TypedContainer expensesCont = Serializer.parseToMap(expenses);		
		Iterator<Entry<TypedKey<?>, Object>> i = expensesCont.iterator();		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
    	    String expense_name = tc.get(TypedKey.EXPENSE_NAME);			
		    BigDecimal init_expense = tc.get(TypedKey.INIT_EXPENSE);
            BigDecimal inflation_rate = tc.get(TypedKey.INFLATION_RATE);        
    	    int frequency = tc.get(TypedKey.EXPENSE_FREQUENCY);
    	    
        	Expense expense = new Expense(expense_name,
        			                      init_expense, 
                                          inflation_rate,                                           
                                          frequency);
            account.addExpense(expense);
		}
	}	

	public void restoreDebts(String debts) throws ParseException {
		TypedContainer debtsCont = Serializer.parseToMap(debts);		
		Iterator<Entry<TypedKey<?>, Object>> i = debtsCont.iterator();		
		while (i.hasNext()) {
			TypedContainer tc = ((TypedContainer) i.next().getValue());
		    BigDecimal debt_amount = tc.get(TypedKey.DEBT_AMOUNT);
    	    String debt_name = tc.get(TypedKey.DEBT_NAME);
    	    
    		Debt debt = new Debt(debt_name, 
		                         debt_amount);
            account.addDebt(debt);	   		
		}
	}	

	
	public void setHistory() {
		history = new History(year, month, listSize);
	}
	
	
	public int getYear(){
	    return year;
	}		
	
	public int getMonth(){
	    return month;
	}			

	public int getListSize() {
		return listSize;
	}
	
	public Account getAccount(){
	    return account;
	}		
	
	public History getHistory(){
	    return history;
	}				
}
