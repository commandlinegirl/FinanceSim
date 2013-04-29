package com.codelemma.finances;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.finances.accounting.History;
import com.codelemma.finances.accounting.HistoryNew;
import com.codelemma.finances.accounting.TableVisitor;


public class FrgList extends SherlockFragment 
                     implements OnItemSelectedListener { 

	private History history;
	private Finances appState;
	int currentElement = 0;
	private ArrayList<HistoryNew> historyItems = new ArrayList<HistoryNew>();
	private TableVisitor tableMaker;
	private String[] elementName = {"income", "expense", "investment", "debt"};
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		appState.setSpinnerPosition(pos);
    	HistoryNew historyItem = historyItems.get(pos);
	    historyItem.makeTable(tableMaker);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.setSelection(appState.getSpinnerPosition());		
    	HistoryNew historyItem = historyItems.get(appState.getSpinnerPosition());
	    historyItem.makeTable(tableMaker);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FrgExpandableList.onCreateView()", "called");    
        appState = Finances.getInstance();
        history = appState.getHistory();
        

        currentElement = ((Main) getSherlockActivity()).getCurrentElement();
        
        for (HistoryNew i : history.getHistory(currentElement)) {
        	historyItems.add(i);
        }
        
    	if (historyItems.size() == 0) {
    		//Toast.makeText(getSherlockActivity(), "Table empty. Please add data to get started.", Toast.LENGTH_SHORT).show();

    		return inflater.inflate(R.layout.frg_empty, container, false);	
    	} 
    	
    	return inflater.inflate(R.layout.frg_explist, container, false);
        
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if (historyItems.size() != 0) {
    		
	        Spinner spinner = (Spinner) getSherlockActivity().findViewById(R.id.exp_spinner);	    
	        ArrayAdapter<HistoryNew> adapter = new ArrayAdapter<HistoryNew>(getSherlockActivity(), android.R.layout.simple_spinner_item, historyItems); 	    	    
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        spinner.setSelection(appState.getSpinnerPosition());
	        spinner.setOnItemSelectedListener(this);	    
	        tableMaker = new TableMaker(getSherlockActivity());
	        
    	} else {
    		
    		TextView tv = (TextView) getSherlockActivity().findViewById(R.id.add_data_text);
    		String format = getResources().getString(R.string.no_data_info_text);
    		String value = String.format(format, elementName[currentElement]); 
    		tv.setText(value);
    		
    		Button button = (Button) getSherlockActivity().findViewById(R.id.add_data_button);
    		String format2 = getResources().getString(R.string.no_data_info_button);
    		String value2 = String.format(format2, elementName[currentElement]); 
    		button.setText(value2);
    		button.setOnClickListener(new DirectToHomeListener(currentElement, getSherlockActivity()));
    	}
	}
	

	
}
