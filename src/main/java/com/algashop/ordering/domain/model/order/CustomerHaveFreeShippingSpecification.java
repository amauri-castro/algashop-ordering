package com.algashop.ordering.domain.model.order;

import com.algashop.ordering.domain.model.Specification;
import com.algashop.ordering.domain.model.customer.Customer;
import com.algashop.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

    private final Orders orders;

    private final int minPointsForFreeShippingRule1;
    private final long salesQuantityForFreeShippingRule1;

    private final int mintPoinsForFreeShippingRule2;

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule1)) >= 0
                && orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= salesQuantityForFreeShippingRule1
                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(mintPoinsForFreeShippingRule2)) >= 0;
    }
}
