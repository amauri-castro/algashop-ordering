package com.algashop.ordering.domain.model.order;

import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.ErrorMessages;

public class OrderDoesNotContainOrderItemExeption extends DomainException {

    public OrderDoesNotContainOrderItemExeption(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }
}
