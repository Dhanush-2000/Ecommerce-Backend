package com.myProject.shops.dto;

import com.myProject.shops.enums.OrderStatus;
import com.myProject.shops.model.OrderItem;
import com.myProject.shops.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public class OrderDto {

    private long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private User user;
    private List<OrderItemDto> Items;
}
