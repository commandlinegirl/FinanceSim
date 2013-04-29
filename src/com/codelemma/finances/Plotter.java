package com.codelemma.finances;

import java.math.BigDecimal;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.finances.accounting.HistoryCashflows;
import com.codelemma.finances.accounting.HistoryDebtLoan;
import com.codelemma.finances.accounting.HistoryDebtMortgage;
import com.codelemma.finances.accounting.HistoryExpenseGeneric;
import com.codelemma.finances.accounting.HistoryIncomeGeneric;
import com.codelemma.finances.accounting.HistoryInvestment401k;
import com.codelemma.finances.accounting.HistoryInvestmentCheckAcct;
import com.codelemma.finances.accounting.HistoryInvestmentSavAcct;
import com.codelemma.finances.accounting.HistoryNetWorth;
import com.codelemma.finances.accounting.Money;
import com.codelemma.finances.accounting.PlotVisitor;

public class Plotter implements PlotVisitor {

	private SherlockFragmentActivity frgActivity;
    private int currentColor = 0;
    private String[] colors = {"#ff00ff00", "#ff0099ff", "#ffFF0080", "#ff8C489F", 
    		                   "#ff9CAA9C", "#ffffff00", "#ff66CCFF",  "#ffcccccc"};    
    private String[] dates;
    
	public Plotter(SherlockFragmentActivity sherlockFragmentActivity, String[] dates) {
		this.frgActivity = sherlockFragmentActivity;
		this.dates = dates;
	}
		
	public void plot(HashMap<String,BigDecimal[]> lists, String title) {		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();		  
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		
		int numberOfMonths = Finances.getInstance().getNumberOfMonthsInChart();
		
        currentColor = 0; 
		
	    double maxY = 0;
	    double minY = 0;
	   
		LinearLayout legendTV = (LinearLayout) frgActivity.findViewById(R.id.legend);		
        legendTV.removeAllViews();
        
	    boolean first = true;	    
		for (HashMap.Entry<String,BigDecimal[]> entry: lists.entrySet()) {
			String name = entry.getKey();
			BigDecimal[] values = entry.getValue();
        
            TimeSeries series = getSeries(values, 
                    numberOfMonths, 
                    name);
            dataset.addSeries(series);
    
            if (first) {
                minY = series.getMinY();
                maxY = series.getMaxY();
            }   
            first = false;
            maxY = Math.max(series.getMaxY(), maxY);
            minY = Math.min(series.getMinY(), minY);                        
               
            XYSeriesRenderer renderer = getSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);
            
            /* Set legend */
            TextView legend = new TextView(frgActivity);            
    		legend.setText("-" + name);
    		legend.setTextSize(10);
    		legend.setPadding(4, 0, 0, 0);
    		legend.setTextColor(Color.parseColor(colors[currentColor]));
            legendTV.addView(legend);
            
            incrementCurrentColor();
        }   				    
		/* Here set left and right margin for the chart based on max and min values from each series */

    	int rightMargin = Utils.px(frgActivity, 12);
	    int leftMargin = Utils.px(frgActivity, 7);
	    int labelMaxYLen = String.valueOf(maxY).length();
	    int labelMinYLen = String.valueOf(minY).length();
	    if (labelMaxYLen >=  labelMinYLen) {
		    leftMargin += Utils.px(frgActivity, 4) * labelMaxYLen;
	    } else {
	    	leftMargin += Utils.px(frgActivity, 4) * labelMinYLen;
	    }	        
	    
