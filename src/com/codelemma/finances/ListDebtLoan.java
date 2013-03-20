package com.codelemma.finances;


public abstract class ListDebtLoan {

	private String date;
	private String monthly_payment;
	private String interests_paid;
	private String total_interests;
	private String principal_paid;
	private String remaining_amount;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMonthlyPayment(String monthly_payment) {
		this.monthly_payment = monthly_payment;
	}
	
	public String getMonthlyPayment() {
		return monthly_payment;
	}
	
	public void setInterestsPaid(String interests_paid) {
		this.interests_paid = interests_paid;
	}	
	
	public String getInterestsPaid() {
		return interests_paid;
	}
	
	public void setTotalInterests(String total_interests) {
		this.total_interests = total_interests;
	}	
	
	public String getTotalInterests() {
		return total_interests;
	}
	
	public void setPrincipalPaid(String principal_paid) {
		this.principal_paid = principal_paid;
	}	
	
	public String getPrincipalPaid() {
		return principal_paid;
	}
	
	public void setRemainingAmount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}	
	
	public String getRemainingAmount() {
		return remaining_amount;
	}
}
