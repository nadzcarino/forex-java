package com.forex.model;

public class Rate {

	private String base;
	private String target;
	private Double rate;
	private Long timeStamp;
	
	
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}


	public Double getRate() {
		return rate;
	}


	public void setRate(Double rate) {
		this.rate = rate;
	}


	public Long getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}


	@Override
	public String toString() {
		return "Rate [base=" + base + ", target=" + target + ", rate=" + rate + ", timeStamp=" + timeStamp + "]";
	}
	
	
}
