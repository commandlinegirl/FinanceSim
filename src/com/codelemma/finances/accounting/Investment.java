package com.codelemma.finances.accounting;
import java.math.BigDecimal;

public abstract class Investment implements NamedValue {
	             
	public abstract  BigDecimal getInitAmount();
    public abstract  BigDecimal getPercontrib();
    public abstract  void initialize();     
    public abstract  void setId(int id);
    public void advance(int month, BigDecimal excess) {};  
    public void advance(int month) {};     

}