package com.algashop.ordering.domain.model.order;

import com.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.algashop.ordering.domain.model.product.Product;
import com.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerateBrandNewOrderItem() {
        Product product = ProductTestDataBuilder.aProductMacbook().build();

        Quantity quantity = new Quantity(1);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNew()
                .product(product)
                .quantity(quantity)
                .orderId(orderId)
                .build();

        Assertions.assertWith(orderItem,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.productId()).isEqualTo(product.id()),
                o -> Assertions.assertThat(o.productName()).isEqualTo(product.name()),
                o -> Assertions.assertThat(o.price()).isEqualTo(product.price()),
                o -> Assertions.assertThat(o.quantity()).isEqualTo(quantity),
                o -> Assertions.assertThat(o.orderId()).isEqualTo(orderId)
                );
    }

}