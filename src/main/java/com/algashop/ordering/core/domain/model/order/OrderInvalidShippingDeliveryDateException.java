package com.algashop.ordering.core.domain.model.order;

import com.algashop.ordering.core.domain.model.DomainException;
import com.algashop.ordering.core.domain.model.ErrorMessages;

public class OrderInvalidShippingDeliveryDateException extends DomainException {


    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_IN_THE_PAST, id));
    }
}
