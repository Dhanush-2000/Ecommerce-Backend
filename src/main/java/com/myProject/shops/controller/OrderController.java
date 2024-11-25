package com.myProject.shops.controller;

import com.myProject.shops.dto.OrderDto;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Order;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.orderService.IOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;
    @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long orderId){
        try {
            Order order = orderService.placeOrder(orderId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("item ordered successfully",orderDto));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("order not created",e.getMessage()));
        }

    }

    @GetMapping("{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("order found",order));
        } catch (ResourceNotFoundException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No Order Found",e.getMessage()));
        }
    }

    @GetMapping("{orderId}/ordersByUser")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long orderId){
        try {
            List<OrderDto> order = orderService.getOrderByUser(orderId);
            return ResponseEntity.ok(new ApiResponse("orders found",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No Order Found",e.getMessage()));
        }
    }
}
