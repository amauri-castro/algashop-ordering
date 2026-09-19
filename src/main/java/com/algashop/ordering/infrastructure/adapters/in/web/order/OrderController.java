package com.algashop.ordering.infrastructure.adapters.in.web.order;

import com.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.algashop.ordering.core.ports.in.order.OrderFilter;
import com.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import com.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadOrders;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ForQueryingOrders forQueryingOrders;

    @GetMapping("/{orderId}")
    @CanReadOrders
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return forQueryingOrders.findById(orderId);
    }

    @GetMapping
    @CanReadOrders
    public PageModel<OrderSummaryOutput> filter(OrderFilter filter) {
        return PageModel.of(forQueryingOrders.filter(filter));
    }

}
