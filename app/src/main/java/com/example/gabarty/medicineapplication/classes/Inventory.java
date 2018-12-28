package com.example.gabarty.medicineapplication.classes;

import java.io.Serializable;

public class Inventory implements Serializable {

    private String name;
    private String quantity;
    private String price;
    private String customer_price;
    private String minimumQuantity;
    private String state;

    public Inventory(String name, String quantity, String price, String cP, String mQ) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.customer_price = cP;
        this.minimumQuantity = mQ;

        int currentQuantity = Integer.parseInt(quantity);
        int minQuantity = Integer.parseInt(mQ);

        if(currentQuantity < minQuantity){
            this.state = "Not Active";
        }else{
            this.state = "Active";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        int currentQuantity = Integer.parseInt(quantity);
        int minQuantity = Integer.parseInt(getMinimumQuantity());

        if(currentQuantity < minQuantity){
            this.state = "Not Active";
        }else{
            this.state = "Active";
        }
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCustomer_price() {
        return customer_price;
    }

    public void setCustomer_price(String customer_price) {
        this.customer_price = customer_price;
    }

    public String getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(String minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
