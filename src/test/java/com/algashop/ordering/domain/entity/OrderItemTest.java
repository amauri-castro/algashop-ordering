package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.Quantity;
import com.algashop.ordering.domain.valueobject.id.OrderId;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerate() {
        OrderItem.brandNew()
                .product(ProductTestDataBuilder.aProductMacbook().build())
                .quantity(new Quantity(1))
                .orderId(new OrderId())
                .build();
    }

}