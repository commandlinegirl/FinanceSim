package com.codelemma.finances;

import java.math.BigDecimal;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.HistoryInvestment;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FrgChart extends SherlockFragment {
 
	private View toggleYrs1;
	private View toggleYrs5;
	private View toggleYrs10;
	private View toggleYrs20;
	private View toggleYrs30;
	private AcctElements currentElement;
    private String[] values; // get from callback from Activity which category was picked up and 
    private History history;
    private LinearLayout predPlot;
    private int currentYrsPred;
    View selectedYrsView;
    
    public void updateValues(String[] vals) {
    	values = vals;
    }
          
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	
    	View frView = inflater.inflate(R.layout.frg_chart, container, false);
    	    	
    	Finances appState = Finances.getInstance(); 	    	    	   
    	history = appState.getHistory();
    	    	
       	toggleYrs1 = (View) frView.findViewById(R.id.pred1);
    	toggleYrs5 = (View) frView.findViewById(R.id.pred5);
    	toggleYrs10 = (View) frView.findViewById(R.id.pred10);
    	toggleYrs20 = (View) frView.findViewById(R.id.pred20);
    	toggleYrs30 = (View) frView.findViewById(R.id.pred30);
    	   	
    	selectedYrsView = ((Main) getSherlockActivity()).getSelectedYrsView();

    	predPlot = (LinearLayout) frView.findViewById(R.id.pred_chart);	
    	
    	currentElement = ((Main) getSherlockActivity()).getCurrentElement();
    	if (currentElement == null) {
    		currentElement = AcctElements.BALANCE;
    	}    	   	
    	
    	currentYrsPred = ((Main) getSherlockActivity()).getCurrentYrsPred();   	    	
    	/*values = ((Main) getSherlockActivity()).getValues();
    	
    	plot(values, currentElement.toString(), currentYrsPred);   
    	//return super.onCreateView(inflater, container, savedInstanceState);*/
    	if (selectedYrsView == null) {    		    		
    		toggleYrs5.setSelected(true);
        	Log.d("selectedYrsView", "NULL");
    	} else {
    		toggleYrsSelection(selectedYrsView);
    		Log.d("selectedYrsView", selectedYrsView.toString());
    	}
    	return frView;
    }
 
	private void toggleYrsSelection(View view) {
        toggleYrs1.setSelected(false);
        toggleYrs5.setSelected(false);
        toggleYrs10.setSelected(false);
        toggleYrs20.setSelected(false);
        toggleYrs30.setSelected(false);
		Log.d("toggleYrsSelection", view.toString());        
	    view.setSelected(true);
	}
	
	private XYMultipleSeriesRenderer createMultipleSeriesRenderer(String title, 				                                                      
			                                                      int seriesSize) {
    	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    	mRenderer.setAxesColor(Color.WHITE);
        mRenderer.setAxisTitleTextSize(Utils.px(getSherlockActivity(), 10));    	
    	mRenderer.setMarginsColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));    	
    	mRenderer.setClickEnabled(true);
    	mRenderer.setPanEnabled(false, false);
    	mRenderer.setZoomEnabled(false, false);
    	mRenderer.setShowGridX(true);
        mRenderer.setGridColor(0x66CCCCCC);                       
        mRenderer.setShowLegend(false);
        mRenderer.setLabelsColor(Color.DKGRAY);
        mRenderer.setXLabels(0);       
        mRenderer.setYLabels(15);        
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.RIGHT, 0);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setYTitle(title);       
        mRenderer.setPointSize(Utils.px(getSherlockActivity(), 2));           	    
    	int labelsTextSize = Utils.px(getSherlockActivity(), 10);
    	mRenderer.setLabelsTextSize(labelsTextSize);                
        return mRenderer;
	}
	
	private void plotAChartEngine(TimeSeries series, String title) {
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	dataset.addSeries(series);
    	    	
    	XYSeriesRenderer renderer = new XYSeriesRenderer();
    	renderer.setColor(Color.YELLOW);
    	renderer.setPointStyle(PointStyle.CIRCLE);
    	renderer.setFillPoints(true);
    	renderer.setLineWidth(Utils.px(getSherlockActivity(), 2));
    	renderer.setDisplayChartValues(true);   
    	renderer.setChartValuesTextAlign(Align.RIGHT);    	    
    	renderer.setChartValuesTextSize(Utils.px(getSherlockActivity(), 10));
        renderer.setChartValuesSpacing(Utils.px(getSherlockActivity(), 8));     

	   
		XYMultipleSeriesRenderer mRenderer = createMultipleSeriesRenderer(title,
			                                                       
				                                                          series.getItemCount());    	
    	mRenderer.addSeriesRenderer(renderer);              
    	GraphicalView mChartView = ChartFactory.getLineChartView(getSherlockActivity(), dataset, mRenderer);	    	  		    	
        LinearLayout layout = (LinearLayout) predPlot;		       
        layout.removeAllViews();
        layout.addView(mChartView);    	
	}	
	
	private TimeSeries getSeries(BigDecimal[] values, String title, int item_count, String name) {
	    TimeSeries series = new TimeSeries(name);	    
	    try {
	        for(int i = 0; i < item_count; i++) {
	    	    series.add(i, values[i].doubleValue());
	        }
	    } catch (IndexOutOfBoundsException e) {
	        e.printStackTrace();
	    }
	    return series;
        //plotAChartEngine(series, title);	    	     
	}	
	
	private XYSeriesRenderer getSeriesRenderer() {
		XYSeriesRenderer renderer = new XYSeriesRenderer();
	   	renderer.setColor(Color.YELLOW); //TODO: random (light) color
    	renderer.setPointStyle(PointStyle.CIRCLE);
    	renderer.setFillPoints(true);
    	renderer.setLineWidth(Utils.px(getSherlockActivity(), 2));
    	renderer.setDisplayChartValues(true);   
    	renderer.setChartValuesTextAlign(Align.RIGHT);    	    
    	renderer.setChartValuesTextSize(Utils.px(getSherlockActivity(), 10));
        renderer.setChartValuesSpacing(Utils.px(getSherlockActivity(), 8));     
		return renderer;
	}
 	
	public void onPredYrsSelection(View view, int item_count, AcctElements title) {
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer = createMultipleSeriesRenderer(title.toString(), 360);  

		toggleYrsSelection(view);
		if (title == AcctElements.INVESTMENT) {
			for(HistoryInvestment i : history.getInvestmentHistory()) {
				//i.plot();
				TimeSeries series = getSeries(i.getAmountHistory(), title.toString(), item_count, i.getName());
				dataset.addSeries(series);
				
			    double maxY = series.getMaxY();
			    double minY = series.getMinY();	    
		        long axisMinY = Math.round(minY - minY/10);
		        long axisMaxY = Math.round(maxY + maxY/8);
		        mRenderer.setYAxisMin(axisMinY);
		    	mRenderer.setYAxisMax(axisMaxY);
			    /*
		    	int rightMargin = Utils.px(getSherlockActivity(), 12);
			    int leftMargin = Utils.px(getSherlockActivity(), 6);
			    int labelMaxYLen = String.valueOf(axisMinY).length();
			    int labelMinYLen = String.valueOf(axisMaxY).length();
			    if (labelMaxYLen >=  labelMinYLen) {
				    leftMargin += Utils.px(getSherlockActivity(), 7) * labelMaxYLen;
			    } else {
			    	leftMargin += Utils.px(getSherlockActivity(), 7) * labelMinYLen;
			    }	        

		    	mRenderer.setMargins(new int[] {Utils.px(getSherlockActivity(), 8), 
		    			leftMargin, 
		    			Utils.px(getSherlockActivity(), 8), 
		    			rightMargin}); // top, left, bottom, right
		    			
		    	int screenWidthDips = Utils.dip(getSherlockActivity(), getResources().getDisplayMetrics().widthPixels);
    	        int chartSize = screenWidthDips - leftMargin - rightMargin;
    	        int numOfLabels = Math.round(chartSize / 30);
                int i;
                int step = seriesSize / numOfLabels;
                if (step < 1) {
        	        step = 1;
                }
		        for (i = 0; i < seriesSize; i += step) { //TODO: check if size == values.size()
			        String date =  history.getDates()[i];
			        mRenderer.addXTextLabel(i, date.replaceFirst(" ", "\n"));
		        }
			    */    
			    XYSeriesRenderer renderer = getSeriesRenderer();
		    	mRenderer.addSeriesRenderer(renderer);              		    	
			}
		}
		
		GraphicalView mChartView = ChartFactory.getLineChartView(getSherlockActivity(), dataset, mRenderer);	    	  		    	
        LinearLayout layout = (LinearLayout) predPlot;		       
        layout.removeAllViews();
        layout.addView(mChartView); 
    }
}