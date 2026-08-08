package com.algashop.ordering.core.domain.model.shoppingcart;

import com.algashop.ordering.core.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {
    public ShoppingCartNotFoundException() {
    }

    public ShoppingCartNotFoundException(UUID shoppingCartId) {
        super(String.format("Shopping cart %s not found", shoppingCartId));
    }

}
