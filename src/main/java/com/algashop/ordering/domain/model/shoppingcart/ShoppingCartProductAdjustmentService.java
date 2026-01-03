package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.commons.Money;
import com.algashop.ordering.domain.model.product.ProductId;

public interface ShoppingCartProductAdjustmentService {

    void adjustPrice(ProductId productId, Money updatePrice);

    void changeAvailability(ProductId productId, boolean available);
}
