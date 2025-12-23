package com.algashop.ordering.domain.model.service;

import com.algashop.ordering.domain.model.entity.Customer;
import com.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.algashop.ordering.domain.model.repository.Customers;
import com.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algashop.ordering.domain.model.utility.DomainService;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(CustomerId customerId) {
        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        if (shoppingCarts.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException();
        }

        return ShoppingCart.startShopping(customerId);

    }
}
