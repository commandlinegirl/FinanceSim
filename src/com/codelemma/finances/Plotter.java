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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.finances.accounting.HistoryCashflows;
import com.codelemma.finances.accounting.HistoryDebtLoan;
import com.codelemma.finances.accounting.HistoryDebtMortgage;
import com.codelemma.finances.accounting.HistoryExpenseGeneric;
import com.codelemma.finances.accounting.HistoryIncomeGeneric;
import com.codelemma.finances.accounting.HistoryInvestment401k;
import com.codelemma.finances.accounting.HistoryInvestmentBond;
import com.codelemma.finances.accounting.HistoryInvestmentSavAcct;
import com.codelemma.finances.accounting.HistoryInvestmentStock;
import com.codelemma.finances.accounting.HistoryNetWorth;
import com.codelemma.finances.accounting.PlotVisitor;

public class Plotter implements PlotVisitor {

	private SherlockFragmentActivity frgActivity;
    private int currentColor = 0;
    private String[] colors = {"#ff00ff00", "#ff0099ff", "#ffFF0080", "#ff8C489F", "#ff9CAA9C", "#ffffff00", "#ff66CCFF",  "#ffcccccc"};    
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
    
            double currentMaxY = series.getMaxY();
            double currentMinY = series.getMinY();    
            if (currentMaxY > maxY) {
                maxY = currentMaxY;
            }   
            if (currentMinY < minY) {
                minY = currentMinY;
            }    
            XYSeriesRenderer renderer = getSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);  
        }   
	
		    
		/* Here set left and right margin for the chart based on max and min values from each series */

    	int rightMargin = Utils.px(frgActivity, 12);
	    int leftMargin = Utils.px(frgActivity, 6);
	    int labelMaxYLen = String.valueOf(maxY).length();
	    int labelMinYLen = String.valueOf(minY).length();
	    if (labelMaxYLen >=  labelMinYLen) {
		    leftMargin += Utils.px(frgActivity, 4) * labelMaxYLen;
	    } else {
	    	leftMargin += Utils.px(frgActivity, 4) * labelMinYLen;
	    }	        

	    
    	mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
    			leftMargin, 
    			Utils.px(frgActivity, 20), 
    			rightMargin}); // top, left, bottom, right
    		
    	int screenWidthDips = Utils.dip(frgActivity, frgActivity.getResources().getDisplayMetrics().widthPixels);
        int chartSize = screenWidthDips - leftMargin - rightMargin;
        int numOfLabels = Math.round(chartSize / 30);
        int i;
        int step = numberOfMonths / numOfLabels;
        if (step < 1) {
	        step = 1;
        }
        for (i = 0; i < numberOfMonths; i += step) { //TODO: check if size == values.size()
	        String date =  dates[i];
	        mRenderer.addXTextLabel(i, date.replaceFirst(" ", "\n"));
        }
                
        mRenderer.setShowLegend(true);
        mRenderer.setLegendHeight(Utils.px(frgActivity, 48));
        mRenderer.setLegendTextSize(Utils.px(frgActivity, 10));
        mRenderer.setInScroll(true);
        
        if (minY >= 0) { // set min and max only if Y values > 0
    	    mRenderer.setYAxisMin(0);
    	    mRenderer.setYAxisMax(maxY + maxY/5);
        }
        
    	customizeMultipleSeriesRenderer(mRenderer, title, 360);
    	
		GraphicalView mChartView = ChartFactory.getLineChartView(frgActivity, dataset, mRenderer);	    	  		    	
        LinearLayout layout = (LinearLayout) frgActivity.findViewById(R.id.pred_chart);	       
        layout.removeAllViews();
        layout.addView(mChartView); 
    }
	
	private void customizeMultipleSeriesRenderer(XYMultipleSeriesRenderer mRenderer, String title, int seriesSize) {
    	
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
        //return mRenderer;
	}
	
	private TimeSeries getSeries(BigDecimal[] values, int item_count, String name) {
	    TimeSeries series = new TimeSeries(name);	    
	    try {
	        for(int i = 0; i < item_count; i++) {
	    	    series.add(i, values[i].doubleValue());
	        }
	    } catch (IndexOutOfBoundsException e) {
	        e.printStackTrace();
	    }
	    return series;
	}	
	
	private XYSeriesRenderer getSeriesRenderer() {
		XYSeriesRenderer renderer = new XYSeriesRenderer();
	   	renderer.setColor(Color.parseColor(colors[currentColor])); //TODO: random (light) color
	   	incrementCurrentColor();	   	
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
		values.put(historyIncomeGeneric.getName(), historyIncomeGeneric.getAmountHistory());
        plot(values, "Income"); 		
	}

	@Override
	public void plotExpenseGeneric(HistoryExpenseGeneric historyExpenseGeneric) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyExpenseGeneric.getName(), historyExpenseGeneric.getAmountHistory());
        plot(values, "Expense"); 				
	}

	@Override
	public void plotInvestment401k(HistoryInvestment401k historyInvestment401k) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestment401k.getName(), historyInvestment401k.getAmountHistory());
        plot(values, "Investment"); 				
	}

	@Override
	public void plotInvestmentSavAcct(
			HistoryInvestmentSavAcct historyInvestmentSavAcct) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestmentSavAcct.getName(), historyInvestmentSavAcct.getAmountHistory());
        plot(values, "Investment"); 				
	}

	@Override
	public void plotInvestmentBond(HistoryInvestmentBond historyInvestmentBond) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestmentBond.getName(), historyInvestmentBond.getAmountHistory());
        plot(values, "Investment"); 				
	}

	@Override
	public void plotInvestmentStock(
			HistoryInvestmentStock historyInvestmentStock) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put(historyInvestmentStock.getName(), historyInvestmentStock.getAmountHistory());
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
		values.put("Interest", historyDebtLoan.getInterestsPaidHistory());
		values.put("Principal", historyDebtLoan.getPrincipalPaidHistory());
		values.put("Outstanding loan", historyDebtLoan.getRemainingAmountHistory());
        plot(values, "Loan"); 		
	}

	@Override
	public void plotCashflows(HistoryCashflows historyCashflows) {
		HashMap<String,BigDecimal[]> values = new HashMap<String,BigDecimal[]>(1);
		values.put("Income", historyCashflows.getIncomeHistory());
		values.put("Capital gains", historyCashflows.getCapitalGainsHistory());
		values.put("Expenses", historyCashflows.getExpensesHistory());
		values.put("Debt rates", historyCashflows.getDebtRatesHistory());
		values.put("Investment rates", historyCashflows.getInvestmentRatesHistory());
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
