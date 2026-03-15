package com.demo.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Instant createdAt;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price, String description, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
