<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"  
    android:orientation="vertical" 
    android:background="#FFE6E6E6"
    android:paddingTop="10dp" 
    android:paddingBottom="10dp" >

                      
   <LinearLayout
        android:id="@+id/tvGroup"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
    
    <TextView
        android:id="@+id/date"
        android:layout_width="0dp"
        android:layout_weight="1"        
        android:layout_height="wrap_content"
        android:layout_gravity="left"
        android:textColor="#FF771100"
        android:textSize="@dimen/list_text_size"
        android:textStyle="bold"/>               

    <TextView
        android:id="@+id/savings"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:textSize="@dimen/list_text_size"
        android:layout_height="wrap_content"/>     
    
    <TextView
        android:id="@+id/outstanding_debts"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:textSize="@dimen/list_text_size"
        android:layout_height="wrap_content"/>   
    
    <TextView
    android:id="@+id/net_worth"
    android:layout_width="0dp"
    android:layout_weight="1"
    android:textSize="@dimen/list_text_size"
    android:layout_height="wrap_content"/>      
    
    </LinearLayout>
    
</LinearLayout>    