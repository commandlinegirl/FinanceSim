package com.codelemma.finances.accounting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Account {
    private int simStartMonth = -1;
    private int simStartYear = -1;
    private int calcStartMonth = -1;
    private int calcStartYear = -1;
	private int preCalculationLength = 0; // number of months for pre calculations (until simulation (for history) starts)
    private int simulationLength = 600;// 50 years * 12;
	private int totalCalculationLength = 600; // number of months for total calculations (simulation + precalculations)
	private Months[] months = Months.values(); 
	private String[] dates; // each element is of "MMM YYYY" format
	
    private BigDecimal investmentsPercontrib;
    private BigDecimal checkingAcctPercontrib;
	private InvestmentCheckAcct checkingAcct; 	
    
    private ArrayList<Income> incomes = new ArrayList<Income>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private ArrayList<Investment> investments = new ArrayList<Investment>();
    private ArrayList<Debt> debts = new ArrayList<Debt>();
    private HistoryCashflows cashflows = new HistoryCashflows("Cashflows (per month)");
    private HistoryNetWorth net_worth = new HistoryNetWorth("Net worth (cumulative)");    
    
    public Account() {
    	investmentsPercontrib = Money.ZERO;
    	checkingAcctPercontrib = Money.ZERO;
    	dates = new String[simulationLength];    	    	
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
	
	public int getSimulationLength() {
		return simulationLength;
	}
	
	public void computeCalculationLength() {
		/* Get the total number of months the calculation (ie. advance iteration in Account)
		 * needs to proceed for. It is the sum of simulation time (eg. 30 years) and 
		 * pre-calculation time (in months) */
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
    
    public void createDateList() {
    	int i;
    	int y = simStartYear;
    	int m = simStartMonth;
    	for (i = 0; i < simulationLength; i++) {
    		dates[i] = months[m]+" "+y;
    		if (m == 11) {
                m = 0;
                y += 1;
            } else {
                m++;
            }        	
    	}    	    	
    }
	
    public String[] getDates() {
    	return dates;
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
        expenses.add(expense);
    }   

    public void addInvestment(Investment investment) {
    	Preconditions.checkNotNull(investment, "Missing investment");
        investments.add(investment);        
    }   

    public void addIncome(Income income) {
        Preconditions.checkNotNull(income, "Missing income");    	
        incomes.add(income);       
    }

    public void addDebt(Debt debt) {
    	Preconditions.checkNotNull(debt, "Missing debt");
        debts.add(debt);       
    }

    public void addAccountingElement(AccountingElement accountingElement) {
    	Preconditions.checkNotNull(accountingElement, "Missing accounting element");
    	accountingElement.addToAccount(this);
    }

    public void removeIncome(Income income) {
    	if (income != null) {
    		incomes.remove(income);
    	}    	    	
    }

    public void removeExpense(Expense expense) {
    	if (expense != null) {
    		expenses.remove(expense);
    	}
    }

    public void removeInvestment(Investment investment) {
    	if (investment != null) {
    		investments.remove(investment);
    	}    	
    }

    public void removeDebt(Debt debt) {
    	if (debt != null) {
    		debts.remove(debt);
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
    
    private static class ConcatenatedIterator<T> implements Iterator<T> {
    	private List<Iterator<? extends T>> iterators;
    	private int current;
    	
    	public ConcatenatedIterator(List<Iterator<? extends T>> iterators) {
    		this.iterators = iterators;
    		this.current = 0;
    		advanceToNonempty();
    	}
    	
    	private void advanceToNonempty() {
    		while (current < iterators.size() && !iterators.get(current).hasNext()) current++;
    	}
    	
    	@Override
    	public boolean hasNext() {
    		return current < iterators.size();
    	}
    	
    	@Override
    	public T next() {
    		if (current >= iterators.size()) {
    			throw new NoSuchElementException();
    		}
    		T nextValue = iterators.get(current).next();
    		advanceToNonempty();
    		return nextValue;
    	}
    	
    	@Override
    	public void remove() {
    		throw new UnsupportedOperationException();
    	}
    }
    
    /**
     * Returns an iterable that goes over all accounting elements.
     */
    public Iterable<AccountingElement> getAccountingElements() {
    	return new Iterable<AccountingElement>() {
    		@Override
    		public Iterator<AccountingElement> iterator() {
    			List<Iterator<? extends AccountingElement>> iterators = new ArrayList<Iterator<? extends AccountingElement>>(4);
    			iterators.add(incomes.iterator());
    			iterators.add(expenses.iterator());
    			iterators.add(investments.iterator());
    			iterators.add(debts.iterator());
    			return new ConcatenatedIterator<AccountingElement>(iterators);
    		}
    	};
    }
    
    public Income getIncomeById(int id) {
        for (Income income: incomes) {
           if (income.getId() == id) {
               return income;
           }
        }
   	    return null;
   	}

    public Expense getExpenseById(int id) {
       for (Expense expense: expenses) {
           if (expense.getId() == id) {
               return expense;
           }
       } 	
   	   return null;
   	}
    
    public Investment getInvestmentById(int id) {
       for (Investment investment: investments) {
           if (investment.getId() == id) {
               return investment;
           }
       }
   	   return null;    
   	}

    public Debt getDebtById(int id) {
        for (Debt debt: debts) {
             if (debt.getId() == id) {
            	 return debt;
             }
        }    	
    	return null;
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