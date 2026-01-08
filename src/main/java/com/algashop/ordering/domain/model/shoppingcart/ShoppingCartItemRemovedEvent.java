package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.customer.CustomerId;
import com.algashop.ordering.domain.model.product.ProductId;

import java.time.OffsetDateTime;

public record ShoppingCartItemRemovedEvent(ShoppingCartId shoppingCartId,
                                           CustomerId customerId,
                                           ProductId productId,
                                           OffsetDateTime removedAt) {
}
