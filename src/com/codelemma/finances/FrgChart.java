package com.codelemma.finances;

import java.util.ArrayList;


import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.HistoryNew;
import com.codelemma.finances.accounting.PlotVisitor;


import android.os.Bundle;
import android.util.Log;
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
 
	private View toggleYrs1;
	private View toggleYrs5;
	private View toggleYrs10;
	private View toggleYrs20;
	private View toggleYrs30;
	private int currentElement;
	private PlotVisitor plotVisitor;
	private ArrayList<HistoryNew> historyItems = new ArrayList<HistoryNew>();
	private int currentPosition;
    private History history;
    private int numberOfMonths;
    private View selectedYrsView;

    private String[] elementName = {"income", "expense", "investment", "debt"};
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    	

        currentElement = ((Main) getSherlockActivity()).getCurrentElement();
    	Finances appState = Finances.getInstance(); 	    	    	   
    	history = appState.getHistory();
        
        switch(currentElement) {
        case 0:
            for (HistoryNew i : history.getIncomeHistory()) {
            	historyItems.add(i);
            }
            break;
	    case 1:
            for (HistoryNew i : history.getExpenseHistory()) {
            	historyItems.add(i);
            }
            break;    
        case 2:
        	for (HistoryNew i : history.getInvestmentHistory()) {
            	historyItems.add(i);
            }
        	break;            
        case 3:    	
            for (HistoryNew i : history.getDebtHistory()) {
            	historyItems.add(i);
            }
            break;
        }
    	
    	if (historyItems.size() != 0) {
    		return inflater.inflate(R.layout.frg_chart, container, false);	
    	} 
    	return inflater.inflate(R.layout.frg_empty, container, false);
    	
    	    	
    }
 
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		currentPosition = pos;
    	HistoryNew historyItem = historyItems.get(pos);
	    historyItem.plot(plotVisitor);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		currentPosition = 0;		
		parent.setSelection(0);		
    	HistoryNew historyItem = historyItems.get(0);
	    historyItem.plot(plotVisitor);
	}
    
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if (historyItems.size() != 0) {
    		
    	   	toggleYrs1 = (View) getSherlockActivity().findViewById(R.id.pred1);
    		toggleYrs5 = (View) getSherlockActivity().findViewById(R.id.pred5);
    		toggleYrs10 = (View) getSherlockActivity().findViewById(R.id.pred10);
    		toggleYrs20 = (View) getSherlockActivity().findViewById(R.id.pred20);
    		toggleYrs30 = (View) getSherlockActivity().findViewById(R.id.pred30);
    		
    		selectedYrsView = ((Main) getSherlockActivity()).getSelectedYrsView();
    		if (selectedYrsView == null) {    		    		
    			toggleYrs5.setSelected(true);
    			selectedYrsView = getSherlockActivity().findViewById(R.id.pred5);
    	    	Log.d("selectedYrsView", "NULL");
    		} else {
    			toggleYrsSelection(selectedYrsView);
    			Log.d("selectedYrsView", selectedYrsView.toString());
    		}
    		
	        Spinner spinner = (Spinner) getSherlockActivity().findViewById(R.id.chart_spinner);	    
	        ArrayAdapter<HistoryNew> adapter = new ArrayAdapter<HistoryNew>(getSherlockActivity(), android.R.layout.simple_spinner_item, historyItems); 	    	    
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        spinner.setSelection(0);
	        spinner.setOnItemSelectedListener(this);	   
        	    	
	        plotVisitor = new Plotter(getSherlockActivity(), history.getDates());
	        plotVisitor.setNumberOfMonths(5*12);

    	} else {
    		
    		TextView tv = (TextView) getSherlockActivity().findViewById(R.id.add_data_text);
    		String format = getResources().getString(R.string.chart_no_data_info_text);
    		String value = String.format(format, elementName[currentElement]); 
    		tv.setText(value);
    		
    		Button button = (Button) getSherlockActivity().findViewById(R.id.add_data_button);
    		String format2 = getResources().getString(R.string.no_data_info_button);
    		String value2 = String.format(format2, elementName[currentElement]); 
    		button.setText(value2);
    		button.setOnClickListener(new DirectToHomeListener(currentElement, getSherlockActivity()));
    	}
    	    	
	}
       		
    public void onPredYrsSelection(View view, int currentElement) {
    	switch (view.getId()) {
    	case R.id.pred1:
    		numberOfMonths = 1*12;
    		break;
    	case R.id.pred5:
    		numberOfMonths = 5*12;
    		break;
    	case R.id.pred10:
    		numberOfMonths = 10*12;
    		break;
    	case R.id.pred20:
    		numberOfMonths = 20*12;
    		break;
    	case R.id.pred30:
    		numberOfMonths = 30*12;
    		break;
    	default:
    		numberOfMonths = 5*12;
    	    break;
    	}
    	toggleYrsSelection(view);
        plotVisitor.setNumberOfMonths(numberOfMonths);
    	HistoryNew historyItem = historyItems.get(currentPosition);
	    historyItem.plot(plotVisitor);
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
}