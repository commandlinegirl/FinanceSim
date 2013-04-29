package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import android.util.SparseArray;

public class Account {
    private int simStartYear;
    private int simStartMonth;
    private BigDecimal investmentsPercontrib;
    private BigDecimal checkingAcctPercontrib;
	private InvestmentCheckAcct checkingAcct; 	

	// To be refactored away. They won't be needed once each accounting element class
	// assumes responsibility for assigning its own instance ids.
    private int income_id = 0;
    private int expense_id = 0;
    private int investment_id = 0;
    private int debt_id = 0;
    
    private SparseArray<Income> income_ids = new SparseArray<Income>();
    private SparseArray<Expense> expense_ids = new SparseArray<Expense>();
    private SparseArray<Investment> investment_ids = new SparseArray<Investment>();
    private SparseArray<Debt> debt_ids = new SparseArray<Debt>();
    
    private ArrayList<Income> incomes = new ArrayList<Income>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private ArrayList<Investment> investments = new ArrayList<Investment>();
    private ArrayList<Debt> debts = new ArrayList<Debt>();

    private HistoryCashflows cashflows = new HistoryCashflows("Cashflows (per month)");
    private HistoryNetWorth net_worth = new HistoryNetWorth("Net worth (cumulative)");    
    
    public Account(int year, int month) {
    	simStartYear = year;
    	simStartMonth = month;
    	investmentsPercontrib = Money.ZERO;
    	checkingAcctPercontrib = Money.ZERO;
    }
    
    public BigDecimal getInvestmentsPercontrib() {
    	return investmentsPercontrib;
    }
    

