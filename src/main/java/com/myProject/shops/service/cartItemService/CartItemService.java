package com.myProject.shops.service.cartItemService;

import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Cart;
import com.myProject.shops.model.CartItems;
import com.myProject.shops.model.Product;
import com.myProject.shops.repository.CartItemsRepository;
import com.myProject.shops.repository.CartRepository;
import com.myProject.shops.repository.ProductRepository;
import com.myProject.shops.service.cartService.ICartService;
import com.myProject.shops.service.productService.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final ProductRepository productRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //get the cartId
        // get the product using productId
        // check the product already exists in the  cart
        // if present increase the quantity by the given  quantity
        // else add the new cartItem the cart

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItems cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(new CartItems());

        if (cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemsRepository.save(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItems itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);


    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);

        cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent((items)->{
                    items.setQuantity(quantity);
                    items.setUnitPrice(items.getProduct().getPrice());
                    items.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
    @Override
    public CartItems getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }
}
