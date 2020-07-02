package com.psguide.uttam.Models.PPayment;


import java.sql.Timestamp;

public class Payment {
    public String userId;
    public boolean status;
    public double amount;
    public Timestamp timestamp;
    public String paymentId;
    public String payMethod;
    public BankMethod bankMethod;
    public PhoneMethod phoneMethod;



    public Payment() {
    }

    public BankMethod getBankMethod() {
        return bankMethod;
    }

    public void setBankMethod(BankMethod bankMethod) {
        this.bankMethod = bankMethod;
    }

    public PhoneMethod getPhoneMethod() {
        return phoneMethod;
    }

    public void setPhoneMethod(PhoneMethod phoneMethod) {
        this.phoneMethod = phoneMethod;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
