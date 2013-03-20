package com.codelemma.finances;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.finances.accounting.DebtLoan;
import com.codelemma.finances.accounting.DebtMortgage;
import com.codelemma.finances.accounting.ExpenseGeneric;
import com.codelemma.finances.accounting.IncomeGeneric;
import com.codelemma.finances.accounting.InputListingVisitor;
import com.codelemma.finances.accounting.Investment401k;
import com.codelemma.finances.accounting.InvestmentBond;
import com.codelemma.finances.accounting.InvestmentSavAcct;
import com.codelemma.finances.accounting.InvestmentStock;
import com.codelemma.finances.accounting.ModifyUiVisitor;

public class InputListingUpdater implements InputListingVisitor {

	private SherlockFragmentActivity frgActivity;
	
	public InputListingUpdater(SherlockFragmentActivity sherlockFragmentActivity) {
		this.frgActivity = sherlockFragmentActivity;
	}
	
	@Override
	public void updateInputListingForExpenseGeneric(ExpenseGeneric expense) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.expense_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, expense);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(expense.getName() + " " + (expense.getValue()).toString()); //TODO: 
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForIncomeGeneric(IncomeGeneric income) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.income_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, income);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(income.getName() + " " + (income.getValue()).toString()); //TODO: 
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForInvestmentSavAcct(
			InvestmentSavAcct investment) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.investment_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, investment);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(investment.getName() + " " + (investment.getValue()).toString()); //TODO: 
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForInvestment401k(Investment401k investment) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.investment_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, investment);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(investment.getName() + " " + (investment.getValue()).toString()); //TODO: 
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForInvestmentBond(InvestmentBond investment) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.investment_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, investment);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(investment.getName() + " " + (investment.getValue()).toString()); //TODO: 
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForInvestmentStock(InvestmentStock investment) {

		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.investment_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, investment);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    
		TextView tv = new TextView(frgActivity);
        tv.setText(investment.getName() + " " + (investment.getValue()).toString()); //TODO:         
        tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);      
        
        tip.addView(rLayout);
        addUnderline(tip); 
	}

	@Override
	public void updateInputListingForDebtLoan(DebtLoan debt) {
		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.debt_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, debt);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

		
	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        
		//rLayout.setLayoutParams(layoutParam);		
		
		TextView tv = new TextView(frgActivity);
		tv.setText(debt.getName());
		tv.setId(100);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);	
	    tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		View tv0 = new View(frgActivity);
		tv0.setId(101);
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv0.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv0);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
		TextView tv2 = new TextView(frgActivity);
		tv2.setText("Loan: " + debt.getInitAmount());
		tv2.setId(102);
		tv.setTextColor(Color.parseColor("#FF771100"));
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 101);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);	
	    tv2.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv2);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv4 = new TextView(frgActivity);
		tv4.setText("Total interest: " + debt.getTotalInterestPaid());
		tv4.setId(103);
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.ALIGN_LEFT, 101);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
	    tv4.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv4);    
	    	    

	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		TextView tv6 = new TextView(frgActivity);
		tv6.setId(104);
		relativeParams.addRule(RelativeLayout.BELOW, 101);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv6.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv6);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv3 = new TextView(frgActivity);
		tv3.setText("Payment: " + debt.getMonthlyPayment());
		tv3.setId(105);
		relativeParams.addRule(RelativeLayout.BELOW, 102);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 104);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    tv3.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv3);
	    	    
	   
	                        
        tip.addView(rLayout);
        addUnderline(tip); 

	}

	@Override
	public void updateInputListingForDebtMortgage(DebtMortgage debt) {
		LinearLayout tip = (LinearLayout) frgActivity.findViewById(R.id.debt_summary);
        
		RelativeLayout rLayout = new RelativeLayout(frgActivity);		
        ModifyUiVisitor modifyUiLauncher = new ModifyUiLauncher(frgActivity);
        rLayout.setOnClickListener(new ModifyUiListener(modifyUiLauncher));
        rLayout.setTag(R.string.acct_object, debt);
        
		int padding = Utils.px(frgActivity, 10);
		rLayout.setPadding(padding, padding, padding, padding); // int left, int top, int right, int bottom     

		
	    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        
		//rLayout.setLayoutParams(layoutParam);		
		
		TextView tv = new TextView(frgActivity);
		tv.setText(debt.getName());
		tv.setId(100);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);	
	    tv.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		View tv0 = new View(frgActivity);
		tv0.setId(101);
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv0.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv0);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
		TextView tv2 = new TextView(frgActivity);
		tv2.setText("Amount: " + debt.getLoanAmount());
		tv2.setId(102);
		tv.setTextColor(Color.parseColor("#FF771100"));
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 101);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);	
	    tv2.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv2);
	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv4 = new TextView(frgActivity);
		tv4.setText("Total interest: " + debt.getTotalInterestPaid());
		tv4.setId(103);
		relativeParams.addRule(RelativeLayout.BELOW, 100);
		relativeParams.addRule(RelativeLayout.ALIGN_LEFT, 101);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
	    tv4.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv4);    
	    	    

	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		TextView tv6 = new TextView(frgActivity);
		tv6.setId(104);
		relativeParams.addRule(RelativeLayout.BELOW, 101);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv6.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv6);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv3 = new TextView(frgActivity);
		tv3.setText("Payment: " + debt.getMonthlyPayment());
		tv3.setId(105);
		relativeParams.addRule(RelativeLayout.BELOW, 102);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 104);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    tv3.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv3);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv5 = new TextView(frgActivity);
		tv5.setText("Extra costs: " + debt.getTotalAdditionalCost());
		tv5.setId(106);
		relativeParams.addRule(RelativeLayout.BELOW, 103);
		relativeParams.addRule(RelativeLayout.ALIGN_LEFT, 104);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
	    tv5.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv5);
	                        
	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		TextView tv7 = new TextView(frgActivity);
		tv7.setId(107);
		relativeParams.addRule(RelativeLayout.BELOW, 104);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv7.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv7);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv8 = new TextView(frgActivity);
		tv8.setText("Monthly PMI: " + debt.getPMIAmount());
		tv8.setId(108);
		relativeParams.addRule(RelativeLayout.BELOW, 105);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 107);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    tv8.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv8);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	    TextView tv9 = new TextView(frgActivity);
		tv9.setText("Total payment: " + debt.getTotalPayment());
		tv9.setId(109);
		relativeParams.addRule(RelativeLayout.BELOW, 106);
		relativeParams.addRule(RelativeLayout.ALIGN_LEFT, 107);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
	    tv9.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv9);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(0, 0);
		TextView tv10 = new TextView(frgActivity);
		tv10.setId(110);
		relativeParams.addRule(RelativeLayout.BELOW, 104);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    tv10.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv10);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);	   
		TextView tv11 = new TextView(frgActivity);
		tv11.setText("Monthly Tax: " + debt.getPropertyTaxAmount());
		tv11.setId(111);
		relativeParams.addRule(RelativeLayout.BELOW, 108);
		relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, 110);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    tv11.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv11);
	    	    
	    relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);
		TextView tv12 = new TextView(frgActivity);
		tv12.setText("Monthly insurance: " + debt.getPropertyInsuranceAmount());
		tv12.setId(112);
		relativeParams.addRule(RelativeLayout.BELOW, 109);
		relativeParams.addRule(RelativeLayout.ALIGN_LEFT, 110);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
	    tv12.setLayoutParams(relativeParams);	    
	    rLayout.addView(tv12);	    
	    
	    
	    
	    
        tip.addView(rLayout);
        addUnderline(tip); 

	}

	private void addUnderline(LinearLayout underline) {	    
	    View v = new View(frgActivity);		
	 	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
	 			                                                        Utils.px(frgActivity, 1));	
		v.setLayoutParams(param);
		v.setBackgroundColor(0xFFCCCCCC);	    
	    underline.addView(v);
    }	
}
