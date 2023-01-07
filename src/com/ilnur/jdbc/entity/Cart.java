package com.ilnur.jdbc.entity;

public class Cart {

    private Long orderId;
    private Long productId;
    private int number;

    public Cart() {
    }

    public Cart(Long orderId, Long productId, int number) {
        this.orderId = orderId;
        this.productId = productId;
        this.number = number;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", number=" + number +
                '}';
    }
}
