package com.algashop.ordering.domain.model.order;

import com.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderPlacedEvent(OrderId orderId,
                               CustomerId customerId,
                               OffsetDateTime placedAt) {
}
