package com.codelemma.finances;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.SherlockFragment;


public class FrgExpandableList extends SherlockFragment  { 

	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;

	
	@Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("FrgExpandableList.onCreateView()", "called");       
        return inflater.inflate(R.layout.frg_explist, container, false);
    }
	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	ExpandList = (ExpandableListView) getSherlockActivity().findViewById(R.id.exp_list);
        ExpListItems = setStandardGroups();
        ExpAdapter = new ExpandListAdapter(getSherlockActivity(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);        
	}
	
    public ArrayList<ExpandListGroup> setStandardGroups() {
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();

    	String[] dates = ((Main) getSherlockActivity()).getDates();
    	String[] values = ((Main) getSherlockActivity()).getValues();
    	

    	int datesLen = dates.length;
    	String prevDate = dates[0].substring(3);
    	String currDate;
    	ExpandListGroup gru1 = new ExpandListGroup();
    	int i = 0;
        gru1.setName(dates[i]+"   "+values[i]);
        i++;
    	while(i < datesLen) {    		
        	currDate = dates[i].substring(3);
    		if (prevDate.equals(currDate)) {
    		    ExpandListChild ch1_1 = new ExpandListChild();
	            ch1_1.setName(dates[i]+"   "+values[i]);
	            ch1_1.setTag(null);
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<ExpandListChild>();
    			gru1 = new ExpandListGroup();
    			gru1.setName(dates[i]+"   "+values[i]);  			
    		}
    		i++;
    		prevDate = currDate;
    	}    	    	
        
        return list;
    }
	
}
