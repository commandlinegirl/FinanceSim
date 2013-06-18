package com.codelemma.finances;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.HistoryNew;
import com.codelemma.finances.accounting.PlotVisitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class FrgChart extends SherlockFragment 
                      implements OnItemSelectedListener{

	private Finances appState;
	private View toggleYrs1;
	private View toggleYrs5;
	private View toggleYrs10;	
	private View toggleYrs30;
	private View toggleYrs50;
	private int currentElement;
	private PlotVisitor plotVisitor;
	private ArrayList<HistoryNew> historyItems = new ArrayList<HistoryNew>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentElement = ((Main) getSherlockActivity()).getCurrentElement();
    	appState = Finances.getInstance();
    	        
        for (HistoryNew i : appState.getHistory().getHistory(currentElement)) {
        	historyItems.add(i);
        }
    	        
    	if (historyItems.size() == 0) {

    		//Toast.makeText(getSherlockActivity(), "Chart empty. Please add data to get started.", Toast.LENGTH_SHORT).show();
    		return inflater.inflate(R.layout.frg_empty, container, false);    		
        } 
    	return inflater.inflate(R.layout.frg_chart, container, false);    	    	
    }
 

	@SuppressLint("DefaultLocale")
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if (historyItems.size() != 0) {
    		
    	   	toggleYrs1 = (View) getSherlockActivity().findViewById(R.id.pred1);
    		toggleYrs5 = (View) getSherlockActivity().findViewById(R.id.pred5);
    		toggleYrs10 = (View) getSherlockActivity().findViewById(R.id.pred10);    		
    		toggleYrs30 = (View) getSherlockActivity().findViewById(R.id.pred30);
    		toggleYrs50 = (View) getSherlockActivity().findViewById(R.id.pred50);
    		    		
    		int numberOfMonths = appState.getNumberOfMonthsInChart();
    		
    		SparseArray<View> m = new SparseArray<View>(5);
    		m.put(12, toggleYrs1);
    		m.put(60, toggleYrs5);
    		m.put(120, toggleYrs10);    		
    		m.put(360, toggleYrs30);
    		m.put(600, toggleYrs50);
  
    		toggleYrsSelection(m.get(numberOfMonths));

	        	    
	        	Spinner spinner = (Spinner) getSherlockActivity().findViewById(R.id.chart_spinner);	
	 	        ArrayAdapter<HistoryNew> adapter = new ArrayAdapter<HistoryNew>(getSherlockActivity(), 
		      	    	android.R.layout.simple_spinner_item, historyItems); 	    	    
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spinner.setAdapter(adapter);
		        spinner.setSelection(appState.getSpinnerPosition()); 
		        spinner.setOnItemSelectedListener(this);	
        	
	        plotVisitor = new Plotter(getSherlockActivity(), appState.getAccount().getDates());

    	} else {
	        
    		String[] elementName = {"income", "expense", "investment", "debt", "cashflow", "networth"};
    		
    		TextView tv = (TextView) getSherlockActivity().findViewById(R.id.add_data_text);
    		String format = getResources().getString(R.string.chart_no_data_info_text);
    		String value = String.format(format,
    				elementName[currentElement].substring(0, 1).toUpperCase() + elementName[currentElement].substring(1),
    				elementName[currentElement]);
    		tv.setText(value);
    		
    		Button button = (Button) getSherlockActivity().findViewById(R.id.add_data_button);
    		String format2 = getResources().getString(R.string.no_data_info_button);
    		String value2 = String.format(format2, elementName[currentElement]); 
    		button.setText(value2);
    		button.setOnClickListener(new DirectToHomeListener(currentElement, getSherlockActivity()));
    	}
    	    	
	}
       		
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		appState.setSpinnerPosition(pos);
    	HistoryNew historyItem = historyItems.get(pos);
	    historyItem.plot(plotVisitor);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {		
		parent.setSelection(appState.getSpinnerPosition());		
    	HistoryNew historyItem = historyItems.get(appState.getSpinnerPosition());
	    historyItem.plot(plotVisitor);	    
	}
    
    public void onPredYrsSelection(View view, int currentElement) {
    	int numberOfMonths = Integer.parseInt((String) view.getTag());
    	toggleYrsSelection(view);
        appState.setNumberOfMonthsInChart(numberOfMonths);        
    	HistoryNew historyItem = historyItems.get(appState.getSpinnerPosition());
	    historyItem.plot(plotVisitor);
    } 
            
	private void toggleYrsSelection(View view) {
        toggleYrs1.setSelected(false);
        toggleYrs5.setSelected(false);
        toggleYrs10.setSelected(false);        
        toggleYrs30.setSelected(false);
        toggleYrs50.setSelected(false);
	    view.setSelected(true);
	}
}