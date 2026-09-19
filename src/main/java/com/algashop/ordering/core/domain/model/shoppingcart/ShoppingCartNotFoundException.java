package com.algashop.ordering.core.domain.model.shoppingcart;

import com.algashop.ordering.core.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {
    public ShoppingCartNotFoundException(String message) {
        super(message);
    }

    public ShoppingCartNotFoundException(UUID shoppingCartId) {
        super(String.format("Shopping cart %s not found", shoppingCartId));
    }

    public static ShoppingCartNotFoundException ofCustomer(UUID customerId) {
        return new ShoppingCartNotFoundException("Shopping cart for customer ID " + customerId + "not found.");
    }

}
