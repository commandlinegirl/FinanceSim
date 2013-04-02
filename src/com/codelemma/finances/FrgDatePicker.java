package com.codelemma.finances;

import java.util.Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class FrgDatePicker extends DialogFragment
                           implements DatePickerDialog.OnDateSetListener {

    private OnDateSelectedListener dateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        //int day = c.get(Calendar.DAY_OF_MONTH);
        int day = 1;
        
    	if (savedInstanceState != null) {
    		year = savedInstanceState.getInt("setYear");
    	    month = savedInstanceState.getInt("setMonth");
    	    day = 1;
    	    savedInstanceState.getInt("setMonth");    	    	
    	}
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

 
    @Override
    public void onAttach(Activity activity) { //TODO: Activity or SherlockActivity?
        super.onAttach(activity);        
        try {
        	dateListener = (OnDateSelectedListener) activity;            
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDateSelectedListener");
        }        
    }
    
    public interface OnDateSelectedListener {
        public void onDateSet(DatePicker view, int year, int month, int day);
    }

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		dateListener.onDateSet(view, year, month, day);		
	}  
    
}