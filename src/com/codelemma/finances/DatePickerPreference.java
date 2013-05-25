package com.codelemma.finances;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.codelemma.finances.accounting.Months;
import com.codelemma.finances.accounting.Storage;
import com.codelemma.finances.accounting.Storage.StorageException;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerPreference extends DialogPreference 
                                  implements DatePicker.OnDateChangedListener {
	private Finances appState;
    private DatePicker datePicker;
    private Storage storage;
    private Context context;
    private int changedYear;
    private int changedMonth;
    private Months[] months = Months.values();

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        storage = StorageFactory.create(PreferenceManager.getDefaultSharedPreferences(context));
        appState = Finances.getInstance();
        this.context = context;
    }

    /**
    * Produces a DatePicker set to the date produced by {@link #getDate()}. When
    * overriding be sure to call the super.
    * 
    * @return a DatePicker with the date set
    */
    @Override
    protected View onCreateDialogView() {
    	Log.d("### onCreateDialogView()", "called");
    	super.onCreateDialogView();
        this.datePicker = new DatePicker(getContext());
        removeCalendarView();
        Calendar calendar = getDate();
        datePicker.init(
        		calendar.get(Calendar.YEAR), 
        		calendar.get(Calendar.MONTH),
        		calendar.get(Calendar.DAY_OF_MONTH),
        		this);
        return datePicker;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void removeCalendarView() {
    	Log.d("### removeCalendarView()", "called");
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    	    this.datePicker.setCalendarViewShown(false);
    	}
    }

    /**
     * Produces the date used for the date picker. If the user has not selected a
     * date, produces the default from the XML's android:defaultValue. If the
     * default is not set in the XML or if the XML's default is invalid it uses
     * the value produced by {@link #defaultCalendar()}.
     * 
     * @return the Calendar for the date picker
     */
    public Calendar getDate() {
    	Log.d("### getDate()", "called");
        Calendar cal = Calendar.getInstance();
        try {        	
        	storage.open(Storage.OpenState.READ);
            int year = storage.getInt("", "ssy");
            int month = storage.getInt("", "ssm");            
            cal.set(year, month, 1);
        } catch (StorageException e) {
			e.printStackTrace();
		    cal = defaultCalendar();
		} finally {
            storage.close();
		}
        return cal;
    }

    /**
    * Called when the user changes the date.
    */
    public void onDateChanged(DatePicker view, int year, int month, int day) {
  	    Log.d("### onDateChanged()", "called");
        this.changedYear = year;
        this.changedMonth = month;
    }

  /**
   * Called when the dialog is closed. If the close was by pressing "OK" it
   * saves the value.
   */
    @Override
    protected void onDialogClosed(boolean shouldSave) {
    	Log.d("### onDialogClosed()", "called");
        if (shouldSave) {
        	Log.d("### onDialogClosed()", "should Save true");
            int year = changedYear; //Integer.parseInt(this.changedValueCanBeNull.substring(0, 4));
            int month = changedMonth;//Integer.parseInt(this.changedValueCanBeNull.substring(5, 7));
            Log.d("onDialogClosed year", String.valueOf(year));
            Log.d("onDialogClosed month", String.valueOf(month));
            Log.d("onDialogClosed year", String.valueOf(appState.getAccount().getCalculationStartYear()));
            Log.d("onDialogClosed month", String.valueOf(appState.getAccount().getCalculationStartMonth()));
            if ((appState.getAccount().getCalculationStartYear() == year && appState.getAccount().getCalculationStartMonth() > month) 
        			|| (appState.getAccount().getCalculationStartYear() > year)) {
            	Log.d("alert", "ALERT");
            	new AlertDialog.Builder(context) 
            	    .setTitle("Date input incorrect")
                    .setMessage("The start date of the simulation should not be earlier than the earliest start date of any of your instruments " +
                    		"("+months[appState.getAccount().getCalculationStartMonth()]+"/"+appState.getAccount().getCalculationStartYear()+")." +
                    		"\n\nPlease set date to "+months[appState.getAccount().getCalculationStartMonth()]+"/"+appState.getAccount().getCalculationStartYear()+" earliest.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                     	   dialog.cancel();
                        }
                    })
                    .show();
            } else {
            	try {
      		        storage.open(Storage.OpenState.WRITE);
    		        storage.putInt("", "ssy", year);
    		        storage.putInt("", "ssm", month); //TODO: change this!!!
    		        appState.getAccount().setSimulationStartYear(year);
    		        appState.getAccount().setSimulationStartMonth(month); //TODO: change this!!!
    		        appState.getAccount().createDateList();
    		        appState.needToRecalculate(true);
    		        Log.d("saved to preferences", "preferences");
    	        	setSummary(months[month]+"/"+year); //TODO: throw exception if indexofoutbounds
    	        } catch (StorageException e) {
    		        e.printStackTrace();
    	        } finally {
    	        	storage.close();
    	        }	
            }
        }
    }

  /**
   * The default date to use when the XML does not set it or the XML has an
   * error.
   * 
   * @return the Calendar set to the default date
   */
    public static Calendar defaultCalendar() {
        Log.d("### defaultCalendar()", "called");
        return new GregorianCalendar(2014, 0, 1);
    }
}