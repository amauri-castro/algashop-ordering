package com.algashop.ordering.domain.model.customer;

import com.algashop.ordering.domain.model.DomainException;
import com.algashop.ordering.domain.model.ErrorMessages;

public class CustomerEmailIsInUseException extends DomainException {

    public CustomerEmailIsInUseException(CustomerId customerId) {
        super(ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE);
    }
}
