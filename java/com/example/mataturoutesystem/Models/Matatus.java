package com.example.mataturoutesystem.Models;

public class Matatus {
    String regNumber, price;

    public Matatus (String regNumber, String price){
        this.regNumber = regNumber;
        this.price = price;
    }

    public Matatus (){}

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
