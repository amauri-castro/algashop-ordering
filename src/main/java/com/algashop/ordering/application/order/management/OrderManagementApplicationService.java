package com.algashop.ordering.application.order.management;

import com.algashop.ordering.domain.model.order.Order;
import com.algashop.ordering.domain.model.order.OrderId;
import com.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algashop.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    private final Orders orders;

    @Transactional
    public void cancel(Long rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);

        order.cancel();
        orders.add(order);
    }

    @Transactional
    public void markAsPaid(Long rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);

        order.markAsPaid();
        orders.add(order);
    }

    @Transactional
    public void markAsReady(Long rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);

        order.markAsReady();
        orders.add(order);
    }

    private Order findOrder(Long rawOrderId) {
        return orders.ofId(new OrderId(rawOrderId))
                .orElseThrow(() -> new OrderNotFoundException());
    }
}