    	mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
    			leftMargin, 
    			Utils.px(frgActivity, 15), 
    			rightMargin}); // top, left, bottom, right
    		
    	int screenWidthDips = Utils.dip(frgActivity, frgActivity.getResources().getDisplayMetrics().widthPixels);
        int chartSize = screenWidthDips - leftMargin - rightMargin;
        int numOfLabels = Math.round(chartSize / 30);
        int i;
        int step = Math.max(1, numberOfMonths / numOfLabels);
        for (i = 0; i < numberOfMonths; i += step) { //TODO: check if size == values.size()
	        String date =  dates[i];
	        mRenderer.addXTextLabel(i, date.replaceFirst(" ", "\n"));
        }
                
        mRenderer.setShowLegend(false);
        //mRenderer.setLegendHeight(Utils.px(frgActivity, 48));
        //mRenderer.setLegendTextSize(Utils.px(frgActivity, 10));
        mRenderer.setInScroll(true);
        
        /* If there is no condition set, negative values will be above positive on the chart
         * (min value will be set to 0, max value will be set to negative number */
        if (maxY > 0) {
    	    mRenderer.setYAxisMin(0.0);
    	    mRenderer.setYAxisMax(maxY + maxY/5);
        }
        
    	customizeMultipleSeriesRenderer(mRenderer, title);
    	
		GraphicalView mChartView = ChartFactory.getLineChartView(frgActivity, dataset, mRenderer);	    	  		    	
        LinearLayout layout = (LinearLayout) frgActivity.findViewById(R.id.pred_chart);	         
        layout.removeAllViews();
        layout.setBackgroundResource(R.drawable.chart_bkg);
        layout.addView(mChartView); 
    }
	
	private void customizeMultipleSeriesRenderer(XYMultipleSeriesRenderer mRenderer, String title) {
    	
    	mRenderer.setAxesColor(Color.WHITE);
        mRenderer.setAxisTitleTextSize(Utils.px(frgActivity, 10));    	
    	mRenderer.setMarginsColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));    	
    	mRenderer.setPanEnabled(false, false);
    	mRenderer.setZoomEnabled(false, false);
    	mRenderer.setShowGridX(true);
        mRenderer.setGridColor(0x66CCCCCC);                       
        mRenderer.setLabelsColor(Color.DKGRAY);
        mRenderer.setXLabels(0);       
        mRenderer.setYLabels(15);        
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.RIGHT, 0);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);              
        mRenderer.setPointSize(Utils.px(frgActivity, 2));          
    	int labelsTextSize = Utils.px(frgActivity, 10);
    	mRenderer.setLabelsTextSize(labelsTextSize);       
    	mRenderer.setPanEnabled(false, true);
    	mRenderer.setZoomEnabled(false, true);    	
        //return mRenderer;
	}
	
	private TimeSeries getSeries(BigDecimal[] values, int item_count, String name) {
	    TimeSeries series = new TimeSeries(name);	    
	    	    
	    try {
	        for(int i = 0; i < item_count; i++) {
	    	    series.add(i, values[i].setScale(0, Money.ROUNDING_MODE).doubleValue());
	        }
	    } catch (IndexOutOfBoundsException e) {
	        e.printStackTrace();
	    }
	    return series;
	}	
	
	private XYSeriesRenderer getSeriesRenderer() {
		XYSeriesRenderer renderer = new XYSeriesRenderer();
	   	renderer.setColor(Color.parseColor(colors[currentColor])); //TODO: random (light) color	   	
    	renderer.setLineWidth(Utils.px(frgActivity, 2));        	
		return renderer;
	}
 	
	private void incrementCurrentColor() {
		if (currentColor == colors.length - 1) {
			currentColor = 0;
		} else {
			currentColor++;
		}		
	}
	
	@Override
	public void plotIncomeGeneric(HistoryIncomeGeneric historyIncomeGeneric) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put("Net income (per month)", historyIncomeGeneric.getNetIncomeHistory());
		values.put("Gross income (per month)", historyIncomeGeneric.getGrossIncomeHistory());
        plot(values, "Income"); 		
	}

	@Override
	public void plotExpenseGeneric(HistoryExpenseGeneric historyExpenseGeneric) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyExpenseGeneric.getName()+ " (per month)", historyExpenseGeneric.getAmountHistory());
        plot(values, "Expense"); 				
	}

	@Override
	public void plotInvestment401k(HistoryInvestment401k historyInvestment401k) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestment401k.getName()+ " (cumulative)", historyInvestment401k.getAmountHistory());
        plot(values, "Investment"); 				
	}

	@Override
	public void plotInvestmentSavAcct(HistoryInvestmentSavAcct historyInvestmentSavAcct) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestmentSavAcct.getName()+ " (cumulative)", historyInvestmentSavAcct.getAmountHistory());
        plot(values, "Investment"); 				
	}

	@Override
	public void plotInvestmentCheckAcct(HistoryInvestmentCheckAcct historyInvestmentCheckAcct) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestmentCheckAcct.getName()+ " (cumulative)", historyInvestmentCheckAcct.getAmountHistory());
        plot(values, "Investment"); 						
	}
	
	@Override
	public void plotDebtMortgage(HistoryDebtMortgage historyDebtMortgage) {         		        
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put("Total Interest", historyDebtMortgage.getTotalInterestsHistory());
		//values.put("Payment", historyDebtMortgage.getMonthlyPaymentHistory());
		values.put("Outstanding loan", historyDebtMortgage.getRemainingAmountHistory());
        plot(values, "Mortgage"); 				
	}

	@Override
	public void plotDebtLoan(HistoryDebtLoan historyDebtLoan) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);		
		values.put("Outstanding loan", historyDebtLoan.getRemainingAmountHistory());
		values.put("Total interest", historyDebtLoan.getTotalInterestsHistory());
        plot(values, "Loan"); 		
	}

	@Override
	public void plotCashflows(HistoryCashflows historyCashflows) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put("Income", historyCashflows.getNetIncomeHistory());
		values.put("Capital gains", historyCashflows.getCapitalGainsHistory());
		values.put("Expenses", historyCashflows.getExpensesHistory());
		values.put("Debt service", historyCashflows.getDebtRatesHistory());
		values.put("Saved", historyCashflows.getInvestmentRatesHistory());
        plot(values, "Cashflows"); 			
	}

	@Override
	public void plotNetWorth(HistoryNetWorth historyNetWorth) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put("Savings", historyNetWorth.getSavingsHistory());
		values.put("Outstanding debts", historyNetWorth.getOutstandingDebtsHistory());
		values.put("Net worth", historyNetWorth.getNetWorthHistory());
        plot(values, "Net worth");		
	}
}
