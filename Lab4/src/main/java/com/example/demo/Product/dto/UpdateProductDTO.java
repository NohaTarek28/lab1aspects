package com.example.demo.Product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateProductDTO {

    @Size(min = 1, max = 20, message = "SKU must be between 1 and 20 characters")
    private String sku;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity must be 0 or more")
    private Integer stockQuantity;

    private Boolean available;

    public UpdateProductDTO() {}

    public UpdateProductDTO(String sku, String name, BigDecimal price, Integer stockQuantity, Boolean available) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.available = available;
    }

    // Getters and Setters
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
