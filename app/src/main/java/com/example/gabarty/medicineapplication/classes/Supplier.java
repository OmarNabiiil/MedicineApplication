package com.example.gabarty.medicineapplication.classes;

import java.io.Serializable;

public class Supplier implements Serializable {

    private String name;
    private String email;
    private String mobile;
    private String quantity;
    private String price;
    private String date;

    public Supplier(String name, String email, String mobile, String quantity, String price) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
