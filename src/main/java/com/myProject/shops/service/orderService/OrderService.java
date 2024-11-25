package com.myProject.shops.service.orderService;

import com.myProject.shops.dto.OrderDto;
import com.myProject.shops.enums.OrderStatus;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.*;
import com.myProject.shops.repository.OrderRepository;
import com.myProject.shops.repository.ProductRepository;
import com.myProject.shops.service.cartService.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItem = createOrderItem(order, cart);
        order.setOrderItems(new HashSet<>(orderItem));
        order.setTotalAmount(calcTotalAmount(orderItem));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart){
        return cart.getItems().stream().map(cartItems->{
            Product product = cartItems.getProduct();
            product.setInventory(product.getInventory()- cartItems.getQuantity());
            productRepository.save(product);
            return new OrderItem(order,product,cartItems.getQuantity(),cartItems.getUnitPrice());
        }).toList();

    }

    private BigDecimal calcTotalAmount(List<OrderItem> orderItemList){
        return orderItemList
                .stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);


    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository
                .findById(orderId).map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("order not found")) ;
    }
    @Override
    public List<OrderDto> getOrderByUser(Long userId){
        return orderRepository
                .findByUserId(userId)
                .stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
