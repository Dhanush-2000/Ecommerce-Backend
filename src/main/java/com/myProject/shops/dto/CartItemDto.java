package com.myProject.shops.dto;

import com.myProject.shops.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

public class CartItemDto {

    private Long id;
    private ProductDto product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

}
