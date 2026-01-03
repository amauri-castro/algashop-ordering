package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.ErrorMessages;
import com.algashop.ordering.domain.model.product.ProductId;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, productId));
    }
}
