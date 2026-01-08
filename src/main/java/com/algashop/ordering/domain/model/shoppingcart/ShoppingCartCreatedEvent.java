package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartCreatedEvent(ShoppingCartId shoppingCartId,
                                       CustomerId customerId,
                                       OffsetDateTime createdAt) {
}
