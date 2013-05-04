package com.codelemma.finances;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.codelemma.finances.accounting.Storage;
import com.codelemma.finances.accounting.Storage.StorageException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
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
    private String dateString;
    private String changedValueCanBeNull;
    private DatePicker datePicker;
    private Storage storage;

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        storage = StorageFactory.create(PreferenceManager.getDefaultSharedPreferences(context));
        appState = Finances.getInstance();
    }

    /**
    * Produces a DatePicker set to the date produced by {@link #getDate()}. When
    * overriding be sure to call the super.
    * 
    * @return a DatePicker with the date set
    */
    @Override
    protected View onCreateDialogView() {
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
        Calendar cal = Calendar.getInstance();
        try {
        	storage.open(Storage.OpenState.READ);
            int year = storage.getInt("", "ssy");
            int month = storage.getInt("", "ssm");            
            Log.d("geDate() y", String.valueOf(year));
            Log.d("geDate() m", String.valueOf(month));
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
   * Set the selected date to the specified string.
   * 
   * @param dateString
   *          The date, represented as a string, in the format specified by
   *          {@link #formatter()}.
   */
  public void setDate(String dateString) {
    this.dateString = dateString;
  }

  /**
   * Produces the date formatter used for dates in the XML. The default is yyyy.MM.dd.
   * Override this to change that.
   * 
   * @return the SimpleDateFormat used for XML dates
   */
  public static SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }

  /**
   * Produces the date formatter used for showing the date in the summary. The default is MMMM dd, yyyy.
   * Override this to change it.
   * 
   * @return the SimpleDateFormat used for summary dates
   */
  public static SimpleDateFormat summaryFormatter() {
    return new SimpleDateFormat("MMM, yyyy");
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getString(index);
  }

  /**
   * Called when the date picker is shown or restored. If it's a restore it gets
   * the persisted value, otherwise it persists the value.
   */
  @Override
  protected void onSetInitialValue(boolean restoreValue, Object def) {
    if (restoreValue) {
      this.dateString = getPersistedString(defaultValue());
      setTheDate(this.dateString);
    } else {
      boolean wasNull = this.dateString == null;
      setDate((String) def);
      if (!wasNull)
        persistDate(this.dateString);
    }
  }

  /**
   * Called when the user changes the date.
   */
  public void onDateChanged(DatePicker view, int year, int month, int day) {
    Calendar selected = new GregorianCalendar(year, month, day);
    this.changedValueCanBeNull = formatter().format(selected.getTime());
  }

  /**
   * Called when the dialog is closed. If the close was by pressing "OK" it
   * saves the value.
   */
    @Override
    protected void onDialogClosed(boolean shouldSave) {
        if (shouldSave && this.changedValueCanBeNull != null) {
            setTheDate(this.changedValueCanBeNull);
            int year = Integer.parseInt(this.changedValueCanBeNull.substring(0, 4));
            int month = Integer.parseInt(this.changedValueCanBeNull.substring(5, 7));
            try {
  		        storage.open(Storage.OpenState.WRITE);
		        storage.putInt("", "ssy", year);
		        storage.putInt("", "ssm", month-1); //TODO: change this!!!
            	  //  also: update Account!
		        appState.getAccount().setSimulationStartYear(year);
		        appState.getAccount().setSimulationStartMonth(month-1); //TODO: change this!!!
		        appState.getAccount().createDateList();
	        } catch (StorageException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
	        } finally {
	        	storage.close();
	        }
            this.changedValueCanBeNull = null;
        }
    }

  private void setTheDate(String s) {
    setDate(s);
    persistDate(s);
  }

  private void persistDate(String s) {
    persistString(s);
    Log.d("String persisted", s);
    setSummary(summaryFormatter().format(getDate().getTime()));
  }

  /**
   * The default date to use when the XML does not set it or the XML has an
   * error.
   * 
   * @return the Calendar set to the default date
   */
  public static Calendar defaultCalendar() {
    return new GregorianCalendar(1970, 0, 1);
  }

  /**
   * The defaultCalendar() as a string using the {@link #formatter()}.
   * 
   * @return a String representation of the default date
   */
  public static String defaultCalendarString() {
    return formatter().format(defaultCalendar().getTime());
  }

    private String defaultValue() {
        if (this.dateString == null)
            setDate(defaultCalendarString());
        return this.dateString;
    }

  /**
   * Called whenever the user clicks on a button. Invokes {@link #onDateChanged(DatePicker, int, int, int)}
   * and {@link #onDialogClosed(boolean)}. Be sure to call the super when overriding.
   */
  @Override
  public void onClick(DialogInterface dialog, int which) {
    super.onClick(dialog, which);
    datePicker.clearFocus();
    onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(),
        datePicker.getDayOfMonth());
    onDialogClosed(which == DialogInterface.BUTTON1); // OK?
  }

  /**
   * Produces the date the user has selected for the given preference, as a
   * calendar.
   */
    public Calendar getDateFor(String field) throws StorageException {
    	storage.open(Storage.OpenState.READ);
        int year = storage.getInt("", "ssy");
        int month = storage.getInt("", "ssm");
        storage.close();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal;
    }

  private static Date stringToDate(String dateString) {
    try {
      return formatter().parse(dateString);
    } catch (ParseException e) {
      return defaultCalendar().getTime();
    }
  }
}