package com.codelemma.finances;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DirectToHomeListener implements OnClickListener {

    private OnClickOnEmptyListener eListener;
    private int element;
    
    public DirectToHomeListener(int element, SherlockFragmentActivity activity) {
    	this.element = element;
        try {
            eListener = (OnClickOnEmptyListener) activity;            
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnControlSelectedListener");
        }
    }
	
	@Override
	public void onClick(View view) {
		Button btn = (Button) view;    //cast view to a button
        eListener.onClickOnEmptySelected(btn, element);      	    
	}
	
    public interface OnClickOnEmptyListener {
        public void onClickOnEmptySelected(View view, int element);
    }   
}