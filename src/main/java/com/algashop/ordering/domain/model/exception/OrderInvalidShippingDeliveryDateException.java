package com.algashop.ordering.domain.model.exception;

import com.algashop.ordering.domain.model.valueobject.id.OrderId;

public class OrderInvalidShippingDeliveryDateException extends DomainException {


    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_IN_THE_PAST, id));
    }
}
