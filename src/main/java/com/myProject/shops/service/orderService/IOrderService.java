package com.myProject.shops.service.orderService;

import com.myProject.shops.dto.OrderDto;
import com.myProject.shops.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getOrderByUser(Long userId);

    OrderDto convertToDto(Order order);
}
