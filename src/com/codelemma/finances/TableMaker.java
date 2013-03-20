package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.ArrayList;


import android.util.Log;
import android.view.Gravity;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.finances.accounting.HistoryDebtLoan;
import com.codelemma.finances.accounting.HistoryDebtMortgage;
import com.codelemma.finances.accounting.HistoryExpenseGeneric;
import com.codelemma.finances.accounting.HistoryIncomeGeneric;
import com.codelemma.finances.accounting.HistoryInvestment401k;
import com.codelemma.finances.accounting.HistoryInvestmentBond;
import com.codelemma.finances.accounting.HistoryInvestmentSavAcct;
import com.codelemma.finances.accounting.HistoryInvestmentStock;
import com.codelemma.finances.accounting.TableVisitor;

public class TableMaker implements TableVisitor {

	private SherlockFragmentActivity frgActivity;
	
	public TableMaker(SherlockFragmentActivity sherlockFragmentActivity) {
		this.frgActivity = sherlockFragmentActivity;
	}
	
	@Override
	public void makeTableExpense(HistoryExpenseGeneric historyExpenseGeneric) {
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Expense after inflation");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupExpenseGeneric> ExpListItems = setGroupsExpenseGeneric(historyExpenseGeneric);
    	ListAdapterExpenseGeneric ExpAdapter = new ListAdapterExpenseGeneric(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);                
	}
	


