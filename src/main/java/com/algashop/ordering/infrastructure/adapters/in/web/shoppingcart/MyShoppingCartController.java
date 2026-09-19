package com.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.algashop.ordering.core.application.security.SecurityChecks;
import com.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppingCarts;
import com.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import com.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import com.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/me/shopping-cart")
@RequiredArgsConstructor
public class MyShoppingCartController {


    private final ForManagingShoppingCarts forManagingShoppingCarts;
    private final ForQueryingShoppingCarts forQueryingShoppingCarts;
    private final SecurityChecks securityChecks;


    @SecurityAnnotations.CanWriteMyShoppingCart
    @PostMapping
    public ResponseEntity<ShoppingCartOutput> create() {
        forManagingShoppingCarts.createNew(securityChecks.getAuthenticatedUserId());
        return ResponseEntity.created(URI.create("/api/v1/customers/me/shopping-cart"))
                .body(findAuthenticatedCustomerShoppingCart());
    }

    @SecurityAnnotations.CanReadMyShoppingCart
    @GetMapping
    public ShoppingCartOutput get() {
        return findAuthenticatedCustomerShoppingCart();
    }

    @SecurityAnnotations.CanReadMyShoppingCart
    @GetMapping("/items")
    public ShoppingCartItemListModel getItems() {
        return new ShoppingCartItemListModel(findAuthenticatedCustomerShoppingCart().getItems());
    }

    @SecurityAnnotations.CanWriteMyShoppingCart
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@RequestBody @Valid ShoppingCartItemInput input) {
        ShoppingCartOutput shoppingCart = findAuthenticatedCustomerShoppingCart();
        input.setShoppingCartId(shoppingCart.getId());
        try {
            forManagingShoppingCarts.addItem(input);
        } catch (ProductNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
    }

    @SecurityAnnotations.CanWriteMyShoppingCart
    @DeleteMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void empty() {
        ShoppingCartOutput shoppingCart = findAuthenticatedCustomerShoppingCart();
        forManagingShoppingCarts.empty(shoppingCart.getId());
    }

    @SecurityAnnotations.CanWriteMyShoppingCart
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable UUID itemId) {
        ShoppingCartOutput shoppingCart = findAuthenticatedCustomerShoppingCart();
        forManagingShoppingCarts.removeItem(shoppingCart.getId(), itemId);
    }

    private ShoppingCartOutput findAuthenticatedCustomerShoppingCart() {
        return forQueryingShoppingCarts.findByCustomerId(securityChecks.getAuthenticatedUserId());
    }
}
