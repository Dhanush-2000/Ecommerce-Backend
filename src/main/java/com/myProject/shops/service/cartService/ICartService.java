package com.myProject.shops.service.cartService;

import com.myProject.shops.model.Cart;
import com.myProject.shops.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeCartId(User user);

    Cart getCartByUserId(Long userId);
}
