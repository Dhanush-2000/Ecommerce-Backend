package com.myProject.shops.repository;

import com.myProject.shops.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    void deleteAllByCartId(Long id);
}