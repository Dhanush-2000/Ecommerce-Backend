package com.myProject.shops.service.cartItemService;

import com.myProject.shops.model.CartItems;

public interface ICartItemService {

    void addItemToCart(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItems getCartItem(Long cartId, Long productId);
}
