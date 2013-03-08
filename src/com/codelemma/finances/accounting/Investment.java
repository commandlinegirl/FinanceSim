package com.codelemma.finances.accounting;
import java.math.BigDecimal;

abstract class Investment  {
	             
    abstract public BigDecimal getInitAmount();
    abstract public BigDecimal getPercontrib();
    abstract public BigDecimal getAmount();  
    abstract public void initialize();     
    abstract public int getId();
    abstract public void setId(int id);
    public void advance(int month, BigDecimal excess) {};  
    public void advance(int month) {}; 
    
    public abstract HistoryNew createHistory();
}