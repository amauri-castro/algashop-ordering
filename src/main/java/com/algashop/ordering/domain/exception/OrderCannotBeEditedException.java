package com.algashop.ordering.domain.exception;

import com.algashop.ordering.domain.entity.OrderStatus;
import com.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBeEditedException extends DomainException {

    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ErrorMessages.ERRO_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
