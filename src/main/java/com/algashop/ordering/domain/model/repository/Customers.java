package com.algashop.ordering.domain.model.repository;

import com.algashop.ordering.domain.model.entity.Customer;
import com.algashop.ordering.domain.model.valueobject.Email;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
    boolean isEmailUnique(Email email, CustomerId exceptCustomerId);
}
