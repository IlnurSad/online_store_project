package com.ilnur.jdbc.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Orders {

    private Long id;
    private int customerId;
    private BigDecimal sum;
    private LocalDateTime orCreatedAt;
    private String status;

    public Orders() {
    }

    public Orders(Long id, int customerId, BigDecimal sum, LocalDateTime orCreatedAt, String status) {
        this.id = id;
        this.customerId = customerId;
        this.sum = sum;
        this.orCreatedAt = orCreatedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public LocalDateTime getOrCreatedAt() {
        return orCreatedAt;
    }

    public void setOrCreatedAt(LocalDateTime orCreatedAt) {
        this.orCreatedAt = orCreatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", sum=" + sum +
                ", orCreatedAt=" + orCreatedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
