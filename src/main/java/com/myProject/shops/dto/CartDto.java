package com.myProject.shops.dto;

import java.math.BigDecimal;
import java.util.Set;

public class CartDto {

    private Long id;
    private BigDecimal totalAmount;
    private Set<CartItemDto> cartItems;
}
