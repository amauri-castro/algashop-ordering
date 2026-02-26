package com.algashop.ordering.core.application.order;

import com.algashop.ordering.core.domain.model.order.Order;
import com.algashop.ordering.core.domain.model.order.OrderId;
import com.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.algashop.ordering.core.domain.model.order.Orders;
import com.algashop.ordering.core.ports.in.order.ForManagingOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService implements ForManagingOrders {

    private final Orders orders;

    @Transactional
    @Override
    public void cancel(Long rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);

        order.cancel();
        orders.add(order);
    }

    @Transactional
    @Override
    public void markAsPaid(Long rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);

        order.markAsPaid();
        orders.add(order);
    }

    @Transactional
    @Override
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
