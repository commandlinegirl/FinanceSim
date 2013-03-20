package com.codelemma.finances;

public abstract class ListInvestment401k {

	private String date;
	private String amount;
	private String salary;
	private String employeeContribution;
	private String employerContribution;
		

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getSalary() {
		return salary;
	}
	
	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	public String getEmployeeContribution() {
		return employeeContribution;
	}
	
	public void setEmployeeContribution(String employeeContribution) {
		this.employeeContribution = employeeContribution;
	}
	
	public String getEmployerContribution() {
		return employerContribution;
	}
	
	public void setEmployerContribution(String employerContribution) {
		this.employerContribution = employerContribution;
	}
	
}