    public void addToInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = investmentsPercontrib.add(newPercentage);
    }

    public void subtractFromInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = investmentsPercontrib.subtract(newPercentage);
    }
    
    public void setInvestmentsPercontrib(BigDecimal newPercentage) {
    	investmentsPercontrib = newPercentage;
    }
    
    public void setCheckingAcctPercontrib() {
    	checkingAcctPercontrib = (new BigDecimal(100)).subtract(investmentsPercontrib);
    }
    
    public BigDecimal getCheckingAcctPercontrib() {
    	return checkingAcctPercontrib;
    }
    
    public void addExpense(Expense expense) {
    	Preconditions.checkNotNull(expense, "Missing expense");
    	expense.setId(expense_id);        
        expenses.add(expense);
        expense_ids.put(expense_id, expense);
        expense_id++;
    }   

    public void addInvestment(Investment investment) {
    	Preconditions.checkNotNull(investment, "Missing investment");
    	investment.setId(investment_id);        
        investments.add(investment);        
        investment_ids.put(investment_id, investment);        
        investment_id++;
    }   

    public void addIncome(Income income) {
        Preconditions.checkNotNull(income, "Missing income");    	
    	income.setId(income_id);
        incomes.add(income);
        income_ids.put(income_id, income);
        income_id++;        
    }

    public void addDebt(Debt debt) {
    	Preconditions.checkNotNull(debt, "Missing debt");
    	debt.setId(debt_id);        
        debts.add(debt);
        debt_ids.put(debt_id, debt);
        debt_id++;        
    }   
           
    // These four methods simply add an accounting element without doing other unrelated work.
    // They should be removed once add*() are refactored to only do adding.
    // Currently they should only be used for accounting element objects which already have an id.

    public void justAddExpense(Expense expense) {
    	Preconditions.checkNotNull(expense, "Missing expense");
        expenses.add(expense);
        expense_ids.put(expense.getId(), expense); // May be removed in refactoring.
    }   

    public void justAddInvestment(Investment investment) {
    	Preconditions.checkNotNull(investment, "Missing investment");
        investments.add(investment);
        investment_ids.put(investment.getId(), investment);
    }   

    public void justAddIncome(Income income) {
        Preconditions.checkNotNull(income, "Missing income");    	
        incomes.add(income);
        income_ids.put(income.getId(), income);
    }

    public void justAddDebt(Debt debt) {
    	Preconditions.checkNotNull(debt, "Missing debt");
        debts.add(debt);
        debt_ids.put(debt.getId(), debt);
    }
    
    public void addAccountingElement(AccountingElement accountingElement) {
    	Preconditions.checkNotNull(accountingElement, "Missing accounting element");
    	accountingElement.addToAccount(this);
    }

    public void removeIncome(Income income) {
    	if (income != null) {
    		incomes.remove(income);
        	income_ids.remove(income.getId());    		
    	}    	    	
    }

    public void removeExpense(Expense expense) {
    	if (expense != null) {
    		expenses.remove(expense);
        	expense_ids.remove(expense.getId());
    	}
    }

    public void removeInvestment(Investment investment) {
    	if (investment != null) {
    		investments.remove(investment);
        	investment_ids.remove(investment.getId());
    	}    	
    }

    public void removeDebt(Debt debt) {
    	if (debt != null) {
    		debts.remove(debt);
        	debt_ids.remove(debt.getId());
    	}
    }
    
    public int getIncomesSize() {
    	return incomes.size();
    }    

    public int getExpensesSize() {
    	return expenses.size();
    }    

    public int getInvestmentsSize() {
    	return investments.size();
    }    

    public int getDebtsSize() {
    	return debts.size();
    }    

    public Iterable<Income> getIncomes() {
    	return Collections.unmodifiableList(incomes);
    }
   
    public Iterable<Expense> getExpenses() {
    	return Collections.unmodifiableList(expenses);
    }

    public Iterable<Investment> getInvestments() {
    	return Collections.unmodifiableList(investments);
    }

    public Iterable<Debt> getDebts() {
    	return Collections.unmodifiableList(debts);
    }   
    
    public Income getIncomeById(int id) {
    	return income_ids.get(id);
    }
    
    public Expense getExpenseById(int id) {
    	return expense_ids.get(id);
    }
    
    public Investment getInvestmentById(int id) {
    	return investment_ids.get(id);
    }
    
    public Debt getDebtById(int id) {
    	return debt_ids.get(id);
    }
        
    public void setInitDebt() {
        for (Debt debt: debts) {
        	debt.setValuesBeforeCalculation();
        }
    }

    public void setInitExpense() {
        for (Expense expense: expenses) {
        	expense.setValuesBeforeCalculation();
        }
    }
    
    public void setInitIncome() {
        for (Income income: incomes) {
            income.setValuesBeforeCalculation();
        }
    }

    public void setInitInvestment() {
        for (Investment investment: investments) {
            investment.setValuesBeforeCalculation();
        }
    }
    
    public void setInitCashflow() {
    	cashflows.initialize();
    }
    
    public void setInitNetworth() {
    	net_worth.initialize();
    }
    
    public BigDecimal advanceDebt(int index, int year, int month) {
    	BigDecimal total_debt = Money.ZERO;
        for (Debt debt: debts) {
    		debt.advance(year, month);   		
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		debt.getHistory().add(index, debt, cashflows, net_worth);
        	}
            total_debt = total_debt.add(debt.getMonthlyPayment());
        }
        return total_debt;
    }  
    
    public BigDecimal advanceExpense(int index, int year, int month) {
    	BigDecimal total_expense = Money.ZERO;
        for (Expense expense: expenses) {
        	expense.advance(year, month);
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		expense.getHistory().add(index, expense, cashflows);
        	}
            total_expense = total_expense.add(expense.getAmount()); 
        }
        return total_expense;
    }
    
    public BigDecimal advanceIncome(int index, int year, int month) {
    	BigDecimal total_income = Money.ZERO;
    	
        for (Income income: incomes) {
            income.advance(year, month, checkingAcct);
    		if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		income.getHistory().add(index, income, cashflows);
        		Investment401k inv401k = income.getInvestment401k();
        		if (inv401k != null) {
        		    inv401k.getHistory().add(index, inv401k, cashflows, net_worth); //401k is incremented (advanced) within
        		}
        	}
            total_income = total_income.add(income.getNetIncome()); // total net income
        }
        return total_income;
    }
    
    public void advanceInvestment(int index, BigDecimal excess, int year, int month) {
        BigDecimal capitalGains = Money.ZERO;
        for (Investment investment: investments) {
        	if (!investment.isPreTax()) { // posttax investment (taken from excess money), pretax is incremented in Income!        	
        		                		
        		investment.advance(year, month, excess, checkingAcctPercontrib);
        		// here add capital gains to excess
        		capitalGains = capitalGains.add(investment.getInterestsNet());
        		       		                       
    		    if ((year > simStartYear) || (year == simStartYear && month >= simStartMonth)) {
        		    investment.getHistory().add(index, investment, cashflows, net_worth);
        	    }
        	}
        }
    }
        
    public void advance(int index, int year, int month) {
    	// year and month - starting date of predictions (saved to history here)
        Preconditions.checkInBounds(month, 0, 11, "Month must be in 0..11");
        BigDecimal excess = Money.ZERO; //= prevMonthCapitalGains;
                
        BigDecimal total_income = advanceIncome(index, year, month);
        excess = excess.add(total_income); // total net income
        
        BigDecimal total_debt = advanceDebt(index, year, month);
        excess = excess.subtract(total_debt);

        BigDecimal total_expense = advanceExpense(index, year, month);        
        excess = excess.subtract(total_expense);
                
        advanceInvestment(index, excess, year, month);
    }
    
    public void addToHistory(History history) {
    	for (Investment investment: investments) {
    		history.addInvestmentHistory(investment.getHistory());
    	}
    	
    	for (Income income: incomes) {
    		history.addIncomeHistory(income.getHistory());
    	}
    	
    	for (Expense expense: expenses) {
    		history.addExpenseHistory(expense.getHistory());
    	}
    	
    	for (Debt debt: debts) {
    		history.addDebtHistory(debt.getHistory());
    	}
    	
    	history.addCashflowHistory(cashflows);
    	history.addNetWorthHistory(net_worth);
    }
    
	public void clearHistory(History history) {
		history.clear();
	}
	
	public void setCheckingAcct(InvestmentCheckAcct investment) {
		checkingAcct = investment;
	}
	
	public InvestmentCheckAcct getCheckingAcct() {
		return checkingAcct;
	}
}