	public ArrayList<ListGroupExpenseGeneric> setGroupsExpenseGeneric(HistoryExpenseGeneric historyExpenseGeneric) {
    	ArrayList<ListGroupExpenseGeneric> list = new ArrayList<ListGroupExpenseGeneric>();
    	ArrayList<ListChildExpenseGeneric> list2 = new ArrayList<ListChildExpenseGeneric>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] net_expenses = historyExpenseGeneric.getAmountHistory();

    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupExpenseGeneric gru1 = new ListGroupExpenseGeneric();
    	int i = 0;
		gru1.setDate(dates[i]);
		gru1.setNetExpense(net_expenses[i].toString());
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    			ListChildExpenseGeneric ch1_1 = new ListChildExpenseGeneric();
    			ch1_1.setDate(dates[i]);
    			ch1_1.setNetExpense(net_expenses[i].toString());
	            list2.add(ch1_1);
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildExpenseGeneric>();
    			gru1 = new ListGroupExpenseGeneric();
    			gru1.setDate(dates[i]);
    			gru1.setNetExpense(net_expenses[i].toString());
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
		
    }		
	
	@Override
    public void makeTableIncome(HistoryIncomeGeneric historyIncomeGeneric) {
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Gross income");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Tax");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Net Income");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);		
		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupIncomeGeneric> ExpListItems = setGroupsIncomeGeneric(historyIncomeGeneric);
    	ListAdapterIncomeGeneric ExpAdapter = new ListAdapterIncomeGeneric(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);                
	}
	


	public ArrayList<ListGroupIncomeGeneric> setGroupsIncomeGeneric(HistoryIncomeGeneric historyIncomeGeneric) {
    	ArrayList<ListGroupIncomeGeneric> list = new ArrayList<ListGroupIncomeGeneric>();
    	ArrayList<ListChildIncomeGeneric> list2 = new ArrayList<ListChildIncomeGeneric>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] gross_incomes = historyIncomeGeneric.getGrossIncomeHistory();
    	BigDecimal[] taxes = historyIncomeGeneric.getTaxHistory();
    	BigDecimal[] net_incomes = historyIncomeGeneric.getNetIncomeHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupIncomeGeneric gru1 = new ListGroupIncomeGeneric();
    	int i = 0;
    	gru1.setDate(dates[i]);
		gru1.setGrossIncome(gross_incomes[i].toString());    	
    	gru1.setTax(taxes[i].toString());
		gru1.setNetIncome(net_incomes[i].toString()); 
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    			ListChildIncomeGeneric ch1_1 = new ListChildIncomeGeneric();
    			ch1_1.setDate(dates[i]);
    			ch1_1.setGrossIncome(gross_incomes[i].toString());    	
    			ch1_1.setTax(taxes[i].toString());
    			ch1_1.setNetIncome(net_incomes[i].toString());
	            list2.add(ch1_1);
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildIncomeGeneric>();
    			gru1 = new ListGroupIncomeGeneric();
    	    	gru1.setDate(dates[i]);
    			gru1.setGrossIncome(gross_incomes[i].toString());    	
    	    	gru1.setTax(taxes[i].toString());
    			gru1.setNetIncome(net_incomes[i].toString());
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
		
    }			

	@Override
	public void makeTableInvestment401k(HistoryInvestment401k historyInvestment401k) {
				
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;                 
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Salary");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Employee \n contrib");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Employer \n contrib");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Total");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv); 

    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupInvestment401k> ExpListItems = setGroupsInvestment401k(historyInvestment401k);
    	ListAdapterInvestment401k ExpAdapter = new ListAdapterInvestment401k(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);
	}
	
    public ArrayList<ListGroupInvestment401k> setGroupsInvestment401k(HistoryInvestment401k historyInvestment401k) {
    	    	
    	ArrayList<ListGroupInvestment401k> list = new ArrayList<ListGroupInvestment401k>();
    	ArrayList<ListChildInvestment401k> list2 = new ArrayList<ListChildInvestment401k>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] amounts = historyInvestment401k.getAmountHistory();
    	BigDecimal[] employeeContributions = historyInvestment401k.getEmployeeContributionHistory();
    	BigDecimal[] employerContributions = historyInvestment401k.getEmployerContributionHistory();
    	BigDecimal[] salary = historyInvestment401k.getSalaryHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupInvestment401k gru1 = new ListGroupInvestment401k();
    	int i = 0;
    	gru1.setDate(dates[i]);
    	gru1.setSalary(salary[i].toString());
		gru1.setEmployeeContribution(employeeContributions[i].toString());
		gru1.setEmployerContribution(employerContributions[i].toString());
		gru1.setAmount(amounts[i].toString()); 
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    		    ListChildInvestment401k ch1_1 = new ListChildInvestment401k();
    		    ch1_1.setDate(dates[i]);
	            ch1_1.setSalary(salary[i].toString());
	            ch1_1.setEmployeeContribution(employeeContributions[i].toString());
	            ch1_1.setEmployerContribution(employerContributions[i].toString());
	            ch1_1.setAmount(amounts[i].toString());
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildInvestment401k>();
    			gru1 = new ListGroupInvestment401k();
    			gru1.setDate(dates[i]);
    			gru1.setSalary(salary[i].toString());
    			gru1.setEmployeeContribution(employeeContributions[i].toString());
    			gru1.setEmployerContribution(employerContributions[i].toString());
    			gru1.setAmount(amounts[i].toString()); 			
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
    }

	@Override
	public void makeTableInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct) {
		
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Deposit");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Tax");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Net interests");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Total");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv); 

		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupInvestmentSavAcct> ExpListItems = setGroupsInvestmentSavAcct(historyInvestmentSavAcct);
    	ListAdapterInvestmentSavAcct ExpAdapter = new ListAdapterInvestmentSavAcct(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);
	}
	
    public ArrayList<ListGroupInvestmentSavAcct> setGroupsInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct) {
    	ArrayList<ListGroupInvestmentSavAcct> list = new ArrayList<ListGroupInvestmentSavAcct>();
    	ArrayList<ListChildInvestmentSavAcct> list2 = new ArrayList<ListChildInvestmentSavAcct>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] amounts = historyInvestmentSavAcct.getAmountHistory();
    	BigDecimal[] taxes = historyInvestmentSavAcct.getTaxHistory();
    	BigDecimal[] net_interests = historyInvestmentSavAcct.getNetInterestsHistory();
    	BigDecimal[] contributions = historyInvestmentSavAcct.getContributionHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupInvestmentSavAcct gru1 = new ListGroupInvestmentSavAcct();
    	int i = 0;
    	gru1.setDate(dates[i]);
    	gru1.setTax(taxes[i].toString());
		gru1.setNetInterests(net_interests[i].toString());
		gru1.setContribution(contributions[i].toString());
		gru1.setAmount(amounts[i].toString()); 
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    		    ListChildInvestmentSavAcct ch1_1 = new ListChildInvestmentSavAcct();
    		    ch1_1.setDate(dates[i]);
    		    ch1_1.setTax(taxes[i].toString());
    		    ch1_1.setNetInterests(net_interests[i].toString());
    		    ch1_1.setContribution(contributions[i].toString());
    		    ch1_1.setAmount(amounts[i].toString()); 
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildInvestmentSavAcct>();
    			gru1 = new ListGroupInvestmentSavAcct();
    			gru1.setDate(dates[i]);
    	    	gru1.setTax(taxes[i].toString());
    			gru1.setNetInterests(net_interests[i].toString());
    			gru1.setContribution(contributions[i].toString());
    			gru1.setAmount(amounts[i].toString()); 		
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
    }


	@Override
	public void makeTableInvestmentBond(HistoryInvestmentBond historyInvestmentBond) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeTableInvestmentStock(HistoryInvestmentStock historyInvestmentStock) {
		
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Dividends");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Capital Gains \n Yield");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Tax");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv);		
		
		tv = new TextView(frgActivity);
		tv.setText("Total");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		header.addView(tv); 

		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupInvestmentStock> ExpListItems = setGroupsInvestmentStock(historyInvestmentStock);
    	ListAdapterInvestmentStock ExpAdapter = new ListAdapterInvestmentStock(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);                
	}
	


	public ArrayList<ListGroupInvestmentStock> setGroupsInvestmentStock(HistoryInvestmentStock historyInvestmentStock) {
    	ArrayList<ListGroupInvestmentStock> list = new ArrayList<ListGroupInvestmentStock>();
    	ArrayList<ListChildInvestmentStock> list2 = new ArrayList<ListChildInvestmentStock>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] amounts = historyInvestmentStock.getAmountHistory();
    	BigDecimal[] taxes = historyInvestmentStock.getTaxHistory();
    	BigDecimal[] yields = historyInvestmentStock.getCapitalGainYieldHistory();
    	BigDecimal[] dividends = historyInvestmentStock.getDividendsHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupInvestmentStock gru1 = new ListGroupInvestmentStock();
    	int i = 0;
    	gru1.setDate(dates[i]);
    	gru1.setTax(taxes[i].toString());
		gru1.setCapitalGainYield(yields[i].toString());
		gru1.setDividends(dividends[i].toString());
		gru1.setAmount(amounts[i].toString()); 
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    			ListChildInvestmentStock ch1_1 = new ListChildInvestmentStock();
    			ch1_1.setDate(dates[i]);
    			ch1_1.setTax(taxes[i].toString());
    			ch1_1.setCapitalGainYield(yields[i].toString());
    			ch1_1.setDividends(dividends[i].toString());
    			ch1_1.setAmount(amounts[i].toString()); 
	            list2.add(ch1_1);
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildInvestmentStock>();
    			gru1 = new ListGroupInvestmentStock();
    	    	gru1.setDate(dates[i]);
    	    	gru1.setTax(taxes[i].toString());
    			gru1.setCapitalGainYield(yields[i].toString());
    			gru1.setDividends(dividends[i].toString());
    			gru1.setAmount(amounts[i].toString()); 	
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
    }

	@Override
	public void makeTableDebtMortgage(HistoryDebtMortgage historyDebtMortgage) {
		// TODO Auto-generated method stub
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv);

		tv = new TextView(frgActivity);
		tv.setText("Principal");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Interest");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Total\ninterest");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
			
		tv = new TextView(frgActivity);
		tv.setText("Tax, PMI,\ninsurance");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Remaining \n amount");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
	

		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupDebtMortgage> ExpListItems = setGroupsDebtMortgage(historyDebtMortgage);
    	ListAdapterDebtMortgage ExpAdapter = new ListAdapterDebtMortgage(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);
	}
	
    public ArrayList<ListGroupDebtMortgage> setGroupsDebtMortgage(HistoryDebtMortgage historyDebtMortgage) {
    	ArrayList<ListGroupDebtMortgage> list = new ArrayList<ListGroupDebtMortgage>();
    	ArrayList<ListChildDebtMortgage> list2 = new ArrayList<ListChildDebtMortgage>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] interestsPaid = historyDebtMortgage.getInterestsPaidHistory();
    	BigDecimal[] totalInterests = historyDebtMortgage.getTotalInterestsHistory();
    	BigDecimal[] principalPaid = historyDebtMortgage.getPrincipalPaidHistory();
    	BigDecimal[] additionalCost = historyDebtMortgage.getAdditionalCostHistory();
    	BigDecimal[] remainingAmount = historyDebtMortgage.getRemainingAmountHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupDebtMortgage gru1 = new ListGroupDebtMortgage();
    	int i = 0;
    	gru1.setDate(dates[i]);
		gru1.setInterestsPaid(interestsPaid[i].toString()); 
		gru1.setTotalInterests(totalInterests[i].toString()); 
		gru1.setPrincipalPaid(principalPaid[i].toString()); 
		gru1.setAdditionalCost(additionalCost[i].toString());
		gru1.setRemainingAmount(remainingAmount[i].toString()); 
		i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    		    ListChildDebtMortgage ch1_1 = new ListChildDebtMortgage();
    		    ch1_1.setDate(dates[i]);
    		    ch1_1.setInterestsPaid(interestsPaid[i].toString()); 
    		    ch1_1.setTotalInterests(totalInterests[i].toString()); 
    		    ch1_1.setPrincipalPaid(principalPaid[i].toString()); 
    		    ch1_1.setAdditionalCost(additionalCost[i].toString());
    		    ch1_1.setRemainingAmount(remainingAmount[i].toString());  
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildDebtMortgage>();
    			gru1 = new ListGroupDebtMortgage();
    			gru1.setDate(dates[i]);
    			gru1.setInterestsPaid(interestsPaid[i].toString()); 
    			gru1.setTotalInterests(totalInterests[i].toString()); 
    			gru1.setPrincipalPaid(principalPaid[i].toString()); 
    			gru1.setAdditionalCost(additionalCost[i].toString());
    			gru1.setRemainingAmount(remainingAmount[i].toString()); 		
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
    }

	@Override
	public void makeTableDebtLoan(HistoryDebtLoan historyDebtLoan) {
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv);
				
		tv = new TextView(frgActivity);
		tv.setText("Principal");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Interest");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Total\ninterest");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
				
		tv = new TextView(frgActivity);
		tv.setText("Remaining \n amount");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<ListGroupDebtLoan> ExpListItems = setGroupsDebtLoan(historyDebtLoan);
    	ListAdapterDebtLoan ExpAdapter = new ListAdapterDebtLoan(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);
	}
	
    public ArrayList<ListGroupDebtLoan> setGroupsDebtLoan(HistoryDebtLoan historyDebtLoan) {
    	ArrayList<ListGroupDebtLoan> list = new ArrayList<ListGroupDebtLoan>();
    	ArrayList<ListChildDebtLoan> list2 = new ArrayList<ListChildDebtLoan>();

    	String[] dates = ((Main) frgActivity).getDates();
    	BigDecimal[] interestsPaid = historyDebtLoan.getInterestsPaidHistory();
    	BigDecimal[] totalInterests = historyDebtLoan.getTotalInterestsHistory();
    	BigDecimal[] principalPaid = historyDebtLoan.getPrincipalPaidHistory();
    	BigDecimal[] remainingAmount = historyDebtLoan.getRemainingAmountHistory();
    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(3);
    	String currYear;
    	ListGroupDebtLoan gru1 = new ListGroupDebtLoan();
    	int i = 0;
    	gru1.setDate(dates[i]);
		gru1.setInterestsPaid(interestsPaid[i].toString()); 
		gru1.setTotalInterests(totalInterests[i].toString()); 
		gru1.setPrincipalPaid(principalPaid[i].toString()); 
		gru1.setRemainingAmount(remainingAmount[i].toString()); 
		
        i++;
    	while(i < datesLen) {    		
    		currYear = dates[i].substring(3);
    		if (prevYear.equals(currYear)) {
    			ListChildDebtLoan ch1_1 = new ListChildDebtLoan();
    		    ch1_1.setDate(dates[i]);
    		    ch1_1.setInterestsPaid(interestsPaid[i].toString()); 
    		    ch1_1.setTotalInterests(totalInterests[i].toString()); 
    		    ch1_1.setPrincipalPaid(principalPaid[i].toString()); 
    		    ch1_1.setRemainingAmount(remainingAmount[i].toString());  
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ListChildDebtLoan>();
    			gru1 = new ListGroupDebtLoan();
    			gru1.setDate(dates[i]);
    			gru1.setInterestsPaid(interestsPaid[i].toString()); 
    			gru1.setTotalInterests(totalInterests[i].toString()); 
    			gru1.setPrincipalPaid(principalPaid[i].toString()); 
    			gru1.setRemainingAmount(remainingAmount[i].toString()); 
    		}
    		i++;
    		prevYear = currYear;
    	}    	    	        
        return list;
    }
}
