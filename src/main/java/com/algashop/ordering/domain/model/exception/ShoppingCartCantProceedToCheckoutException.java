package com.algashop.ordering.domain.model.exception;

import com.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

public class ShoppingCartCantProceedToCheckoutException extends DomainException{


    public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT, id));
    }
}
