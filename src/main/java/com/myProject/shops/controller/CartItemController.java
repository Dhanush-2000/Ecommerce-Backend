package com.myProject.shops.controller;

import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.Cart;
import com.myProject.shops.model.CartItems;
import com.myProject.shops.model.User;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.cartItemService.ICartItemService;
import com.myProject.shops.service.cartService.ICartService;
import com.myProject.shops.service.userService.IUserService;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;
    @PostMapping("/addItemToCart")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam int quantity){
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeCartId(user);


            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("item added to the cart",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch(JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));

        }

    }
    @DeleteMapping("cart/{cartId}/product/{productId}/removeItemFromCart")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,@PathVariable Long productId){
        try {
            cartItemService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("item removed from the cart",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }

    }
    @PutMapping("cart/{cartId}/product/{productId}/updateQuantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                     @PathVariable Long productId,
                                                     @RequestParam int quantity){
        try {
            cartItemService.updateItemQuantity(cartId,productId,quantity);
            return ResponseEntity.ok(new ApiResponse("quantity updated",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }

    }
    @GetMapping("cart/{cartId}/product/{productId}/getCartItem")
    public ResponseEntity<ApiResponse> getCartItem(@PathVariable Long cartId,@PathVariable Long productId){
        try {
            CartItems cartItem = cartItemService.getCartItem(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("success",cartItem));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }

}
