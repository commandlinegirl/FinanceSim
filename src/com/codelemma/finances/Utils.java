package com.codelemma.finances;

import android.app.AlertDialog;
import android.content.Context;

public class Utils {
	
    private Utils() {}

	static public int px(Context context, float dips) {
    /* Convert from dp into int (px) */
	    float DP = context.getResources().getDisplayMetrics().density;
	    return Math.round(dips * DP);
	}
	
	static public int dip(Context context, int px) {
	    /* Convert from px into dip (density independent pixels) */
		    float DP = context.getResources().getDisplayMetrics().density;
		    return Math.round(px / DP);
		}

	static public int getIndex(int[] a, int x) {
		for (int i = 0; (i < a.length); i++) {
	        if (a[i] == x) {
	            return i;
	        }
	    }
		return -1;
	}
	
	static public boolean alertIfEmpty(Context context, String fieldData, String fieldName) {
	    if (fieldData.trim().length() == 0) {
	    	new AlertDialog.Builder(context).setTitle("\""+ fieldName +"\" empty")
	        	                             .setMessage("Please, fill in the field.")
	        	                             .setNeutralButton("Close", null)
	        	                             .show();
	    	return true;
	    }
	    return false;
	}
}
