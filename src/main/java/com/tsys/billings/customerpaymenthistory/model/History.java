package com.tsys.billings.customerpaymenthistory.model;

public class History {

	private String paymentId;
	private String paymentType;
	private String cardNumber;
	private String paymentDate;
	private String paymentTime;
	private String paymentAmount;
	private String storeName;
	
	public History() {
		
	}
	
	public History(String paymentId, String paymentType, String cardNumber, 
			String paymentDate, String paymentTime,
			String paymentAmount, String storeName) {
		super();
		this.paymentId = paymentId;
		this.paymentType = paymentType;
		this.cardNumber = cardNumber;
		this.paymentDate = paymentDate;
		this.paymentTime = paymentTime;
		this.paymentAmount = paymentAmount;
		this.storeName = storeName;
	}
	
	
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}
