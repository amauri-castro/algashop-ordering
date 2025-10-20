package com.algashop.ordering.domain.model.exception;

import com.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, productId));
    }
}
