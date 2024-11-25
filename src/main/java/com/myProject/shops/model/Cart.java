package com.myProject.shops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount=BigDecimal.ZERO;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItems> items = new HashSet<>();

    public void addItem(CartItems item){
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItems item){
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount= items.stream().map(item->{
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice==null){
                return BigDecimal.ZERO;
            }return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}