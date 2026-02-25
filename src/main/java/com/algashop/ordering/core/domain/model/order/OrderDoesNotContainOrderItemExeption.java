package com.algashop.ordering.core.domain.model.order;

import com.algashop.ordering.core.domain.model.DomainException;
import com.algashop.ordering.core.domain.model.ErrorMessages;

public class OrderDoesNotContainOrderItemExeption extends DomainException {

    public OrderDoesNotContainOrderItemExeption(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }
}
