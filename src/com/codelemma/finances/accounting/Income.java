package com.codelemma.finances.accounting;

public abstract class Income implements NamedValue {
   
    public abstract  void initialize();     
    public abstract  void setId(int id); 
    public void advance(int month) {};     	
}
