package com.algashop.ordering.domain.model.shoppingcart;

import com.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algashop.ordering.domain.model.customer.Customers;
import com.algashop.ordering.domain.model.DomainService;
import com.algashop.ordering.domain.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;

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
