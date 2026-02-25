package com.algashop.ordering.core.domain.model.shoppingcart;

import com.algashop.ordering.core.domain.model.DomainException;
import com.algashop.ordering.core.domain.model.ErrorMessages;
import com.algashop.ordering.core.domain.model.product.ProductId;

public class ShoppingCartItemIncompatibleException extends DomainException {


    public ShoppingCartItemIncompatibleException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
