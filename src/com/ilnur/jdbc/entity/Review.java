package com.ilnur.jdbc.entity;

import java.time.LocalDateTime;

public class Review {

    private Long id;
    private Long productId;
    private int costumerId;
    private String contentRv;
    private LocalDateTime rvCreatedAt;

    public Review() {
    }

    public Review(Long id, Long productId, int costumerId, String contentRv, LocalDateTime rvCreatedAt) {
        this.id = id;
        this.productId = productId;
        this.costumerId = costumerId;
        this.contentRv = contentRv;
        this.rvCreatedAt = rvCreatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public String getContentRv() {
        return contentRv;
    }

    public void setContentRv(String contentRv) {
        this.contentRv = contentRv;
    }

    public LocalDateTime getRvCreatedAt() {
        return rvCreatedAt;
    }

    public void setRvCreatedAt(LocalDateTime rvCreatedAt) {
        this.rvCreatedAt = rvCreatedAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "productId=" + productId +
                ", costumerId=" + costumerId +
                ", contentRv='" + contentRv + '\'' +
                ", rvCreatedAt=" + rvCreatedAt +
                '}';
    }
}
