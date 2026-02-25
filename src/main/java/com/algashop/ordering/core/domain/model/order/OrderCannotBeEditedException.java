package com.algashop.ordering.core.domain.model.order;

import com.algashop.ordering.core.domain.model.DomainException;
import com.algashop.ordering.core.domain.model.ErrorMessages;

public class OrderCannotBeEditedException extends DomainException {

    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ErrorMessages.ERRO_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
