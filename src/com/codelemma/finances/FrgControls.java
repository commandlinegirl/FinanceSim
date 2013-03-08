package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FrgControls extends SherlockFragment {
	
    private OnControlSelectedListener eListener;
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("FragmentControls.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_controls, container, false);
    }
    
    @Override
    public void onAttach(Activity activity) { //TODO: Activity or SherlockActivity?
        super.onAttach(activity);
        Log.d("FrgControls.onAttach()", "called");
        try {
            eListener = (OnControlSelectedListener) activity;            
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnControlSelectedListener");
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        Log.d("FragmentControls.onActivityCreated()", "called");
    }
    
    @Override
    public void onDestroyView() {
    	super.onDestroyView();
        Log.d("FragmentControls.onDestroyView()", "called");
    }
      
    @Override
    public void onDetach() {
    	super.onDetach();
        Log.d("FragmentControls.onDetach()", "called");
    }
    
    public interface OnControlSelectedListener {
        public void onControlSelected(View view);
    }        
    
    public void selectControl(View view) {
        // Send the event to the host activity
        eListener.onControlSelected(view);
    }
}