package com.algashop.ordering.domain.model.exception;

import com.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemIncompatibleException extends DomainException {


    public ShoppingCartItemIncompatibleException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
