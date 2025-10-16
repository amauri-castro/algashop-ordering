package com.algashop.ordering.domain.exception;

import com.algashop.ordering.domain.valueobject.id.OrderId;
import com.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderDoesContainOrderItemExeption extends DomainException {

    public OrderDoesContainOrderItemExeption(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERRO_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }
}
