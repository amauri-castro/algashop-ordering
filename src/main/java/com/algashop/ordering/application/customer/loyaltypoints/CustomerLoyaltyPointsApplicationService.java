package com.algashop.ordering.application.customer.loyaltypoints;

import com.algashop.ordering.domain.model.customer.*;
import com.algashop.ordering.domain.model.order.Order;
import com.algashop.ordering.domain.model.order.OrderId;
import com.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algashop.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {

    private final CustomerLoyaltyPointsService customerLoyaltyPointsService;
    private final Customers customers;
    private final Orders orders;

    public void addLoyaltyPoints(UUID rawCustomerId, String rawOrderId) {
        CustomerId customerId = new CustomerId(rawCustomerId);
        OrderId orderId = new OrderId(rawOrderId);

        Customer customer = customers.ofId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException());

        Order order = orders.ofId(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        customerLoyaltyPointsService.addPoints(customer, order);

        customers.add(customer);
    }
}
