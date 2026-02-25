package com.algashop.ordering.core.domain.model.order;

import com.algashop.ordering.core.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderCanceledEvent(OrderId orderId,
                                 CustomerId customerId,
                                 OffsetDateTime canceledAt) {
}
