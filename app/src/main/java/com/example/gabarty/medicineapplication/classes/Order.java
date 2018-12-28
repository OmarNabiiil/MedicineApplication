package com.example.gabarty.medicineapplication.classes;

public class Order {
    private String userId;
    private String product;
    private String quantity;
    private String price;

    public Order(String user_id, String product, String quantity, String price) {
        this.userId = user_id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
}
