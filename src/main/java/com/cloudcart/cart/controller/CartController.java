package com.cloudcart.cart.controller;

import com.cloudcart.cart.dto.CartItemRequest;
import com.cloudcart.cart.entity.Cart;
import com.cloudcart.cart.entity.CartItem;
import com.cloudcart.cart.repository.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    private Cart getOrCreateCart(String username) {
        return cartRepository.findByUsername(username).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUsername(username);
            return cartRepository.save(newCart);
        });
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        return ResponseEntity.ok(getOrCreateCart(authentication.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(Authentication authentication, @RequestBody CartItemRequest request) {
        Cart cart = getOrCreateCart(authentication.getName());

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return ResponseEntity.ok(cartRepository.save(cart));
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateItem(Authentication authentication, @RequestBody CartItemRequest request) {
        Cart cart = getOrCreateCart(authentication.getName());

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(request.getQuantity()));

        return ResponseEntity.ok(cartRepository.save(cart));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeItem(Authentication authentication, @PathVariable Long productId) {
        Cart cart = getOrCreateCart(authentication.getName());
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return ResponseEntity.ok(cartRepository.save(cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart(Authentication authentication) {
        Cart cart = getOrCreateCart(authentication.getName());
        cart.getItems().clear();
        return ResponseEntity.ok(cartRepository.save(cart));
    }
}
