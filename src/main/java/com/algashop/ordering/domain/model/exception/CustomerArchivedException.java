package com.algashop.ordering.domain.model.exception;

import static com.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {


    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }

    public CustomerArchivedException(Throwable cause) {
        super(ERROR_CUSTOMER_ARCHIVED, cause);
    }
}
