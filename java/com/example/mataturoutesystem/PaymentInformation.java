package com.example.mataturoutesystem;

public class PaymentInformation {
    public String pay_id;
    public String amount;
    public String status;

    public PaymentInformation(){

    }

    public PaymentInformation(String pay_id, String amount, String status) {
        this.pay_id = pay_id;
        this.amount = amount;
        this.status = status;
    }
}
