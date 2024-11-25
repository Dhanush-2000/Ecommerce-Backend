package com.myProject.shops.service.cartService;

import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Cart;
import com.myProject.shops.model.User;
import com.myProject.shops.repository.CartItemsRepository;
import com.myProject.shops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cart not found!!"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemsRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();

    }

    @Override
    public Cart initializeCartId(User user){
       return Optional.ofNullable(getCartByUserId(user.getId()))
               .orElseGet(()->{
                   Cart cart = new Cart();
                   cart.setUser(user);
                   return cartRepository.save(cart);
               });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
