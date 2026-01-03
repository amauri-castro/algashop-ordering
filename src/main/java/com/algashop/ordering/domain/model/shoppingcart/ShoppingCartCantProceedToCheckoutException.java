package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.ErrorMessages;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {


    public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT, id));
    }
}
