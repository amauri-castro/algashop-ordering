package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.ErrorMessages;
import com.algashop.ordering.domain.model.product.ProductId;

public class ShoppingCartItemIncompatibleException extends DomainException {


    public ShoppingCartItemIncompatibleException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
