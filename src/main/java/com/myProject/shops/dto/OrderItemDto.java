package com.myProject.shops.dto;

import com.myProject.shops.model.Order;
import com.myProject.shops.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemDto {

    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;

}